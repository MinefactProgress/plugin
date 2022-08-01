package de.minefactprogress.progressplugin.utils;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.District;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ProgressUtils {

    public static ChatColor progressToColor(double progress) {
        if (progress >= 100) return ChatColor.GREEN;
        else if (progress >= 80) return ChatColor.YELLOW;
        else if (progress >= 30) return ChatColor.GOLD;
        else return ChatColor.RED;
    }

    public static boolean checkForData(Player p) {
        if(District.districts.isEmpty() || Block.blocks.isEmpty()) {
            p.sendMessage(Main.getPREFIX() + ChatColor.RED + "Couldn't load data. Please try again later!");
            return false;
        }
        return true;
    }

    public static boolean checkStaffPermission(Player p) {
        if(!Permissions.isTeamMember(p)) {
            p.sendMessage(Main.getPREFIX() + ChatColor.RED + "You don't have permission to execute this command!");
            return false;
        }
        return true;
    }
}
