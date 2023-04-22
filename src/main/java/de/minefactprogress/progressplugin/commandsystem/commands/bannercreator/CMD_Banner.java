package de.minefactprogress.progressplugin.commandsystem.commands.bannercreator;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.commandsystem.BaseCommand;
import de.minefactprogress.progressplugin.menusystem.menus.bannercreator.BannerBaseMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CMD_Banner extends BaseCommand {

    @Override
    public String[] getNames() {
        return new String[] { "bc" };
    }

    @Override
    public String getDescription() {
        return "Create a banner";
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
        Player p = getPlayer(sender);

        if(p == null) {
            sender.sendMessage(Main.getPREFIX() + ChatColor.RED + "This command can only be executed by a player!");
            return true;
        }

        new BannerBaseMenu(Main.getInstance().getMenuStorage(p), null).open();
        return super.onCommand(sender, cmd, s, args);
    }
}
