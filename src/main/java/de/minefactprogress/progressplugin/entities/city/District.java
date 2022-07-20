package de.minefactprogress.progressplugin.entities.city;

import com.google.gson.JsonObject;
import de.minefactprogress.progressplugin.utils.DateUtils;
import de.minefactprogress.progressplugin.utils.Item;
import de.minefactprogress.progressplugin.utils.MathUtils;
import de.minefactprogress.progressplugin.utils.ProgressUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

@Getter
public class District implements Comparable<District> {

    public static final ArrayList<District> districts = new ArrayList<>();

    private final int id;
    private final String name;
    private final Status status;
    private final double progress;
    private final int blocksDone;
    private final int blocksLeft;
    private final String date;
    private final District parent;

    public District(JsonObject json) {
        this.id = json.get("id").getAsInt();
        this.name = json.get("name").getAsString();
        this.status = Status.getByID(json.get("status").getAsInt());
        this.progress = MathUtils.roundTo2Decimals(json.get("progress").getAsDouble());
        this.blocksDone = json.get("blocks").getAsJsonObject().get("done").getAsInt();
        this.blocksLeft = json.get("blocks").getAsJsonObject().get("left").getAsInt();
        this.date = json.get("completionDate").isJsonNull() ? null : DateUtils.formatDateFromISOString(json.get("completionDate").getAsString());
        this.parent = json.get("parent").isJsonNull() ? null : getDistrictByID(json.get("parent").getAsInt());
    }

    public static District getDistrictByID(int id) {
        return districts.stream().filter(d -> d.id == id).findFirst().orElse(null);
    }

    public static District getDistrictByName(String name) {
        return districts.stream().filter(d -> d.name.equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    // -----===== Static Methods =====-----

    public static ArrayList<District> getChildren(String districtName) {
        return districts.stream()
                .filter(d -> d.parent != null && d.parent.id == getDistrictByName(districtName).id)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ItemStack toItemStack() {
        if (parent == null) return null;

        Material mat = parent.parent == null
                ? Material.BOOKSHELF
                : parent.parent.parent == null
                ? Material.LECTERN
                : Material.BOOK;

        ArrayList<String> lore = new ArrayList<>();
        lore.add("§7Status: " + status.getColor() + "§l" + status.getName());
        lore.add("§7Progress: " + ProgressUtils.progressToColor(progress) + progress + "%");
        lore.add("§7Blocks Done: §e" + blocksDone);
        lore.add("§7Blocks Left: §e" + blocksLeft);

        if (date != null) {
            lore.add("§7Completion Date: §e" + date);
        }

        Item item = new Item(mat).setDisplayName("§b" + name).setLore(lore);
        if (status == Status.DONE) {
            item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            item.hideEnchantments(true);
        }

        return item.build();
    }

    @Override
    public int compareTo(District d) {
        if (status.getId() == d.status.getId()) {
            if (progress == d.progress) {
                if (date == null || d.date == null) {
                    return 0;
                }
                if (date.equals(d.date)) {
                    return d.name.compareTo(name);
                }
                String[] dateSplit = date.split("\\.");
                String[] dateSplitOther = d.date.split("\\.");

                Date date1 = new Date(Integer.parseInt(dateSplit[2]), Integer.parseInt(dateSplit[1]), Integer.parseInt(dateSplit[0]));
                Date date2 = new Date(Integer.parseInt(dateSplitOther[2]), Integer.parseInt(dateSplitOther[1]), Integer.parseInt(dateSplitOther[0]));
                return date2.compareTo(date1);
            }
            return progress > d.progress ? 1 : -1;
        }
        return d.status.getId() - status.getId();
    }
}
