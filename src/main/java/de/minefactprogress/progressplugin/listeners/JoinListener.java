package de.minefactprogress.progressplugin.listeners;

import com.google.gson.JsonObject;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.RequestHandler;
import de.minefactprogress.progressplugin.entities.users.Rank;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.utils.Logger;
import de.minefactprogress.progressplugin.utils.conversion.CoordinateConversion;
import de.minefactprogress.progressplugin.utils.conversion.projection.OutOfProjectionBoundsException;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @SneakyThrows
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        User user = User.getByUUID(p.getUniqueId());
        Rank rank = Rank.getByPermission(p);

        // Register user
        if(user == null) {
            User.register(p);
        } else {
            if(!user.getName().equals(p.getName())) {
                // Name changed
                JsonObject json = new JsonObject();
                json.addProperty("uuid", p.getUniqueId().toString());
                json.addProperty("type", "name");
                json.addProperty("value", p.getName());

                Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                    JsonObject res = RequestHandler.getInstance().POST("api/minecraft/users/set", json);
                    if(!res.get("error").getAsBoolean()) {
                        String oldName = user.getName();

                        User.users.remove(user);
                        User.users.add(new User(p.getUniqueId(), p.getName(), user.getRank()));

                        Logger.info("Name of " + oldName + " successfully changed to " + p.getName());
                    } else {
                        Logger.error("§cAn error occurred while renaming " + p.getName(), json);
                    }
                });
            }
            if(!user.getRank().equals(rank)) {
                // Rank changed
                JsonObject json = new JsonObject();
                json.addProperty("uuid", p.getUniqueId().toString());
                json.addProperty("type", "rank");
                json.addProperty("value", rank != null ? rank.getName() : "Player");

                Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                    JsonObject res = RequestHandler.getInstance().POST("api/minecraft/users/set", json);
                    if(!res.get("error").getAsBoolean()) {
                        String oldRank = user.getRank().getName();

                        User.users.remove(user);
                        User.users.add(new User(p.getUniqueId(), p.getName(), rank));

                        Logger.info("Rank of " + p.getName() + " successfully changed to " + (rank != null ? rank.getName() : "Player"));
                    } else {
                        Logger.error("§cAn error occurred while changing the rank of " + p.getName(), json);
                    }
                });
            }
        }

        Main.getDistrictBossbar().addPlayer(p);
        p.sendMessage("welcome");
        try {
            double[] pos = CoordinateConversion.convertFromGeo(40.740699535030515,-73.98937085087103);
            p.sendMessage(pos[0]+","+pos[1]);
        } catch (OutOfProjectionBoundsException outOfProjectionBoundsException) {
            outOfProjectionBoundsException.printStackTrace();
        }
    }
}
