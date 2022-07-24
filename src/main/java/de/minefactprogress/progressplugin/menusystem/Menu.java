package de.minefactprogress.progressplugin.menusystem;

import de.minefactprogress.progressplugin.api.RequestHandler;
import de.minefactprogress.progressplugin.utils.CustomColors;
import de.minefactprogress.progressplugin.utils.Item;
import de.minefactprogress.progressplugin.utils.time.TimeCalculator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

@RequiredArgsConstructor
public abstract class Menu implements InventoryHolder {

    public static final String NAME_BACK = ChatColor.GOLD + "Back";
    public static final String NAME_CLOSE = ChatColor.RED + "Close";
    public static final String NAME_SETTINGS = ChatColor.GOLD + "Settings";

    protected final MenuStorage menuStorage;
    @Getter
    protected final Menu previousMenu;
    @Getter
    protected Inventory inventory;

    public abstract Component menuName();

    public abstract int rows();

    public ItemStack titleItem() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Districts: " + ChatColor.YELLOW + new TimeCalculator(RequestHandler.getInstance().getLastUpdatedDistricts()).formatTimeAgo());
        lore.add(ChatColor.GRAY + "Blocks: " + ChatColor.YELLOW + new TimeCalculator(RequestHandler.getInstance().getLastUpdatedBlocks()).formatTimeAgo());

        return new Item(Material.CLOCK).setDisplayName("§bLast updated:").setLore(lore).build();
    }

    public abstract void handleMenu(InventoryClickEvent e);

    public abstract void setMenuItems();

    public final int slots() {
        return rows() * 9;
    }

    protected void build() {
        this.inventory = Bukkit.createInventory(this, slots(), menuName());
        this.addMenuBorder();
        this.setStaticItems();
        this.setMenuItems();
    }

    public final void open() {
        this.build();
        this.menuStorage.getOwner().openInventory(inventory);
    }

    private void addMenuBorder() {
        ItemStack glass = new Item(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();

        for (int i = 0; i < 9; i++) {
            this.inventory.setItem(i, glass);
        }
        for (int i = slots() - 1; i >= slots() - 9; i--) {
            this.inventory.setItem(i, glass);
        }
        for (int i = slots() - 1 - 9; i > 8; i -= 9) {
            this.inventory.setItem(i, glass);
        }
        for (int i = slots() - 9 - 9; i > 0; i -= 9) {
            this.inventory.setItem(i, glass);
        }
    }

    private void setStaticItems() {
        if (this.previousMenu != null) {
            this.inventory.setItem(slots() - 6, Item.createCustomHead("11221", NAME_BACK, null));
        }
        if (titleItem() != null) {
            this.inventory.setItem(4, titleItem());
        }
        this.inventory.setItem(slots() - 5, new Item(Material.BARRIER).setDisplayName(NAME_CLOSE).build());
        this.inventory.setItem(slots()-4, Item.createCustomHead("26110", NAME_SETTINGS, null));
    }

    public final ItemStack getFilterItem(Class<? extends Enum<?>> e, int filter) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add((filter == 0 ? ChatColor.GRAY + "➝ " + ChatColor.DARK_GRAY : ChatColor.GRAY + "") + "None");

        for (Enum<?> value : e.getEnumConstants()) {
            lore.add(value.ordinal() == filter - 1 ? ChatColor.GRAY + "➝ " + value : ChatColor.GRAY + ChatColor.stripColor(value.toString()));
        }
        lore.add("");
        lore.add(ChatColor.YELLOW + "Left-Click to toggle");
        lore.add(CustomColors.YELLOW.getChatColor() + "Right-Click for backwards");

        return new Item(Material.HOPPER).setDisplayName(ChatColor.BLUE + "Filter").setLore(lore).build();
    }

    public final ItemStack getSortingItem(Class<? extends Enum<?>> e, int sorting) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");

        for (Enum<?> value : e.getEnumConstants()) {
            String[] split = value.name().toLowerCase().split("_");
            StringBuilder result = new StringBuilder();
            for (String str : split) {
                if (!result.toString().equals("")) {
                    result.append(" ");
                }
                result.append(str.substring(0, 1).toUpperCase()).append(str.substring(1));
            }
            lore.add((value.ordinal() == sorting ? ChatColor.GRAY + "➝ " + ChatColor.AQUA : ChatColor.GRAY + "") + result);
        }
        lore.add("");
        lore.add(ChatColor.YELLOW + "Left-Click to toggle");
        lore.add(CustomColors.YELLOW.getChatColor() + "Right-Click for backwards");

        return new Item(Material.COMPARATOR).setDisplayName(ChatColor.BLUE + "Sorting").setLore(lore).build();
    }
}
