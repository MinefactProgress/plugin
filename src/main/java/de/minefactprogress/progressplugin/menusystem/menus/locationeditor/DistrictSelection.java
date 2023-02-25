package de.minefactprogress.progressplugin.menusystem.menus.locationeditor;

import de.minefactprogress.progressplugin.components.LocationEditor;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.District;
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

public class DistrictSelection extends PaginatedMenu {

    public DistrictSelection(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
    }

    @Override
    public Component menuName() {
        return Component.text("Select a district", CustomColors.BLUE.getComponentColor());
    }

    @Override
    public int rows() {
        return 6;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = menuStorage.getOwner();
        ItemStack item = e.getCurrentItem();
        LocationEditor locationEditor = LocationEditor.get(p);

        if (item == null || locationEditor == null) return;

        String districtName = PlainTextComponentSerializer.plainText().serializeOrNull(item.getItemMeta().displayName());
        District district = District.getDistrictByName(districtName);

        if(district == null) {
            locationEditor.setDistrict(null);
            locationEditor.setBlockID(0);
            p.closeInventory();
            return;
        }

        int blockID = Block.getBlocksOfDistrict(district)
                .stream()
                .filter(b -> b.getArea().size() > 0)
                .mapToInt(Block::getId).max()
                .orElse(0);

        locationEditor.setDistrict(district);
        locationEditor.setBlockID(blockID+1);
        p.closeInventory();
    }

    @Override
    public void setMenuItems() {

    }

    @Override
    public List<ItemStack> items() {
        ArrayList<ItemStack> items = new ArrayList<>();
        ArrayList<District> districts = District.getDistrictsWithoutChildren();

        districts.sort(District.Sorting.NAME);

        for(District district : districts) {
            ArrayList<Block> blocks = Block.getBlocksOfDistrict(district);
            long blocksWithArea = blocks.stream().filter(b -> b.getArea().size() > 0).count();
            ChatColor color = blocks.size() == blocksWithArea
                    ? ChatColor.GREEN
                    : blocksWithArea == 0
                    ? ChatColor.RED
                    : ChatColor.YELLOW;
            items.add(Item.create(Material.BOOK, CustomColors.BLUE.getChatColor() + district.getName(), new ArrayList<>(
                    List.of(ChatColor.GRAY + district.getParent().getName(), "", color + "" + blocksWithArea + ChatColor.GRAY + "/" + blocks.size())))
            );
        }

        return items;
    }
}
