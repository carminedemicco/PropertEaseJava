package main.propertease.server.strategy;

import main.propertease.server.mediator.AbstractClientManager;
import main.propertease.server.proxy.DatabaseConnection;
import main.propertease.server.proxy.DatabaseConnectionProxy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

public class AppointmentClientStrategy implements ClientManagerStrategy {
    public AppointmentClientStrategy(AbstractClientManager clientManager) {
        this.clientManager = clientManager;
    }

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
                case "getAppointmentsForUser": {
                    handleGetAppointments(database, message, false);
                    break;
                }

                case "getAppointmentsForAgent": {
                    handleGetAppointments(database, message, true);
                    break;
                }

                case "insertAgentAvailability": {
                    final var parameters = message.getJSONObject("parameters");
                    final var user = parameters.getString("agent");
                    final var startDate = LocalDate.parse(parameters.getString("start_date"));
                    final var endDate = LocalDate.parse(parameters.getString("end_date"));
                    final var query = "insert into Availability values (?, ?)";
                    final var response = new JSONObject();
                    try {
                        for (var date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {
                            final var queryParameters = Arrays.<Object>asList(user, date);
                            database.executeUpdate(query, Optional.of(queryParameters));
                        }
                        response.put("response", new JSONObject());
                    } catch (SQLException e) {
                        response.put("response", JSONObject.NULL);
                    } finally {
                        clientManager.writeLine(response.toString());
                    }
                    break;
                }

                case "bookAppointment": {
                    final var parameters = message.getJSONObject("parameters");
                    final var buyer = parameters.getString("username");
                    final var date = parameters.getString("date");
                    final var house = parameters.getInt("house");
                    // controlla la disponibilità
                    final var query = "select agent from Availability where time = ? and agent <> ?";
                    final var queryParameters = Arrays.<Object>asList(date, buyer);
                    final var response = new JSONObject();
                    response.put("response", JSONObject.NULL);
                    database.executeQuery(query, Optional.of(queryParameters), (result) -> {
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
                                database.executeQuery(checkQuery, Optional.of(checkQueryParameters), (checkResult) -> {
                                    final var insertQuery = "insert into Appointment values (?, ?, ?, ?)";
                                    final var insertQueryParameters = Arrays.<Object>asList(date, buyer, agent, house);
                                    final var deleteAvailabilityQuery = "delete from Availability where time = ? and agent = ?";
                                    final var deleteAvailabilityQueryParameters = Arrays.<Object>asList(date, agent);
                                    try {
                                        if (checkResult.next()) {
                                            if (checkResult.getInt("count") > 0) {
                                                response.put("response", new JSONObject().put("error", "alreadyBooked"));
                                            } else {
                                                database.executeUpdate(insertQuery, Optional.of(insertQueryParameters));
                                                database.executeUpdate(deleteAvailabilityQuery, Optional.of(deleteAvailabilityQueryParameters));
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
                    break;
                }

                case "removeAppointment": {
                    final var parameters = message.getJSONObject("parameters");
                    final var id = parameters.getInt("id");
                    final var query = "select time, agent from Appointment where ROWID = ?";
                    final var queryParameters = Collections.<Object>singletonList(id);
                    final var response = new JSONObject();
                    database.executeQuery(query, Optional.of(queryParameters), (result) -> {
                        try {
                            if (result.next()) {
                                final var date = result.getString("time");
                                final var agent = result.getString("agent");
                                final var deleteQuery = "delete from Appointment where ROWID = ?";
                                final var deleteQueryParameters = Collections.<Object>singletonList(id);
                                final var insertQuery = "insert into Availability values (?, ?)";
                                final var insertQueryParameters = Arrays.<Object>asList(agent, date);
                                try {
                                    database.executeUpdate(deleteQuery, Optional.of(deleteQueryParameters));
                                    database.executeUpdate(insertQuery, Optional.of(insertQueryParameters));
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
                    break;
                }
            }
        } catch (JSONException e) {
            clientManager.writeLine(clientManager.makeErrorMessage(e.getMessage()));
        }
        return true;
    }

    private void handleGetAppointments(DatabaseConnection database, JSONObject message, boolean isAgent) {
        final var parameters = message.getJSONObject("parameters");
        final var username = parameters.getString("username");
        var query = "";
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
                    inner join User U on U.username = A.agent
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
        database.executeQuery(query, Optional.of(queryParameters), (result) -> {
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

    private final AbstractClientManager clientManager;
}
