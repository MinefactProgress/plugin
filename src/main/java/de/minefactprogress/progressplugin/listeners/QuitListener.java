package de.minefactprogress.progressplugin.listeners;

import com.google.gson.JsonObject;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.components.LocationEditor;
import de.minefactprogress.progressplugin.entities.users.Rank;
import de.minefactprogress.progressplugin.entities.users.SettingType;
import de.minefactprogress.progressplugin.entities.users.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();


        // Send socket message
        if(User.getUserByUUID(p.getUniqueId()).getSetting(SettingType.MINECRAFT_MAP_VISIBLE).equals("true")) {
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                JsonObject playerJson = new JsonObject();
                playerJson.addProperty("username", p.getName());
                playerJson.addProperty("uuid", p.getUniqueId().toString());
                playerJson.addProperty("rank", Rank.getByPermission(p).getName());
                Main.getSocketManager().sendMessage("playerLeave", playerJson);
            });
        }

        LocationEditor locationEditor = LocationEditor.get(p);
        if(locationEditor != null) {
            locationEditor.destroy();
        }
    }
}
