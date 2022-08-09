package de.minefactprogress.progressplugin.menusystem;

import de.minefactprogress.progressplugin.utils.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ConfirmMenu extends Menu {

    public ConfirmMenu(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
    }

    @Override
    public Component menuName() {
        return Component.text("Update this block?", NamedTextColor.RED);
    }

    @Override
    public int rows() {
        return 3;
    }

    @Override
    public ItemStack titleItem() {
        return Item.createCustomHead("11229", "§eConfirm update", null);
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();

        if (item == null) return;

        if (item.getType() == Material.LIME_CONCRETE) {
            handleConfirm();
        } else if (item.getType() == Material.RED_CONCRETE) {
            handleCancel();
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(12, new Item(Material.LIME_CONCRETE).setDisplayName(ChatColor.GREEN + "Confirm").build());
        inventory.setItem(14, new Item(Material.RED_CONCRETE).setDisplayName(ChatColor.RED + "Cancel").build());
    }

    public abstract void handleConfirm();

    public abstract void handleCancel();
}
