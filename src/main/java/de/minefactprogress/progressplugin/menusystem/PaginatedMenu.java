package de.minefactprogress.progressplugin.menusystem;

import de.minefactprogress.progressplugin.utils.Item;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class PaginatedMenu extends Menu {

    public static final String NAME_NEXT = "§bNext Page §8>>";
    public static final String NAME_PREVIOUS = "§8<< §bPrevious Page";

    protected int currentPage = 0;
    protected int maxItemsPerPage = ((slots() / 9) - 2) * 7;
    protected int index = 0;

    public PaginatedMenu(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
    }

    public abstract List<ItemStack> items();

    protected void build() {
        super.build();
        this.addDynamicItems();
    }

    public void nextPage() {
        this.currentPage++;
        open();
    }

    public void previousPage() {
        this.currentPage--;
        open();
    }

    public void addDynamicItems() {
        List<ItemStack> items = items();
        for (int i = 0; i < maxItemsPerPage; i++) {
            index = maxItemsPerPage * currentPage + i;
            if (index >= items.size()) {
                break;
            }
            if (items.get(index) != null) {
                ItemStack item = items.get(index);
                this.inventory.addItem(item);
            }
        }
        this.inventory.setItem(slots() - 2, NumberHeads.getFromNumber(currentPage + 1).getHead());

        if (this.currentPage > 0) {
            this.inventory.setItem(slots() - 3, Item.createCustomHead("11218", NAME_PREVIOUS, null));
        }
        if (this.index + 1 < items.size()) {
            this.inventory.setItem(slots() - 1, Item.createCustomHead("11215", NAME_NEXT, null));
        }
    }
}
