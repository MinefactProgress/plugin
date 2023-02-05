package de.minefactprogress.progressplugin.menusystem.menus;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.users.Rank;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.menusystem.menus.confirmation.AddBuilderConfirmMenu;
import de.minefactprogress.progressplugin.menusystem.menus.confirmation.DetailsConfirmMenu;
import de.minefactprogress.progressplugin.utils.CustomColors;
import de.minefactprogress.progressplugin.utils.Item;
import de.minefactprogress.progressplugin.utils.ProgressUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class EditBlockMenu extends Menu {

    public EditBlockMenu(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
    }

    @Override
    public Component menuName() {
        return Component
                .text(menuStorage.getDistrict().getName(), CustomColors.BLUE.getComponentColor())
                .append(Component.text(" (Block #" + menuStorage.getBlock().getId() + ")", NamedTextColor.DARK_GRAY));
    }

    @Override
    public int rows() {
        return 3;
    }

    @Override
    public ItemStack titleItem() {
        return menuStorage.getBlock().toItemStack(menuStorage.getOwner());
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = menuStorage.getOwner();
        ItemStack item = e.getCurrentItem();
        Block block = menuStorage.getBlock();

        if(item == null || block == null) return;

        switch (item.getType()) {
            case ENDER_PEARL:
                // Teleport
                Location loc = block.getCenter();
                if(loc != null) {
                    p.teleport(loc);
                    p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                    p.sendMessage(Main.getPREFIX() + ChatColor.GRAY + "Teleported to Block " + ChatColor.YELLOW + "#"
                            + block.getId() + ChatColor.GRAY + " of " + ChatColor.YELLOW + block.getDistrict().getName());
                } else {
                    p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
                    p.sendMessage(Main.getPREFIX() + ChatColor.RED + "No location found for this block");
                }
                break;
            case SMITHING_TABLE:
                // Progress
                new AnvilGUI.Builder()
                        .onComplete((player, text) -> {
                            try {
                                double progress = Double.parseDouble(text);

                                if(progress > 100 || progress < 0) {
                                    p.sendMessage(Main.getPREFIX() + ChatColor.RED + "The progress must be between 0 and 100");
                                    return AnvilGUI.Response.text(block.getProgress() + "");
                                }

                                block.setProgress(progress, p);

                                return AnvilGUI.Response.close();
                            } catch (NumberFormatException ex) {
                                p.sendMessage(Main.getPREFIX() + ChatColor.RED + "Please enter a valid number");
                                return AnvilGUI.Response.text(block.getProgress() + "");
                            }
                        })
                        .text(String.valueOf(block.getProgress()))
                        .itemLeft(new Item(Material.NAME_TAG).build())
                        .title("Set the new progress")
                        .plugin(Main.getInstance())
                        .open(p);
                break;
            case ROSE_BUSH: case DEAD_BUSH:
                // Details
                menuStorage.setEditDetails(item.getType().equals(Material.DEAD_BUSH));
                new DetailsConfirmMenu(menuStorage, this).open();
                break;
            case IRON_PICKAXE:
                // Builder
                if(e.getClick().isRightClick() || !block.getBuilders().isEmpty()) {
                    // Open Builder Manager
                    new BuilderManagerMenu(menuStorage, this).open();
                } else {
                    // Add yourself as builder
                    if(menuStorage.getBlock().getBuilders().contains(p.getName())) {
                        p.sendMessage(Main.getPREFIX() + ChatColor.RED + "You habe already claimed this block");
                        return;
                    }

                    new AddBuilderConfirmMenu(menuStorage, this).open();
                }
                break;
        }
    }

    @Override
    public void setMenuItems() {
        Block block = menuStorage.getBlock();
        Material statusMat = switch (block.getStatus()) {
            case DONE -> Material.LIME_CONCRETE;
            case DETAILING -> Material.YELLOW_CONCRETE;
            case BUILDING -> Material.ORANGE_CONCRETE;
            case RESERVED -> Material.LIGHT_BLUE_CONCRETE;
            default -> Material.RED_CONCRETE;
        };

        inventory.setItem(10, new Item(statusMat)
                .setDisplayName(ChatColor.GRAY + "Status: " + block.getStatus().getChatColor() + ChatColor.BOLD + block.getStatus().getName())
                .build());
        inventory.setItem(11, new Item(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build());
        inventory.setItem(12, new Item(Material.ENDER_PEARL).setDisplayName(ChatColor.AQUA + "Teleport").build());
        inventory.setItem(13, new Item(Material.SMITHING_TABLE)
                .setDisplayName(ChatColor.GRAY + "Progress: " + ProgressUtils.progressToColor(block.getProgress()) + ChatColor.BOLD + block.getProgress() + "%")
                .setLore(new ArrayList<>(Arrays.asList("", ChatColor.YELLOW + "Click to edit")))
                .build());
        inventory.setItem(14, new Item(block.isDetails() ? Material.ROSE_BUSH : Material.DEAD_BUSH)
                .setDisplayName(ChatColor.GRAY + "Details: " + (block.isDetails() ? ChatColor.GREEN + "✔" : ChatColor.RED + "✘"))
                .setLore(new ArrayList<>(Arrays.asList("", ChatColor.YELLOW + "Click to edit")))
                .build());

        ArrayList<String> loreBuilders = new ArrayList<>();

        if (block.getBuilders().isEmpty()) {
            loreBuilders.add("");
            loreBuilders.add(ChatColor.YELLOW + "Click to claim this block");
            loreBuilders.add(CustomColors.YELLOW.getChatColor() + "Right-Click to manage builders");
        } else {
            for (String builder : block.getBuilders()) {
                User user = User.getUserByName(builder);
                if (user == null) {
                    loreBuilders.add(ChatColor.GRAY + "- " + Rank.PLAYER.getColor() + builder);
                } else {
                    loreBuilders.add(ChatColor.GRAY + "- " + user.getRank().getColor() + user.getUsername());
                }
            }
            loreBuilders.add("");
            loreBuilders.add(ChatColor.YELLOW + "Click to manage builders");
        }

        inventory.setItem(15, new Item(Material.IRON_PICKAXE)
                .setDisplayName(ChatColor.GRAY + "Builders:" + (block.getBuilders().isEmpty() ? ChatColor.GOLD + "" + ChatColor.BOLD + " None" : ""))
                .setLore(loreBuilders)
                .hideAttributes(true)
                .build());
        inventory.setItem(16, new Item(Material.CLOCK)
                .setDisplayName(ChatColor.GRAY + "Completion Date: " + ChatColor.GOLD + "" + ChatColor.BOLD + (block.getCompletionDate() == null ? "None" : block.getCompletionDate()))
                .build());
    }
}
