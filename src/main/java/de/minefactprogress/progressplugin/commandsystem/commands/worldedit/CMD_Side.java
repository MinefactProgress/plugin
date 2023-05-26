package de.minefactprogress.progressplugin.commandsystem.commands.worldedit;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import de.minefactprogress.progressplugin.commandsystem.BaseCommand;
import de.minefactprogress.progressplugin.utils.Constants;
import de.minefactprogress.progressplugin.utils.WorldEditUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CMD_Side extends BaseCommand {

    @Override
    public String[] getNames() {
        return new String[] { "/side" };
    }

    @Override
    public String getDescription() {
        return "Set blocks based on surrounding blocks";
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
        return false;
    }

    @Override
    protected String getPrefix() {
        return Constants.PREFIX_WORLDEDIT;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Constants.PREFIX_WORLDEDIT + Constants.NO_PLAYER);
            return true;
        }
        if (!player.hasPermission("worldedit.wand")) {
            player.sendMessage(Constants.PREFIX_WORLDEDIT + Constants.NO_PERMISSION);
            return true;
        }

        if (args.length >= 3) {
            BlockType preBlock = BlockTypes.get(args[0].toLowerCase());
            BlockType postBlock = BlockTypes.get(args[1].toLowerCase());
            String direction = args[2].toLowerCase();
            boolean ignoreSameBlock = false;
            List<String> masks = null;

            if(preBlock == null) {
                player.sendMessage(Constants.PREFIX_WORLDEDIT + ChatColor.RED + "Invalid source block type");
                return true;
            }
            if(postBlock == null) {
                player.sendMessage(Constants.PREFIX_WORLDEDIT + ChatColor.RED + "Invalid target block type");
                return true;
            }

            if (args.length >= 4) {
                if (args[3].equalsIgnoreCase("y") || args[3].equalsIgnoreCase("yes")) {
                    ignoreSameBlock = true;
                } else if (!args[3].equalsIgnoreCase("n") && !args[3].equalsIgnoreCase("no")) {
                    player.sendMessage(Constants.PREFIX_WORLDEDIT + ChatColor.RED + "Wrong usage:");
                    player.sendMessage(Constants.PREFIX_WORLDEDIT + ChatColor.RED + "//side <Block-ID> <Block-ID> <Direction[n,e,s,w]> <ignoreSameBlocks[y,n] <Mask1> <Mask2> <...>");
                    return true;
                }

                if (args.length >= 5) {
                    masks = new ArrayList<>(Arrays.asList(args).subList(4, args.length));
                }
            }

            World world = player.getWorld();
            com.sk89q.worldedit.entity.Player actor = BukkitAdapter.adapt(player);
            LocalSession session = WorldEditUtils.getLocalSession(player);

            if(session == null) return true;

            Region region = session.getSelection(session.getSelectionWorld());

            // Search for blocks to replace
            Set<BlockVector3> editSet = new HashSet<>();

            for (int i = region.getMinimumPoint().getBlockX(); i <= region.getMaximumPoint().getBlockX(); i++) {
                for (int j = region.getMinimumPoint().getBlockY(); j <= region.getMaximumPoint().getBlockY(); j++) {
                    for (int k = region.getMinimumPoint().getBlockZ(); k <= region.getMaximumPoint().getBlockZ(); k++) {
                        if (region.contains(BlockVector3.at(i, j, k))) {
                            Block block = world.getBlockAt(i, j, k);
                            if (block.getType().toString().equalsIgnoreCase(preBlock.getResource())) {
                                if (masks == null) {
                                    switch (direction) {
                                        case "north", "n" -> {
                                            if (!ignoreSameBlock) {
                                                if (!world.getBlockAt(i, j, k - 1).getType().toString().equalsIgnoreCase(preBlock.getResource())) {
                                                    editSet.add(BlockVector3.at(i, j, k - 1));
                                                }
                                            } else {
                                                editSet.add(BlockVector3.at(i, j, k - 1));
                                            }
                                        }
                                        case "east", "e" -> {
                                            if (!ignoreSameBlock) {
                                                if (!world.getBlockAt(i + 1, j, k).getType().toString().equalsIgnoreCase(preBlock.getResource())) {
                                                    editSet.add(BlockVector3.at(i + 1, j, k));
                                                }
                                            } else {
                                                editSet.add(BlockVector3.at(i + 1, j, k));
                                            }
                                        }
                                        case "south", "s" -> {
                                            if (!ignoreSameBlock) {
                                                if (!world.getBlockAt(i, j, k + 1).getType().toString().equalsIgnoreCase(preBlock.getResource())) {
                                                    editSet.add(BlockVector3.at(i, j, k + 1));
                                                }
                                            } else {
                                                editSet.add(BlockVector3.at(i, j, k + 1));
                                            }
                                        }
                                        case "west", "w" -> {
                                            if (!ignoreSameBlock) {
                                                if (!world.getBlockAt(i - 1, j, k).getType().toString().equalsIgnoreCase(preBlock.getResource())) {
                                                    editSet.add(BlockVector3.at(i - 1, j, k));
                                                }
                                            } else {
                                                editSet.add(BlockVector3.at(i - 1, j, k));
                                            }
                                        }
                                    }
                                } else {
                                    for (String mask : masks) {
                                        switch (direction) {
                                            case "north", "n" -> {
                                                if (!ignoreSameBlock) {
                                                    if (!world.getBlockAt(i, j, k - 1).getType().toString().equalsIgnoreCase(preBlock.getResource()) && world.getBlockAt(i, j, k - 1).getType().toString().equalsIgnoreCase(mask)) {
                                                        editSet.add(BlockVector3.at(i, j, k - 1));
                                                    }
                                                } else {
                                                    if (world.getBlockAt(i, j, k - 1).getType().toString().equalsIgnoreCase(mask)) {
                                                        editSet.add(BlockVector3.at(i, j, k - 1));
                                                    }
                                                }
                                            }
                                            case "east", "e" -> {
                                                if (!ignoreSameBlock) {
                                                    if (!world.getBlockAt(i + 1, j, k).getType().toString().equalsIgnoreCase(preBlock.getResource()) && world.getBlockAt(i + 1, j, k).getType().toString().equalsIgnoreCase(mask)) {
                                                        editSet.add(BlockVector3.at(i + 1, j, k));
                                                    }
                                                } else {
                                                    if (world.getBlockAt(i + 1, j, k).getType().toString().equalsIgnoreCase(mask)) {
                                                        editSet.add(BlockVector3.at(i + 1, j, k));
                                                    }
                                                }
                                            }
                                            case "south", "s" -> {
                                                if (!ignoreSameBlock) {
                                                    if (!world.getBlockAt(i, j, k + 1).getType().toString().equalsIgnoreCase(preBlock.getResource()) && world.getBlockAt(i, j, k + 1).getType().toString().equalsIgnoreCase(mask)) {
                                                        editSet.add(BlockVector3.at(i, j, k + 1));
                                                    }
                                                } else {
                                                    if (world.getBlockAt(i, j, k + 1).getType().toString().equalsIgnoreCase(mask)) {
                                                        editSet.add(BlockVector3.at(i, j, k + 1));
                                                    }
                                                }
                                            }
                                            case "west", "w" -> {
                                                if (!ignoreSameBlock) {
                                                    if (!world.getBlockAt(i - 1, j, k).getType().toString().equalsIgnoreCase(preBlock.getResource()) && world.getBlockAt(i - 1, j, k).getType().toString().equalsIgnoreCase(mask)) {
                                                        editSet.add(BlockVector3.at(i - 1, j, k));
                                                    }
                                                } else {
                                                    if (world.getBlockAt(i - 1, j, k).getType().toString().equalsIgnoreCase(mask)) {
                                                        editSet.add(BlockVector3.at(i - 1, j, k));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            try (EditSession editSession = session.createEditSession(actor)) {
                editSession.setBlocks(editSet, postBlock);
                editSession.flushQueue();
                session.remember(editSession);
            }
            player.sendMessage(Constants.PREFIX_WORLDEDIT + "§7Successfully replaced §6§l" + editSet.size() + " §r§7blocks sideways!");
            return true;
        }

        sendInfo(
                sender,
                "//side <Side-ID> <Place-ID> <Direction> [ignoreSameBlocks] <Mask1> <Mask2> <...>",
                new LinkedHashMap<>() {{
                    put("<Side-ID>", "The BlockID the blocks should be placed next to");
                    put("<Place-ID>", "The BlockID that should be placed");
                    put("<Direction>", "Direction to place blocks (north (n), east (e), south (s), west (w))");
                    put("[ignoreSameBlocks]", "Whether same blocks should be ignored (yes (y), no (n))");
                    put("[Mask]", "The block mask");
                }}
    );
        return super.onCommand(sender, cmd, s, args);
    }
}
