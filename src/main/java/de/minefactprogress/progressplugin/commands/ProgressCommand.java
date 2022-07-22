package de.minefactprogress.progressplugin.commands;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.RequestHandler;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.District;
import de.minefactprogress.progressplugin.menusystem.menus.NewYorkCityMenu;
import de.minefactprogress.progressplugin.utils.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProgressCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage(Main.getPREFIX() + ChatColor.RED + "You have to be a player to execute this command");
            return true;
        }

        if (args.length == 0) {
            if (checkForData(p)) {
                new NewYorkCityMenu(Main.getInstance().getMenuStorage(p), null).open();
            }
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if(checkStaffPermission(p)) {
                    Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                        p.sendMessage(Main.getPREFIX() + ChatColor.GRAY + "Reloading data...");
                        RequestHandler.getInstance().requestUsers();
                        RequestHandler.getInstance().requestDistricts();
                        RequestHandler.getInstance().requestBlocks();
                        p.sendMessage(Main.getPREFIX() + ChatColor.GREEN + "Data reloaded successfully");
                    });
                }
                return true;
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();

        if(!(sender instanceof Player p)) return tab;

        if(args.length == 1) {
            if(Permissions.isTeamMember(p)) {
                if("reload".startsWith(args[0].toLowerCase())) tab.add("reload");
            }
        }

        Collections.sort(tab);
        return tab;
    }

    private boolean checkForData(Player p) {
        if(District.districts.isEmpty() || Block.blocks.isEmpty()) {
            p.sendMessage(Main.getPREFIX() + ChatColor.RED + "Couldn't load data. Please try again later!");
            return false;
        }
        return true;
    }

    private boolean checkStaffPermission(Player p) {
        if(!Permissions.isTeamMember(p)) {
            p.sendMessage(Main.getPREFIX() + ChatColor.RED + "You don't have permission to execute this command!");
            return false;
        }
        return true;
    }
}
