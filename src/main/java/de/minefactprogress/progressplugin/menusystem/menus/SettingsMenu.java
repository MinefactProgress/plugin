package de.minefactprogress.progressplugin.menusystem.menus;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.entities.users.SettingType;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.utils.CustomColors;
import de.minefactprogress.progressplugin.utils.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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

        if (item == null || e.getClickedInventory() == null) return;

        if(item.getType() == Material.LIME_STAINED_GLASS_PANE || item.getType() == Material.RED_STAINED_GLASS_PANE) {
            ItemStack settingItem = e.getClickedInventory().getItem(e.getSlot()-9);

            if(settingItem == null) return;

            String settingName = PlainTextComponentSerializer.plainText().serialize(settingItem.displayName()).replace("[", "").replace("]", "");
            menuStorage.getUser().setSetting(SettingType.getByName(settingName), String.valueOf(item.getType() == Material.RED_STAINED_GLASS_PANE));
            menuStorage.getOwner().closeInventory();

            if(settingName.equals(SettingType.MINECRAFT_DISTRICT_BAR.getName())) {
                Main.getDistrictBossbar().togglePlayer(menuStorage.getOwner());
            }
        }
    }

    @Override
    public void setMenuItems() {
        User user = menuStorage.getUser();
        // District Bossbar
        inventory.setItem(10, new Item(Material.OAK_SIGN).setDisplayName(ChatColor.AQUA+SettingType.MINECRAFT_DISTRICT_BAR.getName()).setLore(new ArrayList<>(
                Arrays.asList(ChatColor.GRAY+"Displays a BossBar on the",ChatColor.GRAY+"top of the screen showing",ChatColor.GRAY+"the District you are in"))).build());
        inventory.setItem(10 + 9, user.getSetting(SettingType.MINECRAFT_DISTRICT_BAR).equals("true") ?ITEM_ENABLED : ITEM_DISABLED);

        // Show On Map
        inventory.setItem(11, new Item(Material.WRITABLE_BOOK).setDisplayName(ChatColor.AQUA+SettingType.MINECRAFT_MAP_VISIBLE.getName()).setLore(new ArrayList<>(
                Arrays.asList(ChatColor.GRAY+"Show your character",ChatColor.GRAY+"on the web-map"))).build());
        inventory.setItem(11 + 9, user.getSetting(SettingType.MINECRAFT_MAP_VISIBLE).equals("true") ?ITEM_ENABLED : ITEM_DISABLED);

        // Debug Mode
        if(user.hasDebugPerms()) {
            inventory.setItem(16, new Item(Material.REDSTONE_TORCH).setDisplayName(ChatColor.RED+SettingType.MINECRAFT_DEBUG_MODE.getName()).setLore(new ArrayList<>(
                    List.of(ChatColor.GRAY + "Displays debug messages in the chat"))).build());
            inventory.setItem(16 + 9, user.getSetting(SettingType.MINECRAFT_DEBUG_MODE).equals("true") ? ITEM_ENABLED : ITEM_DISABLED);
        }
    }
}
