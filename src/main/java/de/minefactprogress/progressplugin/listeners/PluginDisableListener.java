package de.minefactprogress.progressplugin.listeners;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

public class PluginDisableListener implements Listener {

    @EventHandler
    public void onPluginDisable(PluginDisableEvent e) {
        if (e.getPlugin().equals(Main.getInstance())) {
            Logger.info("Disabling plugin...");
            Bukkit.getScheduler().cancelTasks(Main.getInstance());
            Main.getSocketManager().disconnect();
        }
    }
}
