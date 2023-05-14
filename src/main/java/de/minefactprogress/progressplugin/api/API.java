package de.minefactprogress.progressplugin.api;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.District;
import de.minefactprogress.progressplugin.entities.users.SettingType;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.utils.Constants;
import de.minefactprogress.progressplugin.utils.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class API {

    private static final int TIMEOUT = 20;

    private static final HttpClient client = HttpClient.newBuilder().build();
    @Getter
    private static List<District> districts = new ArrayList<>();
    @Getter
    private static List<Block> blocks = new ArrayList<>();
    @Getter
    private static List<User> users = new ArrayList<>();

    private static boolean loadedOnce = false;

    // -----===== Request Methods =====-----

    public static JsonObject GET(String path) {
        HttpRequest req = defaultRequest(path)
                .GET()
                .build();

        return sendRequest(req);
    }

    public static String POST(String path, JsonElement body, Consumer<JsonObject> onSuccess) {
        return POST(path, body, onSuccess, null);
    }

    public static String POST(String path, JsonElement body, Consumer<JsonObject> onSuccess, Player p) {
        HttpRequest req = defaultRequest(path)
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        JsonObject res = sendRequest(req);
        return handleResponse(res, onSuccess, p);
    }

    public static String PUT(String path, JsonElement body, Consumer<JsonObject> onSuccess, Player p) {
        HttpRequest req = defaultRequest(path)
                .PUT(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        JsonObject res = sendRequest(req);
        return handleResponse(res, onSuccess, p);
    }

    public static JsonObject DELETE(String path) {
        HttpRequest req = defaultRequest(path)
                .DELETE()
                .build();

        return sendRequest(req);
    }

    private static String handleResponse(JsonObject res, Consumer<JsonObject> onSuccess, Player player) {
        String errorMessage = res == null || res.get("error") == null ? "An unknown error occurred" : res.get("error").getAsString();
        if(onSuccess != null && res != null && res.get("error") == null) {
            onSuccess.accept(res);
        } else {
            if(player != null) {
                player.sendMessage(Constants.PREFIX + ChatColor.RED + errorMessage);
            }
        }
        return errorMessage;
    }

    // -----===== Progress Data =====-----

    public static void loadProgress() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            long time = System.currentTimeMillis();
            loadAll();

            if (!loadedOnce) {
                // Register district bossbars
                for(Player p : Bukkit.getOnlinePlayers()) {
                    User user = User.getUserByUUID(p.getUniqueId());
                    if(user == null || user.getSetting(SettingType.MINECRAFT_DISTRICT_BAR).equals("true")) {
                        if(!Main.getDistrictBossbar().getBars().containsKey(p.getUniqueId())) {
                            Main.getDistrictBossbar().addPlayer(p);
                        }
                        Main.getDistrictBossbar().updatePlayer(p);
                    }
                }

                Logger.info(String.format("Loaded %d Districts, %d Blocks and %d Users (%dms)",
                        districts.size(),
                        blocks.size(),
                        users.size(),
                        System.currentTimeMillis() - time));
                loadedOnce = true;
            }
        });
    }

    public static void loadAll() {
        loadDistricts();
        loadBlocks();
        loadUsers();
    }

    public static void loadDistricts() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .registerTypeAdapter(Point2D.Double.class, (JsonDeserializer<Point2D.Double>) (jsonElement, type, jsonDeserializationContext) -> {
                    JsonArray array = jsonElement.getAsJsonArray();
                    return new Point2D.Double(array.get(0).getAsDouble(), array.get(1).getAsDouble());
                })
                .create();
        JsonObject res = GET(Routes.DISTRICTS);

        if(res == null) return;

        JsonArray json = res.get("data").getAsJsonArray();

        Type districtType = new TypeToken<ArrayList<District>>(){}.getType();
        districts = gson.fromJson(json, districtType);
    }

    public static void loadBlocks() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, (JsonDeserializer<Integer>) (jsonElement, type, jsonDeserializationContext) -> {
                    if(jsonElement instanceof JsonObject json && json.get("id") != null) {
                        return json.get("id").getAsInt();
                    }
                    return jsonElement.getAsInt();
                })
                .registerTypeAdapter(Point2D.Double.class, (JsonDeserializer<Point2D.Double>) (jsonElement, type, jsonDeserializationContext) -> {
                    JsonArray array = jsonElement.getAsJsonArray();
                    return new Point2D.Double(array.get(0).getAsDouble(), array.get(1).getAsDouble());
                })
                .create();
        JsonObject res = GET(Routes.BLOCKS);

        if(res == null) return;

        JsonArray json = res.get("data").getAsJsonArray();

        Type blockType = new TypeToken<ArrayList<Block>>(){}.getType();
        blocks = gson.fromJson(json, blockType);
    }

    public static void loadUsers() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(UUID.class, (JsonDeserializer<Object>) (jsonElement, type, jsonDeserializationContext) -> UUID.fromString(jsonElement.getAsString()))
                .create();
        JsonObject res = GET(Routes.USERS);

        if(res == null) return;

        JsonArray json = res.get("data").getAsJsonArray();

        Type userType = new TypeToken<ArrayList<User>>(){}.getType();
        users = gson.fromJson(json, userType);
    }

    // -----===== Helper Methods =====-----

    private static JsonObject sendRequest(HttpRequest req) {
        try {
            HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
            return JsonParser.parseString(response.body()).getAsJsonObject();
        } catch (ConnectException e) {
            try {
                int port = req.uri().getPort();
                String url = req.uri().toURL().getProtocol() + "://" + req.uri().getHost() + (port == -1 ? "" : ":" + req.uri().getPort()) + "/";
                Logger.error("Could not establish a connection to " + url);
            } catch (MalformedURLException ex) {
                Logger.error("Unknown error occurred while sending the request (" + req.method() + " " + req.uri() +  ")");
            }
        } catch (IOException | InterruptedException e) {
            Logger.error("Unknown error occurred while sending the request (" + req.method() + " " + req.uri() +  ")");
        }
        return null;
    }

    private static HttpRequest.Builder defaultRequest(String path) {
        try {
            return HttpRequest.newBuilder()
                    .uri(new URI(Constants.API_URL + path))
                    .header("Authorization", "Bearer " + Constants.API_TOKEN)
                    .header("Content-Type", "application/json")
                    .version(HttpClient.Version.HTTP_1_1)
                    .timeout(Duration.of(TIMEOUT, ChronoUnit.SECONDS));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return HttpRequest.newBuilder();
    }
}
