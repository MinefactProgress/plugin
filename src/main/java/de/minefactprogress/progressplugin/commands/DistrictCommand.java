package de.minefactprogress.progressplugin.commands;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.components.DistrictBossbar;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DistrictCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return false;
        if(args.length >0) {
            if(args[0].equalsIgnoreCase("show")) {
                Main.getDistrictBossbar().addPlayer(p);
                p.sendMessage(Main.getPREFIX()+ ChatColor.GRAY+"Set the District Bossbar visible");
            }else if(args[0].equalsIgnoreCase("hide")) {
                Main.getDistrictBossbar().removePlayer(p);
                p.sendMessage(Main.getPREFIX()+ ChatColor.GRAY+"Set the District Bossbar invisible");
            }else if(args[0].equalsIgnoreCase("toggle")) {
                Main.getDistrictBossbar().togglePlayer(p);
                p.sendMessage(Main.getPREFIX()+ ChatColor.GRAY+"Toggled the District Bossbar");
            }
        } else {
            p.sendMessage(Main.getPREFIX()+ ChatColor.GRAY+"Valid Subcommands: <show|hide|toggle|info>");
        }
        return true;
    }
}
