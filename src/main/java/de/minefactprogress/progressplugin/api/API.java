package de.minefactprogress.progressplugin.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.District;
import de.minefactprogress.progressplugin.entities.users.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;

import java.io.IOException;
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
    private static final String BASE_URL = "https://progressbackend.minefact.de/v1";

    private static final HttpClient client = HttpClient.newBuilder().build();

    @Getter
    private static List<District> districts = new ArrayList<>();
    @Getter
    private static List<Block> blocks = new ArrayList<>();
    @Getter
    private static List<User> users = new ArrayList<>();

    // -----===== Request Methods =====-----

    public static JsonElement GET(String path) {
        HttpRequest req = defaultRequest(path)
                .GET()
                .build();

        return sendRequest(req);
    }

    public static JsonElement POST(String path, JsonElement body) {
        HttpRequest req = defaultRequest(path)
                .POST(HttpRequest.BodyPublishers.ofString(body.getAsString()))
                .build();

        return sendRequest(req);
    }

    public static JsonElement PUT(String path, JsonElement body) {
        HttpRequest req = defaultRequest(path)
                .PUT(HttpRequest.BodyPublishers.ofString(body.getAsString()))
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
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
            loadDistricts();
            loadBlocks();

            if (users.isEmpty()) {
                loadUsers();
            }
        }, 0L, INTERVAL * 20L);
    }

    private static void loadDistricts() {
        JsonArray json = GET(Routes.DISTRICTS).getAsJsonObject().get("data").getAsJsonArray();

        List<District> districtsNew = new ArrayList<>();
        for (JsonElement e : json) {
            districtsNew.add(new District(e.getAsJsonObject()));
        }

        districts = districtsNew;

        District.loadMissingParents(json);
    }

    private static void loadBlocks() {
        JsonArray json = GET(Routes.BLOCKS).getAsJsonObject().get("data").getAsJsonArray();

        List<Block> blocksNew = new ArrayList<>();
        for (JsonElement e : json) {
            blocksNew.add(new Block(e.getAsJsonObject()));
        }

        blocks = blocksNew;
    }

    private static void loadUsers() {
        JsonArray json = GET(Routes.USERS).getAsJsonObject().get("data").getAsJsonArray();

        List<User> usersNew = new ArrayList<>();
        for (JsonElement e : json) {
            if(e.getAsJsonObject().get("minecraft").isJsonNull()) continue;
            usersNew.add(new User(e.getAsJsonObject()));
        }

        users = usersNew;
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
                    .uri(new URI(BASE_URL + path))
                    .timeout(Duration.of(TIMEOUT, ChronoUnit.SECONDS));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return HttpRequest.newBuilder();
    }
}
