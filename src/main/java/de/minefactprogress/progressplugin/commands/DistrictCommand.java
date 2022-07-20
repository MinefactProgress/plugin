package de.minefactprogress.progressplugin.commands;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.components.DistrictBossbar;
import de.minefactprogress.progressplugin.entities.city.District;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

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
            }else {
                if(District.getDistrictByName(String.join(" ",args))==null) return true;
                District.getDistrictByName(String.join(" ",args)).sendInfoMessage(p);
            }
        } else {
            p.sendMessage(Main.getPREFIX()+ ChatColor.GRAY+"Valid Subcommands: <<district name>|show|hide|toggle>");
            if(Main.getDistrictBossbar().getBars().containsKey(p))
                Main.getDistrictBossbar().getCurrentDistrict(p).sendInfoMessage(p);
        }
        return true;
    }
}
