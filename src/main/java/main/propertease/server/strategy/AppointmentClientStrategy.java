package main.propertease.server.strategy;

import main.propertease.server.ClientManager;
import main.propertease.server.proxy.DatabaseConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

/**
 * Strategy to handle Appointment related messages and requests.
 */
public class AppointmentClientStrategy implements ClientManagerStrategy {
    /**
     * Creates a new strategy to handle Appointment related messages and requests.
     *
     * @param clientManager The client manager to use.
     */
    public AppointmentClientStrategy(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    /**
     * Implements client/server communication.
     *
     * @return <b>true</b> if the client should continue to be managed, <b>false</b> otherwise.
     */
    @Override
    public boolean communicate() {
        final var line = clientManager.readLine();
        if (line == null) {
            clientManager.close();
            return false;
        }
        final var database = clientManager.getDatabase();
        final var message = new JSONObject(line);
        try {
            final var request = message.getString("request");
            switch (request) {
                case "getAppointmentsForUser":
                    handleGetAppointments(database, message, false);
                    break;

                case "getAppointmentsForAgent":
                    handleGetAppointments(database, message, true);
                    break;

                case "insertAgentAvailability":
                    insertAgentAvailability(database, message);
                    break;

                case "bookAppointment":
                    bookAppointment(database, message);
                    break;

                case "removeAppointment":
                    removeAppointment(database, message);
                    break;
            }
        } catch (JSONException e) {
            clientManager.writeLine(clientManager.makeErrorMessage(e.getMessage()));
        }
        return true;
    }

    /**
     * Inserts the availability of an agent in the database.
     *
     * @param database The database connection handle.
     * @param message  The message containing info regarding agent availability.
     */
    private void insertAgentAvailability(DatabaseConnection database, JSONObject message) {
        final var parameters = message.getJSONObject("parameters");
        final var user = parameters.getString("agent");
        final var startDate = LocalDate.parse(parameters.getString("start_date"));
        final var endDate = LocalDate.parse(parameters.getString("end_date"));
        final var query = "insert into Availability values (?, ?)";
        final var response = new JSONObject();
        for (var date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {
            final var queryParameters = Arrays.<Object>asList(date, user);
            try {
                database.executeUpdate(query, queryParameters);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        response.put("response", new JSONObject());
        clientManager.writeLine(response.toString());
    }

    /**
     * Books an appointment for a user.
     *
     * @param database The database connection handle.
     * @param message  The message containing info regarding the appointment to book.
     */
    private void bookAppointment(DatabaseConnection database, JSONObject message) {
        final var parameters = message.getJSONObject("parameters");
        final var buyer = parameters.getString("username");
        final var date = parameters.getString("date");
        final var house = parameters.getInt("house");
        // controlla la disponibilità
        final var query = "select agent from Availability where time = ? and agent <> ?";
        final var queryParameters = Arrays.<Object>asList(date, buyer);
        final var response = new JSONObject();
        response.put("response", JSONObject.NULL);
        database.executeQuery(query, queryParameters, (result) -> {
            try {
                if (result.next()) {
                    final var agent = result.getString("agent");
                    // controllo se esistono altri appuntamenti per lo stesso giorno
                    final var checkQuery = """
                            select count(*) as count
                              from Appointment
                              where time = ? and
                                    agent = ? and
                                    buyer = ?
                        """;
                    final var checkQueryParameters = Arrays.<Object>asList(date, agent, buyer);
                    database.executeQuery(checkQuery, checkQueryParameters, (checkResult) -> {
                        final var insertQuery = "insert into Appointment values (?, ?, ?, ?)";
                        final var insertQueryParameters = Arrays.<Object>asList(date, buyer, agent, house);
                        final var deleteAvailabilityQuery = "delete from Availability where time = ? and agent = ?";
                        final var deleteAvailabilityQueryParameters = Arrays.<Object>asList(date, agent);
                        try {
                            if (checkResult.next()) {
                                if (checkResult.getInt("count") > 0) {
                                    response.put("response", new JSONObject().put("error", "alreadyBooked"));
                                } else {
                                    database.executeUpdate(insertQuery, insertQueryParameters);
                                    database.executeUpdate(deleteAvailabilityQuery, deleteAvailabilityQueryParameters);
                                    response.put("response", new JSONObject());
                                }
                            } else {
                                response.put("response", new JSONObject().put("error", "unknown"));
                            }
                        } catch (SQLException e) {
                            response.put("response", new JSONObject().put("error", "unknown"));
                        }
                    });
                } else {
                    response.put("response", new JSONObject().put("error", "notAvailable"));
                }
            } catch (SQLException e) {
                response.put("response", new JSONObject().put("error", "unknown"));
            } finally {
                clientManager.writeLine(response.toString());
            }
        });
    }

    /**
     * Removes an appointment from the database.
     *
     * @param database The database connection handle.
     * @param message  The message containing info regarding the appointment to remove.
     */
    private void removeAppointment(DatabaseConnection database, JSONObject message) {
        final var parameters = message.getJSONObject("parameters");
        final var id = parameters.getInt("id");
        final var query = "select time, agent from Appointment where ROWID = ?";
        final var queryParameters = Collections.<Object>singletonList(id);
        final var response = new JSONObject();
        database.executeQuery(query, queryParameters, (result) -> {
            try {
                if (result.next()) {
                    final var date = result.getString("time");
                    final var agent = result.getString("agent");
                    final var deleteQuery = "delete from Appointment where ROWID = ?";
                    final var deleteQueryParameters = Collections.<Object>singletonList(id);
                    final var insertQuery = "insert into Availability values (?, ?)";
                    final var insertQueryParameters = Arrays.<Object>asList(agent, date);
                    try {
                        database.executeUpdate(deleteQuery, deleteQueryParameters);
                        database.executeUpdate(insertQuery, insertQueryParameters);
                        response.put("response", new JSONObject());
                    } catch (SQLException e) {
                        response.put("response", new JSONObject().put("error", "unknown"));
                    }
                } else {
                    response.put("response", new JSONObject().put("error", "notFound"));
                }
            } catch (SQLException e) {
                response.put("response", new JSONObject().put("error", "unknown"));
            } finally {
                clientManager.writeLine(response.toString());
            }
        });
    }

    /**
     * Handles the request to get appointments for a user or an agent.
     *
     * @param database The database connection handle.
     * @param message  The message containing info regarding the request.
     * @param isAgent  <b>true</b> if the request is for an agent, <b>false</b> otherwise.
     */
    private void handleGetAppointments(DatabaseConnection database, JSONObject message, boolean isAgent) {
        final var parameters = message.getJSONObject("parameters");
        final var username = parameters.getString("username");
        var query = new String();
        if (isAgent) {
            query = """
                    select
                        A.ROWID as id,
                        A.time as time,
                        U.first_name as first_name,
                        U.last_name as last_name,
                        H.address as address,
                        H.type as type
                      from Appointment as A
                        inner join User U on U.username = A.buyer
                        inner join House H on H.ROWID = A.house
                      where A.agent = ?
                """;
        } else {
            query = """
                    select
                        A.ROWID as id,
                        A.time as time,
                        U.first_name as first_name,
                        U.last_name as last_name,
                        H.address as address,
                        H.type as type
                      from Appointment as A
                        inner join User U on U.username = A.agent
                        inner join House H on H.ROWID = A.house
                      where A.buyer = ?
                """;
        }
        final var queryParameters = Collections.<Object>singletonList(username);
        database.executeQuery(query, queryParameters, (result) -> {
            final var appointments = new JSONArray();
            final var response = new JSONObject();
            try {
                while (result.next()) {
                    final var appointment = new JSONObject();
                    final var agent = new JSONObject();
                    agent.put("first_name", result.getString("first_name"));
                    agent.put("last_name", result.getString("last_name"));
                    final var house = new JSONObject();
                    house.put("address", result.getString("address"));
                    house.put("type", result.getString("type"));
                    appointment.put("id", result.getInt("id"));
                    appointment.put("date", result.getString("time"));
                    appointment.put("agent", agent);
                    appointment.put("house", house);
                    appointments.put(appointment);
                }
                response.put("response", appointments);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                clientManager.writeLine(response.toString());
            }
        });
    }

    private final ClientManager clientManager;
}
