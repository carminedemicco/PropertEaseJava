package main.propertease.server.strategy;

import main.propertease.server.mediator.AbstractClientManager;
import main.propertease.server.proxy.DatabaseConnectionProxy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
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
        final var database = new DatabaseConnectionProxy();
        final var message = new JSONObject(line);
        try {
            final var request = message.getString("request");
            switch (request) {
                case "getAppointmentsForUser": {
                    final var parameters = message.getJSONObject("parameters");
                    final var username = parameters.getString("username");
                    final var query = """
                      select
                          A.ROWID as id,
                          A.time as time,
                          U.first_name as first_name,
                          U.last_name as last_name,
                          H.address as address
                        from Appointment as A
                          inner join main.User U on U.username = A.buyer
                          inner join main.House H on H.ROWID = A.house
                        where A.agent = ?
                    """;
                    final var queryParameters = Collections.<Object>singletonList(username);
                    database.executeQuery(query, Optional.of(queryParameters), (result) -> {
                        final var appointments = new JSONArray();
                        final var response = new JSONObject();
                        try {
                            while (result.next()) {
                                final var appointment = new JSONObject();
                                appointment.put("id", result.getInt("id"));
                                appointment.put("time", result.getString("time"));
                                appointment.put("agent", result.getString("first_name") + " " + result.getString("last_name"));
                                appointment.put("address", result.getString("address"));
                                appointments.put(appointment);
                            }
                            response.put("response", appointments);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        clientManager.writeLine(response.toString());
                    });
                    break;
                }

                case "insertAgentAvailability": {
                    final var parameters = message.getJSONObject("parameters");
                    final var user = parameters.getString("agent");
                    final var startDate = parameters.getString("start_date");
                    final var endDate = parameters.getString("end_date");
                    final var query = "insert into Availability values (?, ?, ?)";
                    final var queryParameters = Arrays.<Object>asList(user, startDate, endDate);
                    final var response = new JSONObject();
                    try {
                        database.executeUpdate(query, Optional.of(queryParameters));
                        response.put("response", new JSONObject());
                    } catch (SQLException e) {
                        response.put("response", JSONObject.NULL);
                    }
                    clientManager.writeLine(response.toString());
                    break;
                }
            }
        } catch (JSONException e) {
            clientManager.writeLine(clientManager.makeErrorMessage(e.getMessage()));
        }
        return true;
    }

    private final AbstractClientManager clientManager;
}
