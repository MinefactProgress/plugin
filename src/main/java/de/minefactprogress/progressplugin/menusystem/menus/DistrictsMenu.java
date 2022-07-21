package de.minefactprogress.progressplugin.menusystem.menus;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.District;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.menusystem.PaginatedMenu;
import de.minefactprogress.progressplugin.utils.CustomColors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
        Player p = menuStorage.getOwner();
        ItemStack item = e.getCurrentItem();

        if (item == null) return;

        switch (item.getType()) {
            case BOOK:
                if (e.getClick().isLeftClick()) {
                    String districtName = PlainTextComponentSerializer.plainText().serializeOrNull(item.getItemMeta().displayName());
                    District district = District.getDistrictByName(districtName);
                    if (Block.getBlocksOfDistrict(district).isEmpty()) {
                        p.sendMessage(Component.text(Main.getPREFIX()).append(Component.text("This district has no data!", NamedTextColor.RED)));
                        return;
                    }
                    menuStorage.setDistrict(district);
                    new BlocksMenu(menuStorage, this).open();
                } else if (e.getClick().isRightClick()) {
                    // TODO Teleport
                }
                break;
            case HOPPER:
                // TODO Filter
                break;
            case COMPARATOR:
                // TODO Sorting
                break;
        }
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
