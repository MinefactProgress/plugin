package de.minefactprogress.progressplugin.commandsystem.commands.progress;

import com.google.gson.JsonObject;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.commandsystem.BaseCommand;
import de.minefactprogress.progressplugin.commandsystem.SubCommand;
import de.minefactprogress.progressplugin.entities.users.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CMD_Progress_Verify extends SubCommand {

    public CMD_Progress_Verify(BaseCommand baseCommand) {
        super(baseCommand);
    }

    @Override
    public String[] getNames() {
        return new String[] { "verify" };
    }

    @Override
    public String getDescription() {
        return "Link your Minecraft account to your Website account";
    }

    @Override
    public String[] getParameter() {
        return new String[] { "Code" };
    }

    @Override
    public List<String> getTabCompletion(CommandSender sender) {
        return null;
    }

    @Override
    public boolean isStaffOnly() {
        return false;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player p = getPlayer(sender);

        if(p == null) {
            sender.sendMessage(Main.getPREFIX() + ChatColor.RED + "This command can only be executed by a player!");
            return;
        }

        User user = User.getUserByUUID(p.getUniqueId());

        if(user != null) {
            p.sendMessage(Main.getPREFIX() + ChatColor.RED + "Your account is already linked to a website account!");
            return;
        }

        if(args.length != 1) {
            p.sendMessage(Main.getPREFIX() + ChatColor.RED + "Wrong usage: /progress verify <Code>");
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            p.sendMessage(Main.getPREFIX() + ChatColor.GRAY + "Verifying your code...");
            JsonObject json = new JsonObject();
            json.addProperty("code", args[0]);
            json.addProperty("uuid", p.getUniqueId().toString());
            API.PUT("/verify", json, (res) -> {
                p.sendMessage(Main.getPREFIX() + ChatColor.GREEN + "Successfully linked your Minecraft account to your Website account!");
                User verifiedUser = User.getUserById(res.get("data").getAsJsonObject().get("data").getAsJsonObject().get("uid").getAsInt());
                if(verifiedUser == null) return;
                verifiedUser.setUuid(p.getUniqueId());
            }, p);
        });
    }
}
