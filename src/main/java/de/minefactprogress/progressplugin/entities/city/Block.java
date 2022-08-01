package de.minefactprogress.progressplugin.entities.city;

import com.google.gson.JsonObject;
import de.minefactprogress.progressplugin.entities.users.Rank;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.utils.Item;
import de.minefactprogress.progressplugin.utils.MathUtils;
import de.minefactprogress.progressplugin.utils.ProgressUtils;
import de.minefactprogress.progressplugin.utils.time.DateUtils;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@Getter
public class Block {

    public static final ArrayList<Block> blocks = new ArrayList<>();

    private final District district;
    private final int id;
    private final Status status;
    private final double progress;
    private final boolean details;
    private final String date;
    private final ArrayList<String> builders;

    public Block(JsonObject json) {
        this.district = District.getDistrictByID(json.get("district").getAsInt());
        this.id = json.get("id").getAsInt();
        this.status = Status.getByID(json.get("status").getAsInt());
        this.progress = MathUtils.roundTo2Decimals(json.get("progress").getAsDouble());
        this.details = json.get("details").getAsBoolean();
        this.date = json.get("completionDate").isJsonNull() ? null : DateUtils.formatDateFromISOString(json.get("completionDate").getAsString());

        this.builders = new ArrayList<>();
        String[] builders = json.get("builder") == null || json.get("builder").isJsonNull() || json.get("builder").getAsString().equals("")
                ? new String[0] : json.get("builder").getAsString().split(",");
        for (String builder : builders) {
            if (!this.builders.contains(builder)) {
                this.builders.add(builder);
            }
        }
    }

    public ItemStack toItemStack() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Status: " + ChatColor.valueOf(status.getColor()) + ChatColor.BOLD + status.getName());
        lore.add(ChatColor.GRAY + "Progress: " + ProgressUtils.progressToColor(progress) + progress + "%");
        lore.add(ChatColor.GRAY + "Details: " + (details ? ChatColor.GREEN + "✔" : ChatColor.RED + "✘"));

        if (!builders.isEmpty()) {
            lore.add(ChatColor.GRAY + "Builder:");
            for (String builder : builders) {
                User user = User.getByName(builder);
                if (user == null) {
                    lore.add(ChatColor.GRAY + "- " + Rank.PLAYER.getColor() + builder);
                } else {
                    lore.add(ChatColor.GRAY + "- " + user.getRank().getColor() + user.getName());
                }
            }
        }
        if (date != null) {
            lore.add(ChatColor.GRAY + "Completion Date: " + ChatColor.YELLOW + date);
        }

        Item item = new Item(Material.PAPER).setDisplayName(ChatColor.AQUA + "Block #" + id).setLore(lore);
        if (status == Status.DONE) {
            item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            item.hideEnchantments(true);
        }

        return item.build();
    }

    // -----===== Static Methods =====-----

    public static ArrayList<Block> getBlocksOfDistrict(District district) {
        return blocks.stream().filter(b -> b.district.equals(district)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static Block getBlock(District district, int id) {
        return blocks.stream().filter(b -> b.district.equals(district) && b.id == id).findFirst().orElse(null);
    }

    // -----===== Sorting Enum =====-----

    public enum Sorting implements Comparator<Block> {
        ID {
            @Override
            public int compare(Block b1, Block b2) {
                return b1.getId() - b2.getId();
            }
        },
        PROGRESS {
            @Override
            public int compare(Block b1, Block b2) {
                if (b1.getStatus() == b2.getStatus()) {
                    double dif = b2.getProgress() - b1.getProgress();

                    if (dif == 0.0) return 0;
                    return dif > 0 ? 1 : -1;
                }
                return b2.getStatus().compareTo(b1.getStatus());
            }
        }
    }
}
