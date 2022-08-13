package de.minefactprogress.progressplugin.listeners;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.PaginatedMenu;
import de.minefactprogress.progressplugin.menusystem.menus.NewYorkCityMenu;
import de.minefactprogress.progressplugin.menusystem.menus.SettingsMenu;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getItemMeta() == null) return;
        if (e.getClickedInventory() == null) return;

        ItemStack item = e.getCurrentItem();
        InventoryHolder holder = e.getClickedInventory().getHolder();
        String itemName = PlainTextComponentSerializer.plainText().serializeOrNull(item.getItemMeta().displayName());

        if (holder instanceof Menu menu) {
            e.setCancelled(true);

            if (item.getType() == Material.BARRIER && itemName != null && itemName.equals(ChatColor.stripColor(Menu.NAME_CLOSE))) {
                p.closeInventory();
                return;
            } else if (item.getType() == Material.PLAYER_HEAD) {
                if (itemName != null && itemName.equals(ChatColor.stripColor(Menu.NAME_BACK))) {
                    menu.getPreviousMenu().open();
                    return;
                }
                if (itemName != null && itemName.equals(ChatColor.stripColor(Menu.NAME_SETTINGS))) {
                    new SettingsMenu(Main.getInstance().getMenuStorage(p),menu).open();
                    return;
                }
                if (menu instanceof PaginatedMenu paginatedMenu) {
                    if (itemName != null && itemName.equals(ChatColor.stripColor(PaginatedMenu.NAME_NEXT))) {
                        paginatedMenu.nextPage();
                        return;
                    } else if (itemName != null && itemName.equals(ChatColor.stripColor(PaginatedMenu.NAME_PREVIOUS))) {
                        paginatedMenu.previousPage();
                        return;
                    }
                }
            }
            menu.handleMenu(e);
        }
    }
}
