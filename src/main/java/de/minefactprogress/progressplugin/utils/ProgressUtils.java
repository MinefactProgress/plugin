package de.minefactprogress.progressplugin.utils;

import org.bukkit.ChatColor;

public class ProgressUtils {

    public static ChatColor progressToColor(double progress) {
        if (progress >= 100) return ChatColor.GREEN;
        else if (progress >= 80) return ChatColor.YELLOW;
        else if (progress >= 30) return ChatColor.GOLD;
        else return ChatColor.RED;
    }
}
