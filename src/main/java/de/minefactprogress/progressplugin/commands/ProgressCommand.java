package de.minefactprogress.progressplugin.commands;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.menusystem.menus.NewYorkCityMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return null;
    }

    private boolean checkForData(Player p) {
        // TODO
        return true;
    }
}
