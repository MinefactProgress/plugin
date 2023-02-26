package de.minefactprogress.progressplugin.commandsystem.commands.locationeditor;

import de.minefactprogress.progressplugin.commandsystem.BaseCommand;
import de.minefactprogress.progressplugin.components.LocationEditor;
import de.minefactprogress.progressplugin.utils.Constants;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CMD_LocationEditor extends BaseCommand {

    public CMD_LocationEditor() {

    }

    @Override
    public String[] getNames() {
        return new String[]{ "locationeditor" };
    }

    @Override
    public String getDescription() {
        return ChatColor.RED + "Use: /locationeditor <start|stop>";
    }

    @Override
    public String[] getParameter() {
        return new String[0];
    }

    @Override
    public List<String> getTabCompletion(CommandSender sender) {
        return null;
    }

    @Override
    public boolean isStaffOnly() {
        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        if(sender instanceof Player p && args.length == 1) {
            if(args[0].equalsIgnoreCase("start")) {
                if(LocationEditor.get(p) != null) {
                    p.sendMessage(Constants.PREFIX + ChatColor.RED + "LocationEditor already started");
                    return true;
                }
                new LocationEditor(p);
                return true;
            } else if(args[0].equalsIgnoreCase("stop")) {
                if(LocationEditor.get(p) == null) {
                    p.sendMessage(Constants.PREFIX + ChatColor.RED + "LocationEditor is not running");
                    return true;
                }
                LocationEditor.get(p).destroy();
                return true;
            } else if(args[0].equalsIgnoreCase("find")) {
                if(LocationEditor.get(p) == null) {
                    p.sendMessage(Constants.PREFIX + ChatColor.RED + "LocationEditor is not running");
                    return true;
                }
                Location loc = LocationEditor.get(p).getNearestPoint(p.getLocation());
                if(loc == null) {
                    p.sendMessage(Constants.PREFIX + ChatColor.RED + "No point found");
                    return true;
                }
                p.teleport(loc);
                p.sendMessage(Constants.PREFIX + ChatColor.GRAY + "Teleported to nearest point");
                return true;
            }
        }
        return super.onCommand(sender, cmd, s, args);
    }
}
