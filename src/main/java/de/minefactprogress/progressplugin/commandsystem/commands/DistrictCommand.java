package de.minefactprogress.progressplugin.commandsystem.commands;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.entities.city.District;
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
            }else {
                if(District.getDistrictByName(String.join(" ",args))==null) return true;
                sendInfoMessage(p, District.getDistrictByName(String.join(" ",args)));
            }
        } else {
            p.sendMessage(Main.getPREFIX()+ ChatColor.GRAY+"Valid Subcommands: <<district name>|show|hide|toggle>");
            if(Main.getDistrictBossbar().getBars().containsKey(p.getUniqueId()))
                sendInfoMessage(p, Main.getDistrictBossbar().getCurrentDistrict(p));
        }
        return true;
    }

    private void sendInfoMessage(Player player, District district) {
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "------- " + ChatColor.BOLD + "District Overview" + ChatColor.GRAY + " -------");
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "Name: " + ChatColor.YELLOW + district.getName());
        player.sendMessage(ChatColor.GRAY + "Status: " + ChatColor.valueOf(district.getStatus().getColor()) + district.getStatus().getName());
        player.sendMessage(ChatColor.GRAY + "Progress: " + ChatColor.valueOf(district.getStatus().getColor()) + district.getProgress());
//        player.sendMessage(ChatColor.GRAY + "Blocks: " + ChatColor.YELLOW + (blocksDone + blocksLeft) + " (" + blocksDone + " done)");
        if (district.getProgress() == 100) {
            player.sendMessage(ChatColor.GRAY + "Completion Date: " + ChatColor.YELLOW + district.getCompletionDate());
        }
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "----------------------------");
        player.sendMessage("");
    }
}
