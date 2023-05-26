package de.minefactprogress.progressplugin.utils;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WorldEditUtils {

    public static LocalSession getLocalSession(Player player) {
        LocalSession session;

        try {
            session = WorldEdit.getInstance().getSessionManager().findByName(player.getName());
            if (session == null) {
                player.sendMessage(Constants.PREFIX_WORLDEDIT + ChatColor.RED + "There was an error loading your WorldEdit Session. Please try again");
                return null;
            }
        } catch (NullPointerException | IncompleteRegionException ex) {
            player.sendMessage(Constants.PREFIX_WORLDEDIT + ChatColor.RED + "Please select a WorldEdit selection!");
            return null;
        }
        return session;
    }
}
