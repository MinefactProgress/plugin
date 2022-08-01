package de.minefactprogress.progressplugin.utils;

import com.google.gson.JsonObject;
import de.minefactprogress.progressplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger {

    public static void info(String message) {
        Bukkit.getConsoleSender().sendMessage(Main.getPREFIX() + message);
    }

    public static void warn(String message) {
        Bukkit.getLogger().warning(Main.getPREFIX() + ChatColor.stripColor(message));
    }

    public static void error(String message) {
        Bukkit.getConsoleSender().sendMessage(Main.getPREFIX() + message);
    }

    public static void error(String message, JsonObject res) {
        if(res.get("message") == null) {
            error(message);
            return;
        }
        Bukkit.getConsoleSender().sendMessage(Main.getPREFIX() + message + " §c(" + res.get("message").getAsString() + ")");
    }
}
