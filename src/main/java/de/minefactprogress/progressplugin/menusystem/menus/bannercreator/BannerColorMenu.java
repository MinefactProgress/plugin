package de.minefactprogress.progressplugin.menusystem.menus.bannercreator;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.components.BannerHandler;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.utils.CustomColors;
import de.minefactprogress.progressplugin.utils.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BannerColorMenu extends Menu {

    private final String resetName = ChatColor.RED + "Reset Banner";
    private final String createName = ChatColor.GREEN + "Create Banner";

    public BannerColorMenu(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
    }

    @Override
    public Component menuName() {
        return Component.text("Select Pattern Color");
    }

    @Override
    public int rows() {
        return 5;
    }

    @Override
    public ItemStack titleItem() {
        return menuStorage.getBanner();
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = menuStorage.getOwner();
        ItemStack item = e.getCurrentItem();

        if(item == null) return;

        String itemName = PlainTextComponentSerializer.plainText().serializeOrNull(item.getItemMeta().displayName());

        if(itemName == null) return;

        if(item.getType() == Material.PLAYER_HEAD) {
            if(itemName.equals(ChatColor.stripColor(resetName))) {
                menuStorage.setBanner(null);
                new BannerBaseMenu(menuStorage, null).open();
            } else if(itemName.equals(ChatColor.stripColor(createName))) {
                p.getInventory().addItem(menuStorage.getBanner());
                p.closeInventory();
                p.sendMessage(Main.getPREFIX_SERVER() + ChatColor.GRAY + "Banner created successfully");
            }
        } else if(item.getType().name().contains("DYE")) {
            menuStorage.setPatternColor(BannerHandler.getDyeColorByName(itemName));
            new BannerPatternMenu(menuStorage, this).open();
        }
    }

    @Override
    public void setMenuItems() {
        for(DyeColor dyeColor : DyeColor.values()) {
            Color color = dyeColor.getColor();
            inventory.addItem(BannerHandler.createDye(Component.text(dyeColor.name(), CustomColors.of(String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()))), dyeColor));
        }

        inventory.setItem(2, Item.createCustomHead("9382", resetName, null));
        inventory.setItem(6, Item.createCustomHead("21771", createName, null));
    }
}
