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
        StringBuilder sb = new StringBuilder(color + ChatColor.STRIKETHROUGH.toString());
        double numberOfElements = (progress / 100) * 20;
        for(int i = 0; i < numberOfElements; i++) {
            sb.append(" ");
        }
        sb.append(ChatColor.WHITE).append(ChatColor.STRIKETHROUGH);
        for(int i = 0; i < 20 - numberOfElements; i++) {
            sb.append(" ");
        }
        return sb.append(color).append(" ").append(progress).append("%").toString();
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
