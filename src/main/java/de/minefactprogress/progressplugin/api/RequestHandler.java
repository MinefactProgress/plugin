package de.minefactprogress.progressplugin.api;

import com.google.gson.*;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.District;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.utils.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

@Getter
public class RequestHandler {

    @Getter
    private static final RequestHandler instance;

    static {
        instance = new RequestHandler();
    }

    private final int TIMEOUT = 10000;
    private final int INTERVAL = 60;
    private final String BASE_URL = "https://progressbackend.minefact.de/";
    private final String ROOT_KEY = "e9299168-9a87-4a44-801b-4214449e46be";
    private boolean reloading = false;
    private long lastUpdated = 0;

    private RequestHandler() {
    }

    public void startSchedulers() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
            reloading = true;
            if(lastUpdated == 0) {
                Logger.info("Loading progress data...");
            }

            if (User.users.isEmpty()) {
                requestUsers();
            }
            requestDistricts();
            requestBlocks();

            if(lastUpdated == 0) {
                Logger.info("Successfully loaded " + User.users.size() + " users, " + District.districts.size() + " districts and " + Block.blocks.size() + " blocks");
            }

            lastUpdated = System.currentTimeMillis();
            reloading = false;
        }, 0, INTERVAL * 20);
    }

    public void requestUsers() {
        String jsonString = GET("api/minecraft/users/get");

        if (jsonString == null) return;

        JsonElement jsonElement;
        try {
            jsonElement = JsonParser.parseString(jsonString);
        } catch (JsonSyntaxException e) {
            Logger.warn("Could not parse users API response to JSON!");
            return;
        }

        if (!jsonElement.isJsonArray()) return;

        JsonArray jsonArray = jsonElement.getAsJsonArray();

        User.users.clear();
        for (JsonElement e : jsonArray) {
            User.users.add(new User(e.getAsJsonObject()));
        }
    }

    public void requestDistricts() {
        if (!District.districts.isEmpty() && Bukkit.getOnlinePlayers().size() == 0) return;

        String jsonString = GET("api/districts/get");

        if (jsonString == null) return;

        JsonElement jsonElement;
        try {
            jsonElement = JsonParser.parseString(jsonString);
        } catch (JsonSyntaxException e) {
            Logger.warn("Could not parse districts API response to JSON!");
            return;
        }

        if (!jsonElement.isJsonArray()) return;

        JsonArray jsonArray = jsonElement.getAsJsonArray();

        District.districts.clear();
        for (JsonElement e : jsonArray) {
            District.districts.add(new District(e.getAsJsonObject()));
        }
        District.loadMissingParents(jsonArray);
    }

    public void requestBlocks() {
        if (!Block.blocks.isEmpty() && Bukkit.getOnlinePlayers().size() == 0) return;

        String jsonString = GET("api/blocks/get");

        if (jsonString == null) return;

        JsonElement jsonElement;
        try {
            jsonElement = JsonParser.parseString(jsonString);
        } catch (JsonSyntaxException e) {
            Logger.warn("Could not parse blocks API response to JSON!");
            return;
        }

        if (!jsonElement.isJsonArray()) return;

        JsonArray jsonArray = jsonElement.getAsJsonArray();

        Block.blocks.clear();
        for (JsonElement e : jsonArray) {
            Block.blocks.add(new Block(e.getAsJsonObject()));
        }
    }

    public JsonObject createJsonObject(Block block) {
        JsonObject json = new JsonObject();
        json.addProperty("key", ROOT_KEY);
        json.addProperty("district", block.getDistrict().getName());
        json.addProperty("blockID", block.getId());
        json.add("values", new JsonObject());
        return json;
    }

    public String GET(String path) {
        try {
            HttpURLConnection con = createConnection(BASE_URL + path, "GET");

            if (con.getResponseCode() > 299) {
                Logger.warn("API Request returned response code " + con.getResponseCode() + " for URL: " + this.BASE_URL + path);
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            return content.toString();
        } catch (SocketTimeoutException e) {
            Logger.warn("Read timed out! Is the API offline?");
        } catch (ConnectException e) {
            Logger.warn("Could not connect to the API! Is it offline?");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JsonObject POST(String path, JsonObject body) {
        try {
            HttpURLConnection con = createConnection(this.BASE_URL + path + "?key=" + ROOT_KEY, "POST");

            OutputStream out = con.getOutputStream();
            out.write(body.toString().getBytes());
            out.flush();
            out.close();

            if (con.getResponseCode() > 299) {
                Logger.warn("API Request returned response code " + con.getResponseCode() + " for URL: " + this.BASE_URL + path);
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            con.getInputStream().close();
            in.close();
            con.disconnect();

            return JsonParser.parseString(content.toString()).getAsJsonObject();
        } catch (SocketTimeoutException e) {
            Logger.warn("Read timed out! Is the API offline?");
        } catch (ConnectException e) {
            Logger.warn("Could not connect to the API! Is it offline?");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpURLConnection createConnection(String urlString, String method) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        con.addRequestProperty("Content-Type", "application/json");
        con.addRequestProperty("User-Agent", "MineFact-NewYork-Progress");
        if (method.equalsIgnoreCase("post")) {
            con.setDoOutput(true);
        }
        con.setConnectTimeout(TIMEOUT);
        con.setReadTimeout(TIMEOUT);

        return con;
    }
}
