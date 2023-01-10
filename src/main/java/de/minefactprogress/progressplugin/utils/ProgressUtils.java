package de.minefactprogress.progressplugin.utils;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressUtils {

    public static ChatColor progressToColor(double progress) {
        if (progress >= 100) return ChatColor.GREEN;
        else if (progress >= 80) return ChatColor.YELLOW;
        else if (progress >= 30) return ChatColor.GOLD;
        else return ChatColor.RED;
    }

    public static String generateProgressbar(double progress) {
        ChatColor color = progressToColor(progress);
        int numberOfElements = (int) ((progress / 100) * 20);
        return color + ChatColor.STRIKETHROUGH.toString() + " ".repeat(Math.max(0, numberOfElements)) +
                ChatColor.WHITE + ChatColor.STRIKETHROUGH +
                " ".repeat(Math.max(0, 20 - numberOfElements)) +
                color + " " + progress + "%";
    }

    public static boolean checkForData(Player p) {
        if(API.getDistricts().isEmpty() || API.getBlocks().isEmpty()) {
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
