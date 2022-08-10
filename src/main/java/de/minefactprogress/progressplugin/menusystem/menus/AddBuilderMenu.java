package de.minefactprogress.progressplugin.menusystem.menus;

import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.menusystem.PaginatedMenu;
import de.minefactprogress.progressplugin.utils.CustomColors;
import de.minefactprogress.progressplugin.utils.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AddBuilderMenu extends PaginatedMenu {

    public AddBuilderMenu(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
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

        if (item.getType() == Material.PLAYER_HEAD) {
            // Add other player as builder
            String otherName = PlainTextComponentSerializer.plainText().serializeOrNull(item.getItemMeta().displayName());

            menuStorage.getBlock().setBuilder(otherName, p, true);
            p.closeInventory();
        }
    }

    @Override
    public void setMenuItems() {

    }

    @Override
    public List<ItemStack> items() {
        ArrayList<ItemStack> items = new ArrayList<>();
        Block block = menuStorage.getBlock();

        for(User user : User.users) {
            if(block.getBuilders().contains(user.getName())) continue;

            ArrayList<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.YELLOW + "Click to add");

            items.add(Item.createPlayerHead(user.getRank().getColor() + user.getName(), user.getName(), lore));
        }

        return items;
    }
}
