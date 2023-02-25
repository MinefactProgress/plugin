package de.minefactprogress.progressplugin.menusystem.menus;

import de.minefactprogress.progressplugin.entities.city.District;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.utils.CustomColors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BoroughsMenu extends Menu {

    public BoroughsMenu(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
    }

    @Override
    public Component menuName() {
        return Component.text(menuStorage.getBorough().getName(), CustomColors.BLUE.getComponentColor());
    }

    @Override
    public int rows() {
        return 3;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();

        if (item == null) return;

        if (item.getType() == Material.LECTERN) {
            menuStorage.setSubborough(District.getDistrictByName(PlainTextComponentSerializer.plainText().serializeOrNull(item.getItemMeta().displayName())));
            new DistrictsMenu(menuStorage, this, 0, 0).open();
        }
    }

    @Override
    public void setMenuItems() {
        List<District> districts = District.getChildren(menuStorage.getBorough());
        for (int i = 10; i <= 16 && (i - 10) < districts.size(); i++) {
            inventory.setItem(i, districts.get(i - 10).toItemStack());
        }
    }
}
