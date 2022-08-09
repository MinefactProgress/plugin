package de.minefactprogress.progressplugin.menusystem.menus;

import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.users.Rank;
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

public class BuilderManagerMenu extends PaginatedMenu {

    private final String NAME_ADD_BUILDER = ChatColor.GREEN + "Add a new Builder";

    public BuilderManagerMenu(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
    }

    @Override
    public Component menuName() {
        return Component.text("Manage Builders", CustomColors.BLUE.getComponentColor());
    }

    @Override
    public int rows() {
        return 3;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = menuStorage.getOwner();
        ItemStack item = e.getCurrentItem();

        if(item == null) return;

        if(item.getType() == Material.PLAYER_HEAD) {
            String itemName = PlainTextComponentSerializer.plainText().serializeOrNull(item.getItemMeta().displayName());

            if(itemName == null) return;

            if(itemName.equals(NAME_ADD_BUILDER)) {
                // TODO
            } else {
                // Remove builder
                menuStorage.getBlock().setBuilder(itemName, p, false);
                p.closeInventory();
            }
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(19, Item.createCustomHead("9885", NAME_ADD_BUILDER, null));
    }

    @Override
    public List<ItemStack> items() {
        Block block = menuStorage.getBlock();
        ArrayList<ItemStack> items = new ArrayList<>();

        for(String builder : block.getBuilders()) {
            User user = User.getByName(builder);

            ArrayList<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.YELLOW + "Click to remove builder");

            if(user != null) {
                items.add(Item.createPlayerHead(user.getRank().getColor() + user.getName(), user.getName(), lore));
            } else {
                items.add(Item.createPlayerHead(Rank.PLAYER.getColor() + builder, builder, lore));
            }
        }

        return items;
    }
}
