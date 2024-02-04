package main.propertease.server.strategy;

import main.propertease.server.mediator.AbstractClientManager;
import main.propertease.server.proxy.DatabaseConnectionProxy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.Optional;

public class PosterClientStrategy implements ClientManagerStrategy {
    public PosterClientStrategy(AbstractClientManager clientManager) {
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
                case "getHouses": {
                    try (final var result = database.execute("select ROWID, * from House;", Optional.empty())) {
                        final var houses = new JSONArray();
                        while (result.next()) {
                            final var house = new JSONObject();
                            house.put("id", result.getInt("ROWID"));
                            house.put("type", result.getInt("type"));
                            house.put("address", result.getString("address"));
                            house.put("price", result.getInt("price"));
                            house.put("floor", result.getInt("floor"));
                            house.put("elevator", result.getBoolean("elevator"));
                            house.put("balconies", result.getInt("balconies"));
                            house.put("terrace", result.getInt("terrace"));
                            house.put("garden", result.getInt("garden"));
                            house.put("accessories", result.getInt("accessories"));
                            house.put("bedrooms", result.getInt("bedrooms"));
                            house.put("sqm", result.getInt("sqm"));
                            house.put("description", result.getString("description"));
                            house.put("image0", result.getString("image0"));
                            house.put("image1", result.getString("image1"));
                            house.put("image2", result.getString("image2"));
                            houses.put(house);
                        }
                        final var response = new JSONObject();
                        response.put("response", houses);
                        clientManager.writeLine(response.toString());
                    } catch (SQLException e) {
                        clientManager.writeLine(clientManager.makeErrorMessage(e.getMessage()));
                    }
                } break;
            }
        } catch (JSONException e) {
            clientManager.writeLine(clientManager.makeErrorMessage(e.getMessage()));
        }
        return true;
    }

    private final AbstractClientManager clientManager;
}