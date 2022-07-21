package de.minefactprogress.progressplugin.menusystem.menus;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.District;
import de.minefactprogress.progressplugin.entities.city.Status;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.menusystem.PaginatedMenu;
import de.minefactprogress.progressplugin.utils.CustomColors;
import de.minefactprogress.progressplugin.utils.EnumUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DistrictsMenu extends PaginatedMenu {

    private final int filter, sorting;

    public DistrictsMenu(MenuStorage menuStorage, Menu previousMenu, int filter, int sorting) {
        super(menuStorage, previousMenu);
        this.filter = filter;
        this.sorting = sorting;
    }

    @Override
    public Component menuName() {
        return Component.text(menuStorage.getSubborough().getName(), CustomColors.BLUE.getComponentColor());
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

        String districtName = PlainTextComponentSerializer.plainText().serializeOrNull(item.getItemMeta().displayName());
        District district = District.getDistrictByName(districtName);

        switch (item.getType()) {
            case BOOK:
                if (e.getClick().isLeftClick()) {
                    if (Block.getBlocksOfDistrict(district).isEmpty()) {
                        p.sendMessage(Main.getPREFIX() + ChatColor.RED + "This district has no data!");
                        return;
                    }
                    menuStorage.setDistrict(district);
                    new BlocksMenu(menuStorage, this).open();
                } else if (e.getClick().isRightClick()) {
                    if (district.getCenter() != null) {
                        p.teleport(district.getCenter());
                        p.sendMessage(Main.getPREFIX() + ChatColor.GRAY + "You got teleported to " + ChatColor.YELLOW + district.getName());
                    } else {
                        p.sendMessage(Main.getPREFIX() + ChatColor.RED + "No location found for this district!");
                    }
                }
                break;
            case HOPPER:
                if (e.getClick().isLeftClick()) {
                    new DistrictsMenu(menuStorage, previousMenu, EnumUtils.getNextID(Status.class, filter, 1), sorting).open();
                } else if (e.getClick().isRightClick()) {
                    new DistrictsMenu(menuStorage, previousMenu, EnumUtils.getPreviousID(Status.class, filter, 1), sorting).open();
                }
                break;
            case COMPARATOR:
                if (e.getClick().isLeftClick()) {
                    new DistrictsMenu(menuStorage, previousMenu, filter, EnumUtils.getNextID(District.Sorting.class, sorting)).open();
                } else if (e.getClick().isRightClick()) {
                    new DistrictsMenu(menuStorage, previousMenu, filter, EnumUtils.getPreviousID(District.Sorting.class, sorting)).open();
                }
                break;
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(46, getFilterItem(Status.class, filter));
        inventory.setItem(47, getSortingItem(District.Sorting.class, sorting));
    }

    @Override
    public List<ItemStack> items() {
        ArrayList<ItemStack> items = new ArrayList<>();
        List<District> districts = District.getChildren(menuStorage.getSubborough().getName());

        districts.sort(EnumUtils.getByID(District.Sorting.class, sorting));

        for (District district : districts) {
            if (filter != 0 && filter != district.getStatus().ordinal() + 1) continue;

            items.add(district.toItemStack());
        }

        return items;
    }
}
