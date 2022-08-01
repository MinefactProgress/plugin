package de.minefactprogress.progressplugin.commandsystem.commands.progress;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.commandsystem.BaseCommand;
import de.minefactprogress.progressplugin.commandsystem.SubCommand;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.District;
import de.minefactprogress.progressplugin.menusystem.menus.BlocksMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD_Progress_Info extends SubCommand {

    public CMD_Progress_Info(BaseCommand baseCommand) {
        super(baseCommand);
    }

    @Override
    public String[] getNames() {
        return new String[] { "info" };
    }

    @Override
    public String getDescription() {
        return "Opens the GUI of the specified district";
    }

    @Override
    public String[] getParameter() {
        return new String[] { "District" };
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player p = getPlayer(sender);

        if(p == null) {
            sender.sendMessage(Main.getPREFIX() + ChatColor.RED + "This command can only be executed by a player!");
            return;
        }

        if(args.length == 0 || args.length > 2) {
            p.sendMessage(Main.getPREFIX() + ChatColor.RED + "Wrong usage: /progress info <District> [<Block>]");
            return;
        }

        District district = District.getDistrictByName(args[0]);
        if(district == null) {
            p.sendMessage(Main.getPREFIX() + ChatColor.RED + "District " + ChatColor.YELLOW + args[0] + ChatColor.RED + " not found");
            return;
        }

        if (Block.getBlocksOfDistrict(district).isEmpty()) {
            p.sendMessage(Main.getPREFIX() + ChatColor.RED + "This district has no blocks!");
            return;
        }

        if(args.length == 1) {
            Main.getInstance().getMenuStorage(p).setDistrict(district);
            new BlocksMenu(Main.getInstance().getMenuStorage(p), null, 0, 0).open();
        } else {

        }
    }
}
