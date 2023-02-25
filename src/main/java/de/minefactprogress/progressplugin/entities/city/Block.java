package de.minefactprogress.progressplugin.entities.city;

import com.google.gson.annotations.SerializedName;
import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.entities.users.Rank;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.utils.*;
import de.minefactprogress.progressplugin.utils.conversion.CoordinateConversion;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

@Getter
@Setter
public class Block {

    private int uid;
    private int id;
    @SerializedName("district")
    private Integer districtId;
    private Status status;
    private double progress;
    private boolean details;
    private ArrayList<String> builders;
    private Date completionDate;
    @SerializedName("center")
    private double[] latlon;
    private ArrayList<double[]> area;
    private transient Location center = null;

    public District getDistrict() {
        return District.getDistrictById(districtId);
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

    public ItemStack toItemStack(Player p) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Status: " + ChatColor.valueOf(status.getColor()) + ChatColor.BOLD + status.getName());
        lore.add(ChatColor.GRAY + "Progress: ");
        lore.add(ProgressUtils.generateProgressbar(this.progress));
        lore.add(ChatColor.GRAY + "Details: " + (details ? ChatColor.GREEN + "✔" : ChatColor.RED + "✘"));

        if(!builders.isEmpty()) {
            lore.add(ChatColor.GRAY + "Builders:");
            for(String builder : builders) {
                User user = User.getUserByName(builder);
                if(user == null) {
                    lore.add(ChatColor.GRAY + "- " + Rank.PLAYER.getColor() + builder);
                } else {
                    lore.add(ChatColor.GRAY + "- " + user.getRank().getColor() + user.getUsername());
                }
            }
        }
        if(completionDate != null) {
            lore.add(ChatColor.GRAY + "Completion Date: " + ChatColor.YELLOW + completionDate);
        }

        if(Permissions.isTeamMember(p)) {
            lore.add("");
            lore.add(ChatColor.YELLOW + "Click for more options");
        }
        if(latlon != null && latlon.length == 2) {
            if(Permissions.isTeamMember(p)) {
                lore.add(CustomColors.YELLOW.getChatColor() + "Right-Click to teleport");
            } else {
                lore.add("");
                lore.add(ChatColor.YELLOW + "Click to teleport");
            }
        }

        String headID;
        switch(status) {
            case DONE -> headID = "59655";
            case DETAILING -> headID = "6225";
            case BUILDING -> headID = "6173";
            case RESERVED -> headID = "6231";
            default -> headID = "6241";
        }
        return Item.createCustomHead(headID, ChatColor.AQUA + "Block #" + id, lore);
    }

    public void setProgress(double progress, Player p) {

    }

    public void setDetails(boolean details, Player p) {

    }

    public void setBuilder(String name, Player p, boolean add) {

    }

    public static Block getBlock(District district, int id) {
        return API.getBlocks().stream().filter(b -> b.districtId == district.getId() && b.id == id).findFirst().orElse(null);
    }

    public static ArrayList<Block> getBlocksOfDistrict(District district) {
        return API.getBlocks().stream().filter(b -> b.districtId == district.getId()).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public String toString() {
        return getDistrict().getName() + " #" + id;
    }

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
