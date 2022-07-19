package de.minefactprogress.progressplugin;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        registerListeners();
        registerCommands();
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private void registerCommands() {
        // instance.getCommand("spawn").setExecutor(new Command());
    }

    private void registerListeners() {
        // Bukkit.getPluginManager().registerEvents(new Listener(), instance);
    }
}
