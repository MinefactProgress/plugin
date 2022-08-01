package de.minefactprogress.progressplugin.menusystem.menus;

import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.users.Rank;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.utils.CustomColors;
import de.minefactprogress.progressplugin.utils.Item;
import de.minefactprogress.progressplugin.utils.ProgressUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
        return menuStorage.getBlock().toItemStack();
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

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
                User user = User.getByName(builder);
                if (user == null) {
                    loreBuilders.add(ChatColor.GRAY + "- " + Rank.PLAYER.getColor() + builder);
                } else {
                    loreBuilders.add(ChatColor.GRAY + "- " + user.getRank().getColor() + user.getName());
                }
            }
            loreBuilders.add("");
            loreBuilders.add(ChatColor.YELLOW + "Click to manage builders");
        }

        inventory.setItem(15, new Item(Material.IRON_PICKAXE)
                .setDisplayName(ChatColor.GRAY + "Builders:" + (block.getBuilders().isEmpty() ? ChatColor.GOLD + "" + ChatColor.BOLD + "None" : ""))
                .setLore(loreBuilders)
                .hideAttributes(true)
                .build());
        inventory.setItem(16, new Item(Material.CLOCK)
                .setDisplayName(ChatColor.GRAY + "Completion Date: " + ChatColor.GOLD + "" + ChatColor.BOLD + (block.getDate() == null ? "None" : block.getDate()))
                .build());
    }
}
