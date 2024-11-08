package de.minefactprogress.progressplugin;

import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.api.SocketManager;
import de.minefactprogress.progressplugin.commandsystem.CommandManager;
import de.minefactprogress.progressplugin.commandsystem.commands.DistrictCommand;
import de.minefactprogress.progressplugin.components.ConfigManager;
import de.minefactprogress.progressplugin.components.DistrictBossbar;
import de.minefactprogress.progressplugin.components.LocationEditor;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.listeners.*;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.utils.Config;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Main extends JavaPlugin {

    @Getter
    private static final String PREFIX = "§7[§bProgress§7] §7";
    @Getter
    private static final String PREFIX_SERVER = ChatColor.GOLD + "Server >> " + ChatColor.RESET;
    @Getter
    private static Main instance;
    @Getter
    private static SocketManager socketManager;
    @Getter
    private static DistrictBossbar districtBossbar;
    private final HashMap<Player, MenuStorage> menuStorages = new HashMap<>();
    @Getter
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;

        ConfigManager.setStandard();

        registerListeners();
        registerCommands();

        commandManager = new CommandManager();
        commandManager.init();

        districtBossbar = new DistrictBossbar();
        districtBossbar.startSchedulers();

        socketManager = new SocketManager();
        socketManager.startSchedulers();

        // LocationEditor.startScheduler();

        API.loadProgress();

        Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.DARK_GREEN + "Plugin enabled");
    }

    @Override
    public void onDisable() {
        // Remove every player from BossBar
        for(Player p : Bukkit.getOnlinePlayers()) {
            districtBossbar.removePlayer(p);
        }
        // Destroy all LocationEditor Entities
        LocationEditor.destroyAll();

        Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.DARK_RED + "Plugin disabled");
    }


    private void registerCommands() {
        getCommand("district").setExecutor(new DistrictCommand());
    }

    private void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickListener(), instance);
        Bukkit.getServer().getPluginManager().registerEvents(new JoinListener(), instance);
        Bukkit.getServer().getPluginManager().registerEvents(new LocationEditorListener(), instance);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), instance);
        Bukkit.getServer().getPluginManager().registerEvents(new PluginDisableListener(), instance);
        Bukkit.getServer().getPluginManager().registerEvents(new QuitListener(), instance);
        Bukkit.getServer().getPluginManager().registerEvents(new ChatListener(), instance);
    }

    public MenuStorage getMenuStorage(Player p) {
        // TODO: if loaded before users are loaded from api, user is null and never updated again
        if (!menuStorages.containsKey(p)) {
            menuStorages.put(p, new MenuStorage(p, User.getUserByUUID(p.getUniqueId())));
        }
        return menuStorages.get(p);
    }

    public boolean isProductionMode() {
        return Config.getBoolean("config", "productionMode");
    }
}
