package de.minefactprogress.progressplugin.menusystem.menus.bannercreator;

import de.minefactprogress.progressplugin.components.BannerHandler;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.menusystem.PaginatedMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BannerPatternMenu extends PaginatedMenu {

    public BannerPatternMenu(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
    }

    @Override
    public Component menuName() {
        return Component.text("Select Pattern");
    }

    @Override
    public int rows() {
        return 6;
    }

    @Override
    public ItemStack titleItem() {
        return menuStorage.getBanner();
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

    }

    @Override
    public List<ItemStack> items() {
        ArrayList<ItemStack> items = new ArrayList<>();

        for(PatternType pType : PatternType.values()) {
            if(pType == PatternType.BASE) continue;
            items.add(BannerHandler.addPattern(menuStorage.getBanner(), new Pattern(menuStorage.getPatternColor(), pType)));
        }

        return items;
    }
}
