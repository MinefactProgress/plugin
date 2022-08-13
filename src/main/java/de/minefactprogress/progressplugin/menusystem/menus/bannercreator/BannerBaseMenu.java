package de.minefactprogress.progressplugin.menusystem.menus.bannercreator;

import de.minefactprogress.progressplugin.components.BannerHandler;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.utils.Item;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class BannerBaseMenu extends Menu {

    public BannerBaseMenu(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
    }

    @Override
    public Component menuName() {
        return Component.text("Select the base color");
    }

    @Override
    public int rows() {
        return 5;
    }

    @Override
    public ItemStack titleItem() {
        return null;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();

        if(item == null) return;

        if(item.getType().name().contains("BANNER")) {
            menuStorage.setBanner(item);
            new BannerColorMenu(menuStorage, null).open();
        }
    }

    @Override
    public void setMenuItems() {
        for(DyeColor color : DyeColor.values()) {
            inventory.addItem(BannerHandler.createBanner(null, color, new ArrayList<>()));
        }
    }
}
