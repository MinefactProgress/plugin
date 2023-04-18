package de.minefactprogress.progressplugin.entities.city;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.api.Routes;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            lore.add(ChatColor.GRAY + "Completion Date: " + ChatColor.YELLOW + sdf.format(completionDate));
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
        User user = User.getUserByUUID(p.getUniqueId());
        if(user == null) {
            p.sendMessage(Main.getPREFIX() + ChatColor.RED + "You need to link your website account in order " +
                    "to update blocks. To do this type " + ChatColor.YELLOW + "/progress verify");
            return;
        }
        p.sendMessage(Constants.PREFIX + ChatColor.GRAY + "Updating block...");
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            JsonObject json = new JsonObject();
            json.addProperty("progress", progress);
            json.addProperty("editor", user.getUid());

            API.PUT(Routes.BLOCKS + "/" + uid, json, res -> {
                this.progress = progress;
                p.sendMessage(Constants.PREFIX + ChatColor.GRAY + "Successfully set progress of "
                        + ChatColor.YELLOW + getDistrict().getName() + " #" + id + ChatColor.GRAY
                        + " to " + ProgressUtils.progressToColor(progress) + progress + "%");
            }, p);
        });
    }

    public void setDetails(boolean details, Player p) {
        User user = User.getUserByUUID(p.getUniqueId());
        if(user == null) {
            p.sendMessage(Main.getPREFIX() + ChatColor.RED + "You need to link your website account in order " +
                    "to update blocks. To do this type " + ChatColor.YELLOW + "/progress verify");
            return;
        }
        p.sendMessage(Constants.PREFIX + ChatColor.GRAY + "Updating block...");
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            JsonObject json = new JsonObject();
            json.addProperty("details", details);
            json.addProperty("editor", user.getUid());

            API.PUT(Routes.BLOCKS + "/" + uid, json, res -> {
                this.details = details;
                p.sendMessage(Constants.PREFIX + ChatColor.GRAY + "Successfully set details of "
                        + ChatColor.YELLOW + getDistrict().getName() + " #" + id + ChatColor.GRAY
                        + " to " + (details ? ChatColor.GREEN + "true" : ChatColor.RED + "false"));
            }, p);
        });
    }

    public void setBuilder(String name, Player p, boolean add) {
        User editor = User.getUserByUUID(p.getUniqueId());
        if(editor == null) {
            p.sendMessage(Main.getPREFIX() + ChatColor.RED + "You need to link your website account in order " +
                    "to update blocks. To do this type " + ChatColor.YELLOW + "/progress verify");
            return;
        }
        if(add && builders.contains(name)) {
            p.sendMessage(Constants.PREFIX + ChatColor.RED + "This builder has already claimed this block");
            return;
        } else if(!add && !builders.contains(name)) {
            p.sendMessage(Constants.PREFIX + ChatColor.RED + "This builder has not claimed this block");
            return;
        }
        p.sendMessage(Constants.PREFIX + ChatColor.GRAY + "Updating block...");
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            User user = User.getUserByName(name);
            ArrayList<String> buildersNew = new ArrayList<>(builders);
            JsonObject json = new JsonObject();
            if(add) {
                buildersNew.add(name);
            } else {
                buildersNew.remove(name);
            }
            json.add("builder", new Gson().toJsonTree(buildersNew));

            API.PUT(Routes.BLOCKS + "/" + uid, json, res -> {
                this.builders = buildersNew;
                p.sendMessage(Constants.PREFIX + ChatColor.GRAY + "Successfully " + (add ? "added " : "removed ")
                        + (user != null ? user.getRank().getColor() : Rank.PLAYER.getColor()) + name + ChatColor.GRAY
                        + " as a builder of " + ChatColor.YELLOW + getDistrict().getName() + " #" + id + ChatColor.GRAY);
            }, p);
        });
    }

    public static Block getBlockById(int id) {
        return API.getBlocks().stream().filter(b -> b.uid == id).findFirst().orElse(null);
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
