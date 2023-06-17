package de.minefactprogress.progressplugin.menusystem.menus.bannercreator;

import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.entities.Banner;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.menusystem.PaginatedMenu;
import de.minefactprogress.progressplugin.utils.CustomColors;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BannerListMenu extends PaginatedMenu {

    private final String search;

    public BannerListMenu(MenuStorage menuStorage, Menu previousMenu) {
        this(menuStorage, previousMenu, null);
    }

    public BannerListMenu(MenuStorage menuStorage, Menu previousMenu, String search) {
        super(menuStorage, previousMenu);
        this.search = search;
    }

    @Override
    public Component menuName() {
        return Component.text("Banner Browser", CustomColors.BLUE.getComponentColor());
    }

    @Override
    public int rows() {
        return 6;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player p = (Player) e.getWhoClicked();

        if(item == null) return;

        if(item.getType().name().contains("BANNER")) {
            p.getInventory().addItem(item);
            p.closeInventory();
        }
    }

    @Override
    public void setMenuItems() {

    }

    @Override
    public List<ItemStack> items() {
        List<ItemStack> items = new ArrayList<>();

        for (Banner banner : API.getBanners()) {
            if (search != null) {
                int id;
                try {
                    id = Integer.parseInt(search);
                } catch (NumberFormatException ex) {
                    id = 0;
                }
                if (!banner.getName().toLowerCase().contains(search.toLowerCase()) && !(id > 0 && banner.getId() == id)) continue;
            }
            items.add(banner.toItemStack());
        }
        return items;
    }
}
