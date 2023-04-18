package de.minefactprogress.progressplugin.commandsystem.commands.progress;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.commandsystem.BaseCommand;
import de.minefactprogress.progressplugin.commandsystem.SubCommand;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.District;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.utils.Constants;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CMD_Progress_Debug extends SubCommand {

    public CMD_Progress_Debug(BaseCommand baseCommand) {
        super(baseCommand);
    }

    @Override
    public String[] getNames() {
        return new String[] { "debug" };
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
        return List.of("user", "block", "district");
    }

    @Override
    public boolean isStaffOnly() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(Main.getPREFIX() + "Users: " + API.getUsers().size());
            sender.sendMessage(Main.getPREFIX() + "Districts: " + API.getDistricts().size());
            sender.sendMessage(Main.getPREFIX() + "Blocks: " + API.getBlocks().size());
            sender.sendMessage(Main.getPREFIX() + "Socket: " + (Main.getSocketManager().isConnected() ? ChatColor.GREEN + "Connected" : ChatColor.RED + "Disconnected"));
            return;
        } else if(args.length == 2) {
            try {
                String type = args[0];
                int id = Integer.parseInt(args[1]);

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                if(type.equalsIgnoreCase("user")) {
                    sender.sendMessage(gson.toJson(User.getUserById(id)));
                } else if (type.equalsIgnoreCase("block")) {
                    sender.sendMessage(gson.toJson(Block.getBlockById(id)));
                } else if (type.equalsIgnoreCase("district")) {
                    sender.sendMessage(gson.toJson(District.getDistrictById(id)));
                } else {
                    sender.sendMessage(Constants.PREFIX + ChatColor.RED + "Invalid type! Use one of: block, district, user");
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(Constants.PREFIX + ChatColor.RED + "Invalid ID");
            }
            return;
        }
        sender.sendMessage(Constants.PREFIX + ChatColor.RED + "Wrong usage: /progress debug <type> <ID>");
    }
}
