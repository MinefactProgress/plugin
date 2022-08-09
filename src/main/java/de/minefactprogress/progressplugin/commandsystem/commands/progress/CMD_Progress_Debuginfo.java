package de.minefactprogress.progressplugin.commandsystem.commands.progress;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.commandsystem.BaseCommand;
import de.minefactprogress.progressplugin.commandsystem.SubCommand;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.District;
import de.minefactprogress.progressplugin.entities.users.User;
import org.bukkit.command.CommandSender;

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
    public boolean isStaffOnly() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        sender.sendMessage(Main.getPREFIX() + "Users: " + User.users.size());
        sender.sendMessage(Main.getPREFIX() + "Districts: " + District.districts.size());
        sender.sendMessage(Main.getPREFIX() + "Blocks: " + Block.blocks.size());
    }
}
