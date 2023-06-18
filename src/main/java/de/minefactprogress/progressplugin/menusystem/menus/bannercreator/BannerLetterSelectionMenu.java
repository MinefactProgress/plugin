package de.minefactprogress.progressplugin.menusystem.menus.bannercreator;

import de.minefactprogress.progressplugin.components.BannerHandler;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.utils.CustomColors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BannerLetterSelectionMenu extends Menu {

    private final String NAME_BASE_COLOR = "Base Color";
    private final String NAME_PATTERN_COLOR = "Letter Color";

    public BannerLetterSelectionMenu(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
    }

    @Override
    public Component menuName() {
        return Component.text("Select Letter");
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

        String itemName = PlainTextComponentSerializer.plainText().serializeOrNull(item.getItemMeta().displayName());

        if (itemName == null) return;

        if (item.getType().name().contains("BANNER")) {
            try {
                BannerHandler.Letter letter = BannerHandler.Letter.valueOf(ChatColor.stripColor(itemName));
                p.getInventory().addItem(letter.getAsItemStack(menuStorage.getLetterBaseColor(), menuStorage.getLetterPatternColor()));
            } catch (IllegalArgumentException ignored) {}
        } else if (item.getType().name().contains("DYE")) {
            new BannerColorMenu(menuStorage, this, itemName.equals(NAME_BASE_COLOR)).open();
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(37, borderItem());
        inventory.setItem(43, borderItem());
        inventory.setItem(52, BannerHandler.createDyeWithLore(Component.text(NAME_BASE_COLOR, CustomColors.BLUE.getComponentColor()), menuStorage.getLetterBaseColor(), menuStorage.getLetterBaseColor().name().charAt(0) + menuStorage.getLetterBaseColor().name().substring(1).replace("_", " ").toLowerCase()));
        inventory.setItem(53, BannerHandler.createDyeWithLore(Component.text(NAME_PATTERN_COLOR, CustomColors.BLUE.getComponentColor()), menuStorage.getLetterPatternColor(), menuStorage.getLetterPatternColor().name().charAt(0) + menuStorage.getLetterPatternColor().name().substring(1).replace("_", " ").toLowerCase()));

        for (BannerHandler.Letter letter : BannerHandler.Letter.values()) {
            inventory.addItem(letter.getAsItemStack(menuStorage.getLetterBaseColor(), menuStorage.getLetterPatternColor()));
        }
    }
}
