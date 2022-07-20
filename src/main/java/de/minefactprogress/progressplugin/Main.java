package de.minefactprogress.progressplugin;

import de.minefactprogress.progressplugin.api.RequestHandler;
import de.minefactprogress.progressplugin.components.DistrictBossbar;
import de.minefactprogress.progressplugin.listeners.InventoryClickListener;
import de.minefactprogress.progressplugin.listeners.JoinListener;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Main extends JavaPlugin {

    @Getter
    private static final String PREFIX = "§7[§bProgress§7] §r";
    @Getter
    private static Main instance;
    @Getter
    private static DistrictBossbar districtBossbar;
    private final HashMap<Player, MenuStorage> menuStorages = new HashMap<>();

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
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickListener(), instance);
        Bukkit.getServer().getPluginManager().registerEvents(new JoinListener(), instance);
    }

    public MenuStorage getMenuStorage(Player p) {
        if (!menuStorages.containsKey(p)) {
            menuStorages.put(p, new MenuStorage(p));
        }
        return menuStorages.get(p);
    }
}
