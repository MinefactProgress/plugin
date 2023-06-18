package de.minefactprogress.progressplugin.menusystem.menus.bannercreator;

import de.minefactprogress.progressplugin.components.BannerHandler;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.utils.CustomColors;
import de.minefactprogress.progressplugin.utils.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class BannerBaseMenu extends Menu {

    private final String BANNER_BROWSER_NAME = ChatColor.AQUA + "Browse Banners";
    private final String BANNER_LETTER_GENERATOR_NAME = ChatColor.GOLD + "Letter Generator";

    public BannerBaseMenu(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
    }

    @Override
    public Component menuName() {
        return Component.text("Select the base color");
    }

    @Override
    public int rows() {
        return 5;
    }

    @Override
    public ItemStack titleItem() {
        return null;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();

        if(item == null) return;

        if(item.getType().name().contains("BANNER")) {
            menuStorage.setBanner(item);
            new BannerColorMenu(menuStorage, null).open();
        }

        String itemName = PlainTextComponentSerializer.plainText().serializeOrNull(item.getItemMeta().displayName());

        if (itemName == null) return;

        if (item.getType() == Material.PLAYER_HEAD) {
            if (itemName.equals(ChatColor.stripColor(BANNER_BROWSER_NAME))) {
                new BannerListMenu(menuStorage, this).open();
            } else if (itemName.equals(ChatColor.stripColor(BANNER_LETTER_GENERATOR_NAME))) {
                if (menuStorage.getLetterBaseColor() == null)
                    menuStorage.setLetterBaseColor(DyeColor.WHITE);
                if (menuStorage.getLetterPatternColor() == null)
                    menuStorage.setLetterPatternColor(DyeColor.BLACK);
                new BannerLetterSelectionMenu(menuStorage, this).open();
            }
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(0, Item.createCustomHead("9297", BANNER_LETTER_GENERATOR_NAME, new ArrayList<>(Arrays.asList("", ChatColor.YELLOW + "Click to open letter generator"))));
        inventory.setItem(8, Item.createCustomHead("2669", BANNER_BROWSER_NAME, new ArrayList<>(Arrays.asList("", ChatColor.YELLOW + "Click to open banner browser"))));
        for(DyeColor color : DyeColor.values()) {
            inventory.addItem(BannerHandler.createBanner(Component.text(color.name().charAt(0) + color.name().substring(1).replace("_", " ").toLowerCase() + " Banner",
                    CustomColors.of(String.format("#%02x%02x%02x", color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue()))), color, new ArrayList<>()));
        }
    }
}
