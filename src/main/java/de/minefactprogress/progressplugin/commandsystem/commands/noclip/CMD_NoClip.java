package de.minefactprogress.progressplugin.commandsystem.commands.noclip;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.commandsystem.BaseCommand;
import de.minefactprogress.progressplugin.components.NoClipHandler;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CMD_NoClip extends BaseCommand {

    @Override
    public String[] getNames() {
        return new String[] { "noclip", "nc" };
    }

    @Override
    public String getDescription() {
        return "Toggle NoClip-Mode";
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

        if(NoClipHandler.getInstance().getNoClipPlayers().contains(p.getUniqueId())) {
            NoClipHandler.getInstance().removePlayer(p);
            p.sendMessage(Main.getPREFIX_SERVER() + ChatColor.GRAY + "Disabled NoClip-Mode");
            if(p.getGameMode().equals(GameMode.SPECTATOR)) {
                p.setGameMode(GameMode.CREATIVE);
            }
        } else {
            NoClipHandler.getInstance().addPlayer(p);
            p.sendMessage(Main.getPREFIX_SERVER() + ChatColor.GRAY + "Enabled NoClip-Mode");
        }

        return super.onCommand(sender, cmd, s, args);
    }
}
