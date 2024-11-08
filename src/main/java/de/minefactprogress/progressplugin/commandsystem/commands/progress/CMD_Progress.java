package de.minefactprogress.progressplugin.commandsystem.commands.progress;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.commandsystem.BaseCommand;
import de.minefactprogress.progressplugin.menusystem.menus.NewYorkCityMenu;
import de.minefactprogress.progressplugin.utils.Permissions;
import de.minefactprogress.progressplugin.utils.ProgressUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CMD_Progress extends BaseCommand {

    public CMD_Progress() {
        registerSubCommand(new CMD_Progress_Debug(this));
        registerSubCommand(new CMD_Progress_Info(this));
        registerSubCommand(new CMD_Progress_Reload(this));
        registerSubCommand(new CMD_Progress_Teleport(this));
        registerSubCommand(new CMD_Progress_User(this));
        registerSubCommand(new CMD_Progress_Verify(this));
    }

    @Override
    public String[] getNames() {
        return new String[] { "progress" };
    }

    @Override
    public String getDescription() {
        return "View the progress of New York City";
    }

    @Override
    public String[] getParameter() {
        return new String[0];
    }

    @Override
    public List<String> getTabCompletion(CommandSender sender) {
        return getSubCommands().stream()
                .filter(sub -> !sub.isStaffOnly() || Permissions.isTeamMember(getPlayer(sender)))
                .map(sub -> sub.getNames()[0]).toList();
    }

    @Override
    public boolean isStaffOnly() {
        return false;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        if(args.length == 0) {
            Player p = getPlayer(sender);

            if(p == null) {
                sender.sendMessage(Main.getPREFIX() + ChatColor.RED + "This command can only be executed by a player!");
                return true;
            }

            if (ProgressUtils.checkForData(p)) {
                new NewYorkCityMenu(Main.getInstance().getMenuStorage(p), null).open();
            }
            return true;
        }
        return super.onCommand(sender, cmd, s, args);
    }
}
