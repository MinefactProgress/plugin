package de.minefactprogress.progressplugin.commandsystem.commands.progress;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.commandsystem.BaseCommand;
import de.minefactprogress.progressplugin.commandsystem.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CMD_Progress_Debuginfo extends SubCommand {

    public CMD_Progress_Debuginfo(BaseCommand baseCommand) {
        super(baseCommand);
    }

    @Override
    public String[] getNames() {
        return new String[] { "debuginfo" };
    }

    @Override
    public String getDescription() {
        return "Displays the number of loaded objects. For dev use only.";
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
    public void onCommand(CommandSender sender, String[] args) {
        sender.sendMessage(Main.getPREFIX() + "Users: " + API.getUsers().size());
        sender.sendMessage(Main.getPREFIX() + "Districts: " + API.getDistricts().size());
        sender.sendMessage(Main.getPREFIX() + "Blocks: " + API.getBlocks().size());
        sender.sendMessage(Main.getPREFIX() + "Socket: " + (Main.getSocketManager().isConnected() ? ChatColor.GREEN + "Connected" : ChatColor.RED + "Disconnected"));
    }
}
