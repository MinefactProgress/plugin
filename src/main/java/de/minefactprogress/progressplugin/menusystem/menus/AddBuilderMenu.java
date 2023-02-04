package de.minefactprogress.progressplugin.menusystem.menus;

import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.users.Rank;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.menusystem.PaginatedMenu;
import de.minefactprogress.progressplugin.utils.CustomColors;
import de.minefactprogress.progressplugin.utils.EnumUtils;
import de.minefactprogress.progressplugin.utils.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AddBuilderMenu extends PaginatedMenu {

    private final int filter, sorting;

    public AddBuilderMenu(MenuStorage menuStorage, Menu previousMenu, int filter, int sorting) {
        super(menuStorage, previousMenu);
        this.filter = filter;
        this.sorting = sorting;
    }

    @Override
    public Component menuName() {
        return Component.text("Add a new builder", CustomColors.BLUE.getComponentColor());
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
            case PLAYER_HEAD:
                // Add other player as builder
                String otherName = PlainTextComponentSerializer.plainText().serializeOrNull(item.getItemMeta().displayName());

                menuStorage.getBlock().setBuilder(otherName, p, true);
                p.closeInventory();
                break;
            case HOPPER:
                if (e.getClick().isLeftClick()) {
                    new AddBuilderMenu(menuStorage, previousMenu, EnumUtils.getNextID(Rank.class, filter, 1), sorting).open();
                } else if (e.getClick().isRightClick()) {
                    new AddBuilderMenu(menuStorage, previousMenu, EnumUtils.getPreviousID(Rank.class, filter, 1), sorting).open();
                }
                break;
            case COMPARATOR:
                if (e.getClick().isLeftClick()) {
                    new AddBuilderMenu(menuStorage, previousMenu, filter, EnumUtils.getNextID(User.Sorting.class, sorting)).open();
                } else if (e.getClick().isRightClick()) {
                    new AddBuilderMenu(menuStorage, previousMenu, filter, EnumUtils.getPreviousID(User.Sorting.class, sorting)).open();
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
        ArrayList<User> users = new ArrayList<>();
        ArrayList<ItemStack> items = new ArrayList<>();
        Block block = menuStorage.getBlock();

        for (User user : API.getUsers()) {
            if(!block.getBuilders().contains(user.getUsername())) {
                users.add(user);
            }
        }

        users.sort(EnumUtils.getByID(User.Sorting.class, sorting));

        for(User user : users) {
            if(filter != 0 && filter != user.getRank().ordinal() + 1) continue;

            ArrayList<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.YELLOW + "Click to add");

            items.add(Item.createPlayerHead(user.getRank().getColor() + user.getUsername(), user.getUsername(), lore));
        }

        return items;
    }
}
