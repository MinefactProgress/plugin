package de.minefactprogress.progressplugin.api;

import com.google.gson.*;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.entities.users.User;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

public class RequestHandler {

    @Getter
    private static final RequestHandler instance;

    private final int TIMEOUT = 10000;
    private final int INTERVAL = 60;
    private final String BASE_URL = "https://progressbackend.minefact.de/";
    private final String ROOT_KEY = "e9299168-9a87-4a44-801b-4214449e46be";

    private long lastUpdatedDistricts = 0;

    static {
        instance = new RequestHandler();
    }

    private RequestHandler() {}

    public void startSchedulers() {
        if(User.users.isEmpty()) {
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), this::requestUsers);
        }
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), this::requestDistricts, 0, INTERVAL*20);
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), this::requestBlocks, 0, INTERVAL*20);
    }

    public void requestUsers() {
        String jsonString = GET("api/minecraft/users/get");

        if(jsonString == null) return;

        JsonElement jsonElement;
        try {
            jsonElement = JsonParser.parseString(jsonString);
        } catch (JsonSyntaxException e) {
            Bukkit.getLogger().warning("Could not parse users API response to JSON!");
            return;
        }

        if(!jsonElement.isJsonArray()) return;

        JsonArray jsonArray = jsonElement.getAsJsonArray();

        User.users.clear();
        for(JsonElement e : jsonArray) {
            User.users.add(new User(e.getAsJsonObject()));
        }
    }

    public void requestDistricts() {

    }

    public void requestBlocks() {

    }

    public String GET(String path) {
        try {
            HttpURLConnection con = createConnection(BASE_URL + path, "GET");

            if(con.getResponseCode() > 299) {
                Bukkit.getLogger().warning("API Request returned response code " + con.getResponseCode() + " for URL: " + this.BASE_URL + path);
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            return content.toString();
        } catch (SocketTimeoutException e) {
            Bukkit.getLogger().warning("Read timed out! Is the API offline?");
        } catch (ConnectException e) {
            Bukkit.getLogger().warning("Could not connect to the API! Is it offline?");
        }  catch (IOException e) {
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

            if(con.getResponseCode() > 299) {
                Bukkit.getLogger().warning("API Request returned response code " + con.getResponseCode() + " for URL: " + this.BASE_URL + path);
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            con.getInputStream().close();
            in.close();
            con.disconnect();

            return JsonParser.parseString(content.toString()).getAsJsonObject();
        } catch (SocketTimeoutException e) {
            Bukkit.getLogger().warning("Read timed out! Is the API offline?");
        } catch (ConnectException e) {
            Bukkit.getLogger().warning("Could not connect to the API! Is it offline?");
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
        if(method.equalsIgnoreCase("post")) {
            con.setDoOutput(true);
        }
        con.setConnectTimeout(TIMEOUT);
        con.setReadTimeout(TIMEOUT);

        return con;
    }
}
