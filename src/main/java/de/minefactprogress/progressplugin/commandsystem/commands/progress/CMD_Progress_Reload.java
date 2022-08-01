package de.minefactprogress.progressplugin.commandsystem.commands.progress;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.RequestHandler;
import de.minefactprogress.progressplugin.commandsystem.BaseCommand;
import de.minefactprogress.progressplugin.commandsystem.SubCommand;
import de.minefactprogress.progressplugin.utils.ProgressUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_Progress_Reload extends SubCommand {

    public CMD_Progress_Reload(BaseCommand baseCommand) {
        super(baseCommand);
    }

    @Override
    public String[] getNames() {
        return new String[] { "reload", "rl" };
    }

    @Override
    public String getDescription() {
        return "Reloads the progress data";
    }

    @Override
    public String[] getParameter() {
        return new String[0];
    }

    @Override
    public boolean isStaffOnly() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(sender instanceof Player p && !ProgressUtils.checkStaffPermission(p)) {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            sender.sendMessage(Main.getPREFIX() + ChatColor.GRAY + "Reloading data...");
            RequestHandler.getInstance().requestUsers();
            RequestHandler.getInstance().requestDistricts();
            RequestHandler.getInstance().requestBlocks();
            sender.sendMessage(Main.getPREFIX() + ChatColor.GREEN + "Data reloaded successfully");
        });
    }
}
