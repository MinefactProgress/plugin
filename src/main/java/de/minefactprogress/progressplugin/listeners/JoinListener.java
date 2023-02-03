package de.minefactprogress.progressplugin.listeners;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.entities.users.Rank;
import de.minefactprogress.progressplugin.entities.users.User2;
import de.minefactprogress.progressplugin.utils.CustomColors;
import de.minefactprogress.progressplugin.utils.Item;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class JoinListener implements Listener {

    @SneakyThrows
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        User2 user = User2.getUserByUUID(p.getUniqueId());
        Rank rank = Rank.getByPermission(p);

        // Add progress item
        ArrayList<String> progressLore = new ArrayList<>();
        progressLore.add(ChatColor.GRAY + "Check out the progress of New York City.");
        p.getInventory().setItem(4, new Item(Material.EMERALD).setDisplayName(CustomColors.BLUE.getChatColor() + "Progress").setLore(progressLore).build());

        // Register district bossbar
        if(Main.getDistrictBossbar().getBars().containsKey(p.getUniqueId())) {
            Main.getDistrictBossbar().updatePlayer(p);
        }

        // TODO
        if(user != null) {
            if(!user.getUsername().equals(p.getName())) {
                // Name changed
            }
            if(!user.getRank().equals(rank)) {
                // Rank changed
                Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
//                    user.setRank(rank);
//
//                    JsonObject json = new JsonObject();
//                    json.addProperty("rank", rank.getName());
//
//                    JsonElement res = API.PUT(Routes.USERS + "/" + user.getUid(), json);
//                    Logger.info(new Gson().toJsonTree(user).toString());
//                    Logger.info(res.toString());
//                    Logger.info("Rank of " + p.getName() + " successfully changed to " + rank.getName());
                });
            }
        }
    }
}
