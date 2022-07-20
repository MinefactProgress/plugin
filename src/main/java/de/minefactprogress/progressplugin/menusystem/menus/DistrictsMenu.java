package de.minefactprogress.progressplugin.menusystem.menus;

import de.minefactprogress.progressplugin.entities.city.District;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.menusystem.PaginatedMenu;
import de.minefactprogress.progressplugin.utils.CustomColors;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DistrictsMenu extends PaginatedMenu {

    public DistrictsMenu(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
    }

    @Override
    public Component menuName() {
        return Component.text(menuStorage.getSubborough().getName(), CustomColors.BLUE.getColor());
    }

    @Override
    public int rows() {
        return 6;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

    }

    @Override
    public void setMenuItems() {

    }

    @Override
    public List<ItemStack> items() {
        ArrayList<ItemStack> items = new ArrayList<>();
        List<District> districts = District.getChildren(menuStorage.getSubborough().getName());

        for (District district : districts) {
            items.add(district.toItemStack());
        }

        return items;
    }
}
