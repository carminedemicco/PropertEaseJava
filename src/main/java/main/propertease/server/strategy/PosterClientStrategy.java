package main.propertease.server.strategy;

import main.propertease.server.mediator.AbstractClientManager;
import main.propertease.server.proxy.DatabaseConnectionProxy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
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
                    database.executeQuery("select ROWID, * from House;", Optional.empty(), (result) -> {
                        final var houses = new JSONArray();
                        final var response = new JSONObject();
                        try {
                            while (result.next()) {
                                final var house = new JSONObject();
                                final var houseId = result.getInt("ROWID");
                                house.put("id", houseId);
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
                                house.put("image0", getHouseImageAsBase64(houseId, result.getString("image0")));
                                house.put("image1", getHouseImageAsBase64(houseId, result.getString("image1")));
                                house.put("image2", getHouseImageAsBase64(houseId, result.getString("image2")));
                                houses.put(house);
                            }
                            response.put("response", houses);
                        } catch (Exception e) {
                            e.printStackTrace();
                            response.put("response", JSONObject.NULL);
                        }
                        clientManager.writeLine(response.toString());
                    });
                } break;
            }
        } catch (JSONException e) {
            clientManager.writeLine(clientManager.makeErrorMessage(e.getMessage()));
        }
        return true;
    }

    private static URL getHouseImagePath(int houseId, String name) {
        final var classPath = String.format("/main/propertease/server/images/%d/%s", houseId, name);
        return PosterClientStrategy.class.getResource(classPath);
    }

    private static Object getHouseImageAsBase64(int houseId, String name) {
        final var path = getHouseImagePath(houseId, name);
        // TODO: ritorna immagine di default
        if (path == null) {
            return JSONObject.NULL;
        }
        try {
            final var bytes = Files.readAllBytes(Path.of(path.toURI()));
            final var encoded = Base64.getEncoder().encode(bytes);
            return new String(encoded);
        } catch (Exception e) {
            e.printStackTrace();
            return JSONObject.NULL;
        }
    }

    private final AbstractClientManager clientManager;
}
