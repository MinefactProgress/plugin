package de.minefactprogress.progressplugin.menusystem.menus;

import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.entities.users.Rank;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.menusystem.PaginatedMenu;
import de.minefactprogress.progressplugin.utils.CustomColors;
import de.minefactprogress.progressplugin.utils.EnumUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class UsersMenu extends PaginatedMenu {

    private final int filter, sorting;

    public UsersMenu(MenuStorage menuStorage, Menu previousMenu, int filter, int sorting) {
        super(menuStorage, previousMenu);
        this.filter = filter;
        this.sorting = sorting;
    }

    @Override
    public Component menuName() {
        return Component.text("Builders", CustomColors.BLUE.getComponentColor());
    }

    @Override
    public int rows() {
        return 6;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = menuStorage.getOwner();
        ItemStack item = e.getCurrentItem();

        if(item == null) return;

        switch (item.getType()) {
            case HOPPER:
                if(e.getClick().isLeftClick()) {
                    new UsersMenu(menuStorage, previousMenu, EnumUtils.getNextID(Rank.class, filter, 1), sorting).open();
                } else if (e.getClick().isRightClick()) {
                    new UsersMenu(menuStorage, previousMenu, EnumUtils.getPreviousID(Rank.class, filter, 1), sorting).open();
                }
                break;
            case COMPARATOR:
                if(e.getClick().isLeftClick()) {
                    new UsersMenu(menuStorage, previousMenu, filter, EnumUtils.getNextID(User.Sorting.class, sorting)).open();
                } else if (e.getClick().isRightClick()) {
                    new UsersMenu(menuStorage, previousMenu, filter, EnumUtils.getPreviousID(User.Sorting.class, sorting)).open();
                }
                break;
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(46, getFilterItem(Rank.class, filter));
        inventory.setItem(47, getSortingItem(User.Sorting.class, sorting));
    }

    @Override
    public List<ItemStack> items() {
        ArrayList<ItemStack> items = new ArrayList<>();
        List<User> users = new ArrayList<>(API.getUsers().stream().filter(user -> user.getRank() != null && (user.isStaff() || user.countClaims() > 0)).toList());

        users.sort(EnumUtils.getByID(User.Sorting.class, sorting));

        for(User user : users) {
            if(filter != 0 && filter != user.getRank().ordinal() + 1) continue;

            items.add(user.toItemStack());
        }

        return items;
    }
}
