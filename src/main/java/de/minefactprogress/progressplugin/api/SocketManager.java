package de.minefactprogress.progressplugin.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.District;
import de.minefactprogress.progressplugin.entities.users.Rank;
import de.minefactprogress.progressplugin.entities.users.SettingType;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.utils.Constants;
import de.minefactprogress.progressplugin.utils.Logger;
import de.minefactprogress.progressplugin.utils.MessageHandler;
import de.minefactprogress.progressplugin.utils.Reflections;
import de.minefactprogress.progressplugin.utils.conversion.CoordinateConversion;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
        try {
            socket.disconnect();    // If previous connection did not close properly
            socket.connect();
            Logger.info("Successfully connected to socket: " + Constants.BASE_URL);
        } catch (Exception e) {
            Logger.error("Error occurred while connecting to socket: " + Constants.BASE_URL);
            return;
        }

        // Join socket rooms
        joinRoom("nyc_server");
        joinRoom("block_updates");

        // Listen to events
        listenEvent("block_updates", (Object... args) -> {
            for(Object obj : args) {
                JsonObject json = (JsonObject) JsonParser.parseString(obj.toString());
                JsonObject blockJson = json.get("block").getAsJsonObject();

                District district = District.getDistrictById(blockJson.get("district").getAsJsonObject().get("id").getAsInt());
                Block block = Block.getBlock(district, blockJson.get("id").getAsInt());
                User user = User.getUserByName(json.get("user").getAsJsonObject().get("username").getAsString());

                if(block == null) return;

                if (user != null && !user.getUsername().equals("root")) {
                    MessageHandler.sendToAllPlayers(ChatColor.YELLOW + block.toString() + ChatColor.GRAY + " updated by " + user);
                }

                // TODO: update blocks directly instead of requesting again
                Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), API::loadAll);
            }
        });
        listenEvent("playerTeleport", (Object... args) -> {
                JsonObject json = (JsonObject) JsonParser.parseString(args[0].toString());
                if(!(json.get("user")!=null&&json.get("coordinates")!=null&&(json.get("coordinates").getAsJsonArray().size()>=2))) return;
                double[] coords = CoordinateConversion.convertFromGeo(json.get("coordinates").getAsJsonArray().get(0).getAsDouble(),json.get("coordinates").getAsJsonArray().get(1).getAsDouble());
                User user = User.getUserById(Integer.parseInt(json.get("user").toString()));
                if(user == null) return;
                Player player = user.getPlayer();
                Location loc = player.getWorld().getHighestBlockAt((int)coords[0],(int)coords[1]).getLocation();
                loc.setY(loc.getY()+1);
            Bukkit.getScheduler().runTask(Main.getInstance(), () ->
                    player.teleport(loc));
            player.sendMessage(Main.getPREFIX()+"Teleported to "+ChatColor.GOLD+coords[0]+", "+coords[1]);
        });
    }

    public boolean isConnected() {
        return socket.connected();
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
                User user = User.getUserByUUID(p.getUniqueId());

                if(user != null && user.getSetting(SettingType.MINECRAFT_MAP_VISIBLE).equals("false")) continue;

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
