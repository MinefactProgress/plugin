package de.minefactprogress.progressplugin.listeners;

import com.google.gson.JsonObject;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.api.Routes;
import de.minefactprogress.progressplugin.entities.users.Rank;
import de.minefactprogress.progressplugin.entities.users.SettingType;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.utils.*;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class JoinListener implements Listener {

    @SneakyThrows
    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        User user = User.getUserByUUID(p.getUniqueId());
        Rank rank = Rank.getByPermission(p);

        // Add progress item
        ArrayList<String> progressLore = new ArrayList<>();
        progressLore.add(ChatColor.GRAY + "Check out the progress of New York City.");
        p.getInventory().setItem(4, new Item(Material.EMERALD).setDisplayName(CustomColors.BLUE.getChatColor() + "Progress").setLore(progressLore).build());

        // Send socket message
        if(Main.getInstance().isProductionMode() && (user == null || user.getSetting(SettingType.MINECRAFT_MAP_VISIBLE).equals("true"))) {
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                JsonObject playerJson = new JsonObject();
                playerJson.addProperty("username", p.getName());
                playerJson.addProperty("uuid", p.getUniqueId().toString());
                playerJson.addProperty("rank", Rank.getByPermission(p).getName());
                Main.getSocketManager().sendMessage("nyc_server_player_join", playerJson);
            });
        }
        // Register district bossbar
        if(user == null || user.getSetting(SettingType.MINECRAFT_DISTRICT_BAR).equals("true")) {
            if(!Main.getDistrictBossbar().getBars().containsKey(p.getUniqueId())) {
                Main.getDistrictBossbar().addPlayer(p);
            }
            Main.getDistrictBossbar().updatePlayer(p);
        }

        if(user != null) {
            if(Main.getInstance().isProductionMode()) {
                if(!user.getUsername().equals(p.getName())) {
                    // Name changed
                /*Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                    String oldName = user.getUsername();
                    user.setUsername(p.getName());

                    JsonObject json = new JsonObject();
                    json.addProperty("username", p.getName());

                    API.PUT(Routes.USERS + "/" + user.getUid(), json, res -> {
                        Logger.info("Username of " + oldName + " successfully changed to " + p.getName());
                    }, p);
                });*/
                }
                if(!user.getRank().equals(rank)) {
                    // Rank changed
                    Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                        user.setRank(rank);

                        JsonObject json = new JsonObject();
                        json.addProperty("rank", rank.getName());

                        API.PUT(Routes.USERS + "/" + user.getUid(), json,res -> {
                            Logger.info("Rank of " + p.getName() + " successfully changed to " + rank.getName());
                        }, p);
                    });
                }
            }
            if(Permissions.isTeamMember(p)) {
                p.sendMessage(Main.getPREFIX()+"Logged in as "+user.getUsername());
            }
        } else {
            if(Permissions.isTeamMember(p)) {
                p.sendMessage(Main.getPREFIX()+"Please link your account to your website account! https://progress.minefact.de/account");
            }
        }
    }
}
