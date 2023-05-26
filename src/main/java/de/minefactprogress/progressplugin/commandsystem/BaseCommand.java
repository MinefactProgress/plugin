/*
 * The MIT License (MIT)
 *
 *  Copyright © 2021, Alps BTE <bte.atchli@gmail.com>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package de.minefactprogress.progressplugin.commandsystem;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.utils.Constants;
import de.minefactprogress.progressplugin.utils.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class BaseCommand implements TabExecutor, ICommand {
    private final List<SubCommand> subCommands = new ArrayList<>();

    /**
     * Executes base command and checks for sub commands
     * @param sender player or console
     * @param args parameter
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        if (args.length > 0) {
            SubCommand subCommand = subCommands.stream()
                    .filter(subCmd -> Arrays.stream(subCmd.getNames()).anyMatch(sub -> sub.equalsIgnoreCase(args[0])))
                    .findFirst()
                    .orElse(null);

            // Check if sub commands have sub commands
            if (subCommand != null && args.length > 1) {
                SubCommand iterateSubCommand;
                for (int i = 1; i <= args.length; i++) {
                    final int index = i;
                    iterateSubCommand = subCommand.getSubCommands().stream()
                            .filter(subCmd -> Arrays.stream(subCmd.getNames()).anyMatch(sub -> sub.equalsIgnoreCase(args[index])))
                            .findFirst()
                            .orElse(null);

                    if (iterateSubCommand == null) {
                        break;
                    } else {
                        subCommand = iterateSubCommand;
                    }
                }
            }

            if (subCommand == null) {
                sendInfo(sender, null, null);
            } else {
                if (subCommand.isStaffOnly() && sender instanceof Player p && !Permissions.isTeamMember(p)) {
                    sender.sendMessage(Main.getPREFIX() + ChatColor.RED + "You don't have permission to execute this command!");
                    return true;
                }
                subCommand.onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        SubCommand subCommand = subCommands.stream()
                .filter(subCmd -> Arrays.stream(subCmd.getNames()).anyMatch(sub -> sub.equalsIgnoreCase(args[0])))
                .findFirst()
                .orElse(null);
        if(subCommand == null && args.length <= 1) {
            if(getTabCompletion(sender) == null) {
                return new ArrayList<>();
            }
            return getTabCompletion(sender).stream().filter(str -> str.toLowerCase().startsWith(args[args.length-1].toLowerCase())).sorted().toList();
        }
        for(int i = 0; i < args.length; i++) {
            if(subCommand == null) break;
            int finalI = i;
            if(args.length == i+2 && Arrays.stream(subCommand.getNames()).anyMatch(arg -> arg.equalsIgnoreCase(args[finalI]))) {
                break;
            }

            subCommand = subCommand.getSubCommand();
        }
        return subCommand != null && subCommand.getTabCompletion(sender) != null
                ? subCommand.getTabCompletion(sender).stream()
                    .filter(str -> str.toLowerCase().startsWith(args[args.length-1].toLowerCase()))
                    .sorted().toList() : new ArrayList<>();
    }

    /**
     * @return All sub commands
     */
    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    /**
     * Gets the player
     * @param sender player
     * @return null if sender is not a player
     */
    protected Player getPlayer(CommandSender sender) {
        return sender instanceof Player ? (Player) sender : null;
    }

    /**
     * Registers sub command
     * @param subCommand base sub command
     */
    protected void registerSubCommand(SubCommand subCommand) {
        subCommands.add(subCommand);
    }

    protected String getPrefix() {
        return Constants.PREFIX;
    }

    @Override
    public void sendInfo(CommandSender sender, String usage, Map<String, String> arguments) {
        StringBuilder line = new StringBuilder();

        int lineSegmentsPerSide = (66 - (11 + getPrefix().length() + getNames()[0].length())) / 2;

        line.append("-".repeat(Math.max(0, lineSegmentsPerSide)));
        line.append(ChatColor.YELLOW).append(" Help for /").append(getNames()[0]).append(" ").append(ChatColor.YELLOW).append(ChatColor.STRIKETHROUGH);
        line.append("-".repeat(Math.max(0, lineSegmentsPerSide)));

        sender.sendMessage(getPrefix() + ChatColor.YELLOW + ChatColor.STRIKETHROUGH + line);
        sender.sendMessage(ChatColor.GRAY + getDescription());
        if (usage != null) {
            sender.sendMessage(ChatColor.GRAY + "Usage: " + ChatColor.GOLD + usage);
        }
        if (arguments != null) {
            sender.sendMessage(ChatColor.GRAY + "Arguments: ");
            for (Map.Entry<String, String> entry : arguments.entrySet()) {
                sender.sendMessage("  " + ChatColor.GOLD + entry.getKey() + ChatColor.YELLOW + ": " + entry.getValue());
            }
        }
    }
}