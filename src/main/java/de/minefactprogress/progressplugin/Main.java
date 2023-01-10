package de.minefactprogress.progressplugin;

import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.api.SocketManager;
import de.minefactprogress.progressplugin.commandsystem.CommandManager;
import de.minefactprogress.progressplugin.commandsystem.commands.DistrictCommand;
import de.minefactprogress.progressplugin.components.DistrictBossbar;
import de.minefactprogress.progressplugin.listeners.InventoryClickListener;
import de.minefactprogress.progressplugin.listeners.JoinListener;
import de.minefactprogress.progressplugin.listeners.PlayerInteractListener;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Main extends JavaPlugin {

    @Getter
    private static final String PREFIX = "§7[§bProgress§7] §r";
    @Getter
    private static final String PREFIX_SERVER = ChatColor.GOLD + "Server >> " + ChatColor.RESET;
    private static final String SOCKET_URL = "https://progressbackend.minefact.de";
    @Getter
    private static Main instance;
    private static SocketManager socketManager;
    @Getter
    private static DistrictBossbar districtBossbar;
    private final HashMap<Player, MenuStorage> menuStorages = new HashMap<>();
    @Getter
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;
        registerListeners();
        registerCommands();

        commandManager = new CommandManager();
        commandManager.init();

        districtBossbar = new DistrictBossbar();
        districtBossbar.startSchedulers();

        socketManager = new SocketManager(SOCKET_URL);
        socketManager.listenEvent("motd");

        API.loadProgress();

        Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.DARK_GREEN + "Plugin enabled");
    }

    @Override
    public void onDisable() {
        // Remove every player from BossBar
        for(Player p : Bukkit.getOnlinePlayers()) {
            districtBossbar.removePlayer(p);
        }

        socketManager.disconnect();

        Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.DARK_RED + "Plugin disabled");
    }


    private void registerCommands() {
        getCommand("district").setExecutor(new DistrictCommand());
    }

    private void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickListener(), instance);
        Bukkit.getServer().getPluginManager().registerEvents(new JoinListener(), instance);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), instance);
    }

    public MenuStorage getMenuStorage(Player p) {
        if (!menuStorages.containsKey(p)) {
            menuStorages.put(p, new MenuStorage(p));
        }
        return menuStorages.get(p);
    }
}
