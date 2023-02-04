package de.minefactprogress.progressplugin.api;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.District;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.utils.Constants;
import de.minefactprogress.progressplugin.utils.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class API {

    private static final int TIMEOUT = 10;
    private static final int INTERVAL = 60;

    private static final HttpClient client = HttpClient.newBuilder().build();

    @Getter
    private static List<District> districts = new ArrayList<>();
    @Getter
    private static List<Block> blocks = new ArrayList<>();
    @Getter
    private static List<User> users = new ArrayList<>();

    private static boolean loadedOnce = false;

    // -----===== Request Methods =====-----

    public static JsonElement GET(String path) {
        HttpRequest req = defaultRequest(path)
                .GET()
                .build();

        return sendRequest(req);
    }

    public static JsonElement POST(String path, JsonElement body) {
        HttpRequest req = defaultRequest(path)
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        return sendRequest(req);
    }

    public static JsonElement PUT(String path, JsonElement body) {
        HttpRequest req = defaultRequest(path)
                .PUT(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        return sendRequest(req);
    }

    public static JsonElement DELETE(String path) {
        HttpRequest req = defaultRequest(path)
                .DELETE()
                .build();

        return sendRequest(req);
    }

    // -----===== Progress Data =====-----

    public static void loadProgress() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            long time = System.currentTimeMillis();
            loadAll();

            if (!loadedOnce) {
                Logger.info(String.format("Successfully loaded %d Districts, %d Blocks and %d Users (%dms)",
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
        JsonArray json = GET(Routes.DISTRICTS).getAsJsonObject().get("data").getAsJsonArray();

        List<District> districtsNew = new ArrayList<>();
        for (JsonElement e : json) {
            districtsNew.add(new District(e.getAsJsonObject()));
        }

        districts = districtsNew;

        District.loadMissingParents(json);
    }

    public static void loadBlocks() {
        JsonArray json = GET(Routes.BLOCKS).getAsJsonObject().get("data").getAsJsonArray();

        List<Block> blocksNew = new ArrayList<>();
        for (JsonElement e : json) {
            blocksNew.add(new Block(e.getAsJsonObject()));
        }

        blocks = blocksNew;
    }

    public static void loadUsers() {
        JsonArray json = GET(Routes.USERS).getAsJsonObject().get("data").getAsJsonArray();

        Type userType = new TypeToken<ArrayList<User>>(){}.getType();
        users = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create().fromJson(json, userType);
    }

    // -----===== Helper Methods =====-----

    private static JsonElement sendRequest(HttpRequest req) {
        try {
            HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
            return JsonParser.parseString(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return new JsonPrimitive("");
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
