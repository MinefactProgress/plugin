package de.minefactprogress.progressplugin.utils;

import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.entities.users.SettingType;
import de.minefactprogress.progressplugin.entities.users.User;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageHandler {

    public static void sendToAllPlayers(String msg) {
        for (Player all : Bukkit.getOnlinePlayers()) {
            all.sendMessage(Component.text(Constants.PREFIX + msg));
        }
    }

    public static void sendMessageToDebuggers(String msg) {
        for (User user : API.getUsers()) {
            if(user.getSetting(SettingType.MINECRAFT_DEBUG_MODE).equals("true")) {
                Player p = Bukkit.getPlayer(user.getUuid());

                if(p != null) {
                    p.sendMessage(Component.text(Constants.PREFIX_DEBUG + msg));
                }
            }
        }
    }
}
