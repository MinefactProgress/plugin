package de.minefactprogress.progressplugin.entities.city;

import com.google.gson.annotations.SerializedName;
import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.utils.*;
import de.minefactprogress.progressplugin.utils.conversion.CoordinateConversion;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class District implements Comparable<District> {

    private int id;
    private String name;
    private Date completionDate;
    private Status status;
    private double progress;
    private BlockCount blocks;
    @SerializedName("parent")
    private int parentId;
    private ArrayList<Point2D.Double> area;
    @SerializedName("center")
    private double[] latlon;
    private transient Location center = null;

    public District getParent() {
        return getDistrictById(parentId);
    }

    public Location getCenter() {
        if (center == null && latlon != null && latlon.length == 2) {
            double[] coords = CoordinateConversion.convertFromGeo(latlon[0], latlon[1]);
            World world = Bukkit.getWorld("world");
            if(world != null) {
                center = new Location(world, coords[0], Utils.getHighestY(world, (int) coords[0], (int) coords[1]) + 1., coords[1]);
            }
        }
        return center;
    }

    public ItemStack toItemStack() {
        Material mat = getParent().getParent() == null
                ? Material.BOOKSHELF
                : getParent().getParent().getParent() == null
                ? Material.LECTERN
                : Material.BOOK;

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Status: " + ChatColor.valueOf(status.getColor()) + ChatColor.BOLD + status.getName());
        lore.add(ChatColor.GRAY + "Progress:");
        lore.add(ProgressUtils.generateProgressbar(MathUtils.roundTo2Decimals(progress)));
        lore.add(ChatColor.GRAY + "Blocks Done: " + ChatColor.YELLOW + blocks.getDone());
        lore.add(ChatColor.GRAY + "Blocks Left: " + ChatColor.YELLOW + blocks.getLeft());

        if(completionDate != null) {
            lore.add(ChatColor.GRAY + "Completion Date: " + ChatColor.YELLOW + completionDate);
        }
        if(blocks.getTotal() > 0) {
            lore.add("");
            lore.add(ChatColor.YELLOW + "Click for more info");
        }
        if(latlon != null && latlon.length == 2) {
            if (blocks.getTotal() == 0) {
                lore.add("");
            }
            lore.add(CustomColors.YELLOW.getChatColor() + "Right-Click to teleport");
        }

        Item item = new Item(mat).setDisplayName(ChatColor.AQUA + name).setLore(lore);
        if(status == Status.DONE) {
            item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            item.hideEnchantments(true);
        }

        return item.build();
    }

    public static District getDistrictById(int id) {
        return API.getDistricts().stream().filter(d -> d.id == id).findFirst().orElse(null);
    }

    public static District getDistrictByName(String name) {
        return API.getDistricts().stream().filter(d -> d.name.equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static ArrayList<District> getChildren(District district) {
        return API.getDistricts().stream()
                .filter(d -> d.parentId == district.id)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public int compareTo(@NotNull District d) {
        if (status.getId() == d.status.getId()) {
            if (progress == d.progress) {
                if (completionDate == null || d.completionDate == null) {
                    return 0;
                }
                if (completionDate.equals(d.completionDate)) {
                    return d.name.compareTo(name);
                }
                return completionDate.compareTo(d.completionDate);
            }
            return progress > d.progress ? 1 : -1;
        }
        return d.status.getId() - status.getId();
    }

    public enum Sorting implements Comparator<District> {
        PROGRESS {
            @Override
            public int compare(District d1, District d2) {
                if (d1.getStatus() == d2.getStatus()) {
                    double dif = d2.getProgress() - d1.getProgress();

                    if (dif == 0.0) return 0;
                    return dif > 0 ? 1 : -1;
                }
                return d2.getStatus().compareTo(d1.getStatus());
            }
        },
        NAME {
            @Override
            public int compare(District d1, District d2) {
                return d1.getName().compareTo(d2.getName());
            }
        },
        TOTAL_BLOCKS_COUNT {
            @Override
            public int compare(District d1, District d2) {
                return (d2.blocks.getDone() + d2.blocks.getLeft()) - (d1.blocks.getDone() + d1.blocks.getLeft());
            }
        },
        BLOCKS_DONE {
            @Override
            public int compare(District d1, District d2) {
                return d2.blocks.getDone() - d1.blocks.getDone();
            }
        },
        BLOCKS_LEFT {
            @Override
            public int compare(District d1, District d2) {
                return d2.blocks.getLeft() - d1.blocks.getLeft();
            }
        }
    }
}
