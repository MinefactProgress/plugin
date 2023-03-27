package de.minefactprogress.progressplugin.commandsystem.commands.progress;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.commandsystem.BaseCommand;
import de.minefactprogress.progressplugin.commandsystem.SubCommand;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.menusystem.menus.ClaimsMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CMD_Progress_User extends SubCommand {

    public CMD_Progress_User(BaseCommand baseCommand) {
        super(baseCommand);
    }

    @Override
    public String[] getNames() {
        return new String[] { "user" };
    }

    @Override
    public String getDescription() {
        return "View all claims by a user";
    }

    @Override
    public String[] getParameter() {
        return new String[] { "User" };
    }

    @Override
    public List<String> getTabCompletion(CommandSender sender) {
        return API.getUsers().stream()
                .filter(user -> user.getRank() != null && (user.isStaff() || user.countClaims() > 0))
                .map(User::getUsername)
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public boolean isStaffOnly() {
        return false;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player p = getPlayer(sender);

        if (p == null) {
            sender.sendMessage(Main.getPREFIX() + ChatColor.RED + "This command can only be executed by a player!");
            return;
        }

        if(args.length != 1) {
            p.sendMessage(Main.getPREFIX() + ChatColor.RED + "Wrong usage: /progress user <Username>");
            return;
        }

        User user = User.getUserByName(args[0]);
        if(user == null) {
            p.sendMessage(Main.getPREFIX() + ChatColor.RED + "No user found with the name " + ChatColor.YELLOW + args[0]);
            return;
        }

        Main.getInstance().getMenuStorage(p).setClaimsUser(user);
        new ClaimsMenu(Main.getInstance().getMenuStorage(p), null).open();
    }
}
