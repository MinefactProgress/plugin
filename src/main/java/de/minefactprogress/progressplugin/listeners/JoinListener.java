package de.minefactprogress.progressplugin.listeners;

import de.minefactprogress.progressplugin.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Main.getDistrictBossbar().addPlayer(e.getPlayer());
        e.getPlayer().sendMessage("welcome");
    }
}