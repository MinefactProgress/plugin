package de.minefactprogress.progressplugin.menusystem.menus;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.entities.city.District;
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

import java.util.List;

public class NewYorkCityMenu extends Menu {

    private static final String NAME_SETTINGS = ChatColor.GOLD + "Settings";

    public NewYorkCityMenu(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
    }

    @Override
    public Component menuName() {
        return Component.text("New York City", CustomColors.BLUE.getComponentColor());
    }

    @Override
    public int rows() {
        return 3;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();

        if (item == null) return;

        String itemName = PlainTextComponentSerializer.plainText().serializeOrNull(item.getItemMeta().displayName());

        if (item.getType() == Material.BOOKSHELF) {
            menuStorage.setBorough(District.getDistrictByName(PlainTextComponentSerializer.plainText().serializeOrNull(item.getItemMeta().displayName())));
            new BoroughsMenu(menuStorage, this).open();
        } else if (item.getType() == Material.PLAYER_HEAD && itemName != null && itemName.equals(ChatColor.stripColor(NAME_SETTINGS))) {
            new SettingsMenu(menuStorage, this).open();
            return;
        }
    }

    @Override
    public void setMenuItems() {
        List<District> districts = District.getChildren("New York City");
        int size = districts.size();
        int steps = size > 4 ? 1 : 2;
        int index = size % 2 == 0 ? (13 - (size - 1)) : (13 - ((size / 2) * 2));

        if (index < 10) {
            index = 11 - (size > 5 ? 1 : 0);
        }

        for (District district : districts) {
            inventory.setItem(index, district.toItemStack());
            index += steps;
        }

        inventory.setItem(slots() - 4, Item.createCustomHead("26110", NAME_SETTINGS, null));
    }
}
