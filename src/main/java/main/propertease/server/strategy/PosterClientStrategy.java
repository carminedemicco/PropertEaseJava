package main.propertease.server.strategy;

import main.propertease.server.mediator.AbstractClientManager;
import main.propertease.server.proxy.DatabaseConnection;
import main.propertease.server.proxy.DatabaseConnectionProxy;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
        final var database = clientManager.getDatabase();
        final var message = new JSONObject(line);
        try {
            final var request = message.getString("request");
            switch (request) {
                case "getHouses": {
                    database.executeQuery("select ROWID, * from House;", null, (result) -> {
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
                        } finally {
                            clientManager.writeLine(response.toString());
                        }
                    });
                    break;
                }

                case "insertHouse": {
                    final var parameters = message.getJSONObject("parameters");
                    final var type = parameters.getInt("type");
                    final var address = parameters.getString("address");
                    final var floor = parameters.getInt("floor");
                    final var elevator = parameters.getBoolean("elevator");
                    final var balconies = parameters.getInt("balconies");
                    final var terrace = parameters.getInt("terrace");
                    final var garden = parameters.getInt("garden");
                    final var accessories = parameters.getInt("accessories");
                    final var bedrooms = parameters.getInt("bedrooms");
                    final var sqm = parameters.getInt("sqm");
                    final var price = parameters.getInt("price");
                    final var description = parameters.getString("description");
                    final var pictures = parameters.getJSONArray("pictures");
                    final var houseId = getNewHouseId(database);
                    final var images = storeHouseImages(houseId, pictures);

                    final var response = new JSONObject();
                    if (!parameters.isNull("id")) {
                        final var updateHouseId = parameters.getInt("id");
                        final var query = """
                            update House set
                              type = ?,
                              address = ?,
                              price = ?,
                              floor = ?,
                              elevator = ?,
                              balconies = ?,
                              terrace = ?,
                              garden = ?,
                              accessories = ?,
                              bedrooms = ?,
                              sqm = ?,
                              description = ?,
                              image0 = ?,
                              image1 = ?,
                              image2 = ?
                            where ROWID = ?
                        """;
                        final var values = Arrays.<Object>asList(
                            type,
                            address,
                            price,
                            floor,
                            elevator,
                            balconies,
                            terrace,
                            garden,
                            accessories,
                            bedrooms,
                            sqm,
                            description,
                            images.get(0),
                            images.get(1),
                            images.get(2),
                            updateHouseId
                        );
                        try {
                            database.executeUpdate(query, values);
                            response.put("response", new JSONObject().put("id", updateHouseId));
                        } catch (Exception e) {
                            response.put("response", JSONObject.NULL);
                        } finally {
                            clientManager.writeLine(response.toString());
                        }
                    } else {
                        final var query = """
                            insert into House values (
                              ?,
                              ?,
                              ?,
                              ?,
                              ?,
                              ?,
                              ?,
                              ?,
                              ?,
                              ?,
                              ?,
                              ?,
                              ?,
                              ?,
                              ?
                            )
                        """;
                        final var values = Arrays.<Object>asList(
                            type,
                            address,
                            price,
                            floor,
                            elevator,
                            balconies,
                            terrace,
                            garden,
                            accessories,
                            bedrooms,
                            sqm,
                            description,
                            images.get(0),
                            images.get(1),
                            images.get(2)
                        );
                        try {
                            database.executeUpdate(query, values);
                            response.put("response", new JSONObject().put("id", houseId));
                        } catch (Exception e) {
                            response.put("response", JSONObject.NULL);
                        } finally {
                            clientManager.writeLine(response.toString());
                        }
                    }
                    break;
                }
            }
        } catch (JSONException e) {
            clientManager.writeLine(clientManager.makeErrorMessage(e.getMessage()));
        }
        return true;
    }

    private static URI getHouseImagePath(int houseId, String name) {
        final var relativePath = String.format("src/main/resources/main/propertease/server/images/%d/%s", houseId, name);
        final var root = System.getProperty("user.dir");
        return Path.of(String.format("%s/%s", root, relativePath)).toUri();
    }

    private static Object getHouseImageAsBase64(int houseId, String name) {
        // TODO: ritorna immagine di default
        var path = Path.of(getHouseImagePath(houseId, name));
        if (!Files.exists(path)) {
            final var classPath = "/main/propertease/img/icons/placeholder.png";
            final var defaultResource = PosterClientStrategy.class.getResource(classPath);
            try {
                path = Path.of(Objects.requireNonNull(defaultResource).toURI());
            } catch (URISyntaxException e) {
                return JSONObject.NULL;
            }
        }
        try {
            final var bytes = Files.readAllBytes(path);
            final var encoded = Base64.getEncoder().encode(bytes);
            return new String(encoded);
        } catch (Exception e) {
            e.printStackTrace();
            return JSONObject.NULL;
        }
    }

    private static List<String> storeHouseImages(int houseId, JSONArray pictures) {
        final var images = new ArrayList<String>(pictures.length());
        for (var i = 0; i < pictures.length(); ++i) {
            if (pictures.isNull(i)) {
                images.add(null);
                continue;
            }
            final var decoded = Base64.getDecoder().decode(pictures.getString(i));
            final var name = String.format("%d.png", i);
            final var path = Path.of(getHouseImagePath(houseId, name));
            if (!Files.exists(path.getParent())) {
                try {
                    Files.createDirectories(path.getParent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Files.write(path, decoded, StandardOpenOption.CREATE);
                images.add(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return images;
    }

    private static int getNewHouseId(DatabaseConnection database) {
        final var id = new AtomicInteger(-1);
        database.executeQuery("select max(ROWID) from House;", null, (result) -> {
            try {
                id.set(result.getInt(1) + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return id.get();
    }

    private final AbstractClientManager clientManager;
}
