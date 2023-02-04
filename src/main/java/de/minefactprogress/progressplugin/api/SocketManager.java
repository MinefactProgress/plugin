package de.minefactprogress.progressplugin.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.entities.users.Rank;
import de.minefactprogress.progressplugin.utils.Constants;
import de.minefactprogress.progressplugin.utils.Logger;
import de.minefactprogress.progressplugin.utils.MessageHandler;
import de.minefactprogress.progressplugin.utils.Reflections;
import de.minefactprogress.progressplugin.utils.conversion.CoordinateConversion;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SocketManager {

    private static final int INTERVAL = 10;

    private final Socket socket;

    public SocketManager() {
        socket = IO.socket(URI.create(Constants.BASE_URL), IO.Options.builder().setAuth(Collections.singletonMap("token", Constants.API_TOKEN)).build());
        socket.disconnect();    // If previous connection did not close properly
        socket.connect();
        Logger.info("Successfully connected to socket: " + Constants.BASE_URL);

        // Join socket rooms
        joinRoom("nyc_server");
        joinRoom("block_updates");

        // Listen to events
        listenEvent("block_updates", (Object... args) -> {
            for(Object obj : args) {
                JsonObject json = (JsonObject) JsonParser.parseString(obj.toString());
                JsonObject block = json.get("block").getAsJsonObject();

                MessageHandler.sendToAllPlayers(
                        block.get("district").getAsJsonObject().get("name").getAsString()
                                + " #" + block.get("id").getAsInt() + " updated by "
                                + json.get("user").getAsJsonObject().get("username").getAsString()
                );

                // TODO: update blocks directly instead of requesting again
                Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), API::loadAll);
            }
        });
    }

    public void sendMessage(String event, Object... message) {
        socket.emit(event, message);
    }

    public void joinRoom(String room) {
        JsonObject roomJson = new JsonObject();
        roomJson.addProperty("room", room);

        JsonObject json = new JsonObject();
        json.add("data", roomJson);
        sendMessage("join", json);
    }

    public void listenEvent(String event, Consumer<Object[]> func) {
        socket.on(event, func::accept);
    }

    public void startSchedulers() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
            if(Bukkit.getOnlinePlayers().isEmpty()) return;

            JsonArray jsonArray = new JsonArray();
            for(Player p : Bukkit.getOnlinePlayers()) {
                JsonObject json = new JsonObject();
                json.addProperty("username", p.getName());
                json.addProperty("uuid", p.getUniqueId().toString());
                json.addProperty("rank", Rank.getByPermission(p).getName());
                json.addProperty("location", Reflections.convertLocationToString(p.getLocation(), false, false));
                json.addProperty("latlon", Arrays.stream(CoordinateConversion.convertToGeo(p.getLocation().getX(), p.getLocation().getZ()))
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(",")));

                jsonArray.add(json);
            }
            sendMessage("players", jsonArray);
        }, 0L, 20L * INTERVAL);
    }

    public void disconnect() {
        socket.disconnect();
        Logger.info("Successfully disconnected from socket: " + Constants.BASE_URL);
    }

}
