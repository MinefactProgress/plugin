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

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class SubCommand implements ICommand {

    private final BaseCommand baseCommand;
    private final SubCommand subCommand;
    private final List<SubCommand> subCommands = new ArrayList<>();

    public SubCommand(BaseCommand baseCommand) {
        this.baseCommand = baseCommand;
        this.subCommand = null;
    }

    public SubCommand(BaseCommand baseCommand, SubCommand subCommand) {
        this.baseCommand = baseCommand;
        this.subCommand = subCommand;
    }

    /**
     * Executes sub command
     * @param sender player or console
     * @param args parameter
     */
    public abstract void onCommand(CommandSender sender, String[] args);

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
     * @param subCommand this
     */
    public void registerSubCommand(SubCommand subCommand) {
        subCommands.add(subCommand);
    }

    /**
     * @return Base Command
     */
    public BaseCommand getBaseCommand() {
        return baseCommand;
    }

    /**
     * @return Sub Command (if it exists)
     */
    public SubCommand getSubCommand() {
        return subCommand;
    }

    /**
     * @return All sub commands
     */
    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public void sendInfo(CommandSender sender) {
        // TODO
        sender.sendMessage("TODO");
    }
}