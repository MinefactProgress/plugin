package de.minefactprogress.progressplugin.menusystem.menus;

import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.entities.users.Rank;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.menusystem.PaginatedMenu;
import de.minefactprogress.progressplugin.utils.Constants;
import de.minefactprogress.progressplugin.utils.CustomColors;
import de.minefactprogress.progressplugin.utils.EnumUtils;
import de.minefactprogress.progressplugin.utils.Permissions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
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
        ItemStack item = e.getCurrentItem();
        Player p = (Player) e.getWhoClicked();

        if(item == null) return;

        String playerName = PlainTextComponentSerializer.plainText().serialize(item.displayName()).replaceAll("[\\[|\\]]", "");
        Player target = Bukkit.getPlayer(playerName);
        switch (item.getType()) {
            case PLAYER_HEAD:
                if (e.isLeftClick()) {
                    menuStorage.setClaimsUser(User.getUserByName(playerName));
                    new ClaimsMenu(menuStorage, this).open();
                } else if (e.isRightClick() && target != null) {
                    if(target.getUniqueId().equals(p.getUniqueId())) {
                        p.sendMessage(Constants.PREFIX + ChatColor.RED + "You cannot teleport to yourself!");
                        return;
                    }
                    if (Permissions.isTeamMember(p)) {
                        p.teleport(target.getLocation());
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                    } else {
                        p.performCommand("tpa " + target.getName());
                    }
                }
                break;
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
        users.sort((user1, user2) -> {
            if (user1.isOnline() == user2.isOnline()) {
                return 0;
            } else if (user1.isOnline() && !user2.isOnline()) {
                return -1;
            }
            return 1;
        });

        for(User user : users) {
            if(filter != 0 && filter != user.getRank().ordinal() + 1) continue;

            items.add(user.toItemStack(Permissions.isTeamMember(menuStorage.getOwner())));
        }

        return items;
    }
}
