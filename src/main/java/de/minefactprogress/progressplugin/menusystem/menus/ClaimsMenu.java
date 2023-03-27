package de.minefactprogress.progressplugin.menusystem.menus;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.District;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.menusystem.PaginatedMenu;
import de.minefactprogress.progressplugin.utils.CustomColors;
import de.minefactprogress.progressplugin.utils.Item;
import de.minefactprogress.progressplugin.utils.Permissions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ClaimsMenu extends PaginatedMenu {

    public ClaimsMenu(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
    }

    @Override
    public Component menuName() {
        return Component.text("Claims by " + menuStorage.getClaimsUser().getRank().getColor() + menuStorage.getClaimsUser().getUsername(), CustomColors.BLUE.getComponentColor());
    }

    @Override
    public int rows() {
        return 6;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if(item == null) return;

        if(item.getType() == Material.PLAYER_HEAD) {
            String itemName = PlainTextComponentSerializer.plainText().serialize(item.displayName()).replaceAll("[\\[|\\]]", "");

            if(!itemName.contains(" #")) return;

            District district = District.getDistrictByName(itemName.split(" #")[0]);
            int blockID = Integer.parseInt(itemName.replaceAll("\\D+", ""));
            Block block = Block.getBlock(district, blockID);
            if (e.getClick().isRightClick() || !Permissions.isTeamMember(p)) {
                Location loc = block.getCenter();
                if (loc != null) {
                    p.teleport(loc);
                    p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                    p.sendMessage(Main.getPREFIX() + ChatColor.GRAY + "Teleported to Block " + ChatColor.YELLOW
                            + "#" + block.getId() + " " + ChatColor.GRAY + "of " + ChatColor.YELLOW + block.getDistrict().getName());
                }
            } else if (e.getClick().isLeftClick()) {
                menuStorage.setDistrict(district);
                menuStorage.setBlock(block);
                new EditBlockMenu(menuStorage, this).open();
            }
        }
    }

    @Override
    public void setMenuItems() {
        if(menuStorage.getClaimsUser().countClaims() == 0) {
            inventory.setItem(22, Item.create(Material.BARRIER, ChatColor.RED + "No claims"));
        }
    }

    @Override
    public List<ItemStack> items() {
        List<ItemStack> items = new ArrayList<>();

        for(Block block : API.getBlocks()) {
            if (block.getBuilders().contains(menuStorage.getClaimsUser().getUsername())) {
                ItemStack item = block.toItemStack(menuStorage.getOwner());
                items.add(Item.edit(item, ChatColor.AQUA + block.getDistrict().getName() + " #" + block.getId()));
            }
        }

        return items;
    }
}
