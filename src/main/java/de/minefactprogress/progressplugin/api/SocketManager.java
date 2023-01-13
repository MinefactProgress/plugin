package de.minefactprogress.progressplugin.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.entities.users.Rank;
import de.minefactprogress.progressplugin.utils.Logger;
import de.minefactprogress.progressplugin.utils.Reflections;
import de.minefactprogress.progressplugin.utils.conversion.CoordinateConversion;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SocketManager {

    private static final int INTERVAL = 10;

    private final Socket socket;

    public SocketManager(String ip) {
        Socket s;
        try {
            s = IO.socket(ip, IO.Options.builder().build());
            s.connect();
            Logger.info("Successfully connected to socket");
        } catch (URISyntaxException e) {
            s = null;
            Logger.error("Error occurred while connecting to socket: " + ip);
        }
        this.socket = s;
        joinRoom("nyc_server");
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
        this.socket.disconnect();
    }

}
