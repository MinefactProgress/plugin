package de.minefactprogress.progressplugin;

import de.minefactprogress.progressplugin.api.RequestHandler;
import de.minefactprogress.progressplugin.components.DistrictBossbar;
import de.minefactprogress.progressplugin.listeners.JoinListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;
    @Getter
    private static DistrictBossbar districtBossbar;
    @Getter
    private static final String PREFIX = "§7[§bProgress§7] §r";

    @Override
    public void onEnable() {
        instance = this;
        registerListeners();
        registerCommands();
        districtBossbar = new DistrictBossbar();
        districtBossbar.startSchedulers();

        RequestHandler.getInstance().startSchedulers();

        Bukkit.getConsoleSender().sendMessage(PREFIX + "§2Plugin enabled");
    }

    @Override
    public void onDisable() {

    }


    private void registerCommands() {

    }

    private void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new JoinListener(),instance);
    }
}
