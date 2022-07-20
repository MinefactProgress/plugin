package de.minefactprogress.progressplugin.menusystem;

import de.minefactprogress.progressplugin.utils.Item;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ConfirmMenu extends Menu {

    public ConfirmMenu(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
    }

    @Override
    public String menuName() {
        return "§cUpdate this block?";
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

    public abstract void handleConfirm();

    public abstract void handleCancel();
}
