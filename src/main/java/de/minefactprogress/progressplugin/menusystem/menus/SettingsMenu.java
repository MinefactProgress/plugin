package de.minefactprogress.progressplugin.menusystem.menus;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.utils.Constants;
import de.minefactprogress.progressplugin.utils.CustomColors;
import de.minefactprogress.progressplugin.utils.Item;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsMenu extends Menu {

    public SettingsMenu(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
    }

    private static final ItemStack ITEM_ENABLED = new Item(Material.LIME_STAINED_GLASS_PANE).setDisplayName(ChatColor.GREEN+"Enabled").build();
    private static final ItemStack ITEM_DISABLED = new Item(Material.RED_STAINED_GLASS_PANE).setDisplayName(ChatColor.RED+"Disabled").build();

    @Override
    public Component menuName() {
        return Component.text("Settings", CustomColors.BLUE.getComponentColor());
    }

    @Override
    public int rows() {
        return 4;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();

        if (item == null) return;

        if(item.getType() == Material.OAK_SIGN) {
            menuStorage.getOwner().sendMessage(Main.getPREFIX()+ ChatColor.GRAY+"Toggled the District Bossbar");
            Main.getDistrictBossbar().togglePlayer(menuStorage.getOwner());
            inventory.setItem(10 +9, Main.getDistrictBossbar().getBars().containsKey(menuStorage.getOwner().getUniqueId())?ITEM_ENABLED:ITEM_DISABLED);
        } else if (item.getType() == Material.REDSTONE_TORCH) {
            menuStorage.getOwner().sendMessage(Constants.PREFIX + ChatColor.GRAY + "Toggled debug mode");
            menuStorage.getUser().getSettings().setDebugMode(!menuStorage.getUser().getSettings().isDebugMode());
            inventory.setItem(12 + 9, menuStorage.getUser().getSettings().isDebugMode() ? ITEM_ENABLED : ITEM_DISABLED);
        }
    }

    @Override
    public void setMenuItems() {
        // District Bossbar
        inventory.setItem(10, new Item(Material.OAK_SIGN).setDisplayName(ChatColor.AQUA+"District Bar").setLore(new ArrayList<>(
                Arrays.asList(ChatColor.GRAY+"Displays a BossBar on the",ChatColor.GRAY+"top of the screen showing",ChatColor.GRAY+"the District you are in"))).build());
        inventory.setItem(10 +9, Main.getDistrictBossbar().getBars().containsKey(menuStorage.getOwner().getUniqueId())?ITEM_ENABLED:ITEM_DISABLED);

        // Debug Mode
        if(menuStorage.getUser().hasDebugPerms()) {
            inventory.setItem(12, new Item(Material.REDSTONE_TORCH).setDisplayName(ChatColor.RED+"Debug Mode").setLore(new ArrayList<>(
                    List.of(ChatColor.GRAY + "Displays debug messages in the chat"))).build());
            inventory.setItem(12 + 9, menuStorage.getUser().getSettings().isDebugMode() ? ITEM_ENABLED : ITEM_DISABLED);
        }
    }
}
