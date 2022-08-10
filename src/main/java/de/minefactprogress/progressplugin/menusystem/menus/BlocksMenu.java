package de.minefactprogress.progressplugin.menusystem.menus;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.Status;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.menusystem.PaginatedMenu;
import de.minefactprogress.progressplugin.utils.CustomColors;
import de.minefactprogress.progressplugin.utils.EnumUtils;
import de.minefactprogress.progressplugin.utils.Permissions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BlocksMenu extends PaginatedMenu {

    private final int filter, sorting;

    public BlocksMenu(MenuStorage menuStorage, Menu previousMenu, int filter, int sorting) {
        super(menuStorage, previousMenu);
        this.filter = filter;
        this.sorting = sorting;
    }

    @Override
    public Component menuName() {
        return Component.text(menuStorage.getDistrict().getName(), CustomColors.BLUE.getComponentColor());
    }

    @Override
    public int rows() {
        return 6;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if (item == null) return;

        switch (item.getType()) {
            case PAPER:
                int blockID = Integer.parseInt(PlainTextComponentSerializer.plainText().serialize(item.displayName()).replaceAll("\\D+", ""));
                Block block = Block.getBlock(menuStorage.getDistrict(), blockID);
                Location loc = block.getCenter();
                if (e.getClick().isRightClick() || !Permissions.isTeamMember(p)) {
                    if (loc != null) {
                        p.teleport(loc);
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                        p.sendMessage(Main.getPREFIX() + ChatColor.GRAY + "Teleported to Block " + ChatColor.YELLOW
                                + "#" + block.getId() + " " + ChatColor.GRAY + "of " + ChatColor.YELLOW + block.getDistrict().getName());
                    }
                } else if (e.getClick().isLeftClick()) {
                    menuStorage.setBlock(block);
                    new EditBlockMenu(menuStorage, this).open();
                }
                break;
            case HOPPER:
                if (e.getClick().isLeftClick()) {
                    new BlocksMenu(menuStorage, previousMenu, EnumUtils.getNextID(Status.class, filter, 1), sorting).open();
                } else if (e.getClick().isRightClick()) {
                    new BlocksMenu(menuStorage, previousMenu, EnumUtils.getPreviousID(Status.class, filter, 1), sorting).open();
                }
                break;
            case COMPARATOR:
                if (e.getClick().isLeftClick()) {
                    new BlocksMenu(menuStorage, previousMenu, filter, EnumUtils.getNextID(Block.Sorting.class, sorting)).open();
                } else if (e.getClick().isRightClick()) {
                    new BlocksMenu(menuStorage, previousMenu, filter, EnumUtils.getPreviousID(Block.Sorting.class, sorting)).open();
                }
                break;
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(46, getFilterItem(Status.class, filter));
        inventory.setItem(47, getSortingItem(Block.Sorting.class, sorting));
    }

    @Override
    public List<ItemStack> items() {
        ArrayList<ItemStack> items = new ArrayList<>();
        List<Block> blocks = Block.getBlocksOfDistrict(menuStorage.getDistrict());

        blocks.sort(EnumUtils.getByID(Block.Sorting.class, sorting));

        for (Block block : blocks) {
            if (filter != 0 && filter != block.getStatus().ordinal() + 1) continue;

            items.add(block.toItemStack(menuStorage.getOwner(), false));
        }

        return items;
    }
}
