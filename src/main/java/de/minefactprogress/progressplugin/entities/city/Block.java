package de.minefactprogress.progressplugin.entities.city;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.entities.users.Rank;
import de.minefactprogress.progressplugin.entities.users.User;
import de.minefactprogress.progressplugin.utils.*;
import de.minefactprogress.progressplugin.utils.conversion.CoordinateConversion;
import de.minefactprogress.progressplugin.utils.time.DateUtils;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@Getter
@ToString
public class Block {

    public static final ArrayList<Block> blocks = new ArrayList<>();

    private final District district;
    private final int id;
    private Status status;
    private double progress;
    private boolean details;
    private final String date;
    private final ArrayList<String> builders;
    private Location center = null;
    private final double[] latlong;

    public Block(JsonObject json) {
        this.district = District.getDistrictByID(json.get("district").getAsJsonObject().get("id").getAsInt());
        this.id = json.get("id").getAsInt();
        this.status = Status.getByID(json.get("status").getAsInt());
        this.progress = MathUtils.roundTo2Decimals(json.get("progress").getAsDouble());
        this.details = json.get("details").getAsBoolean();
        this.date = json.get("completionDate").isJsonNull() ? null : DateUtils.formatDateFromISOString(json.get("completionDate").getAsString());

        this.builders = new ArrayList<>();
        for (String builder : new Gson().fromJson(json.get("builders").getAsJsonArray(), String[].class)) {
            if (!this.builders.contains(builder)) {
                this.builders.add(builder);
            }
        }

        JsonArray latlongJson = json.get("center").getAsJsonArray();
        if(latlongJson.size() == 2) {
            this.latlong = new double[2];
            this.latlong[0] = latlongJson.get(0).getAsDouble();
            this.latlong[1] = latlongJson.get(1).getAsDouble();
        } else {
            this.latlong = null;
        }
    }

    public Location getCenter() {
        if(this.center == null && this.latlong != null) {
            double[] coords = CoordinateConversion.convertFromGeo(latlong[0], latlong[1]);
            World world = Bukkit.getWorld("world");
            if(world != null) {
                this.center = new Location(world, coords[0], Utils.getHighestY(world, (int) coords[0], (int) coords[1]) + 1., coords[1]);
            }
        }
        return this.center;
    }

    public ItemStack toItemStack(Player p, boolean titleitem) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Status: " + ChatColor.valueOf(status.getColor()) + ChatColor.BOLD + status.getName());
        lore.add(ChatColor.GRAY + "Progress: ");
        lore.add(ProgressUtils.generateProgressbar(this.progress));
        lore.add(ChatColor.GRAY + "Details: " + (details ? ChatColor.GREEN + "✔" : ChatColor.RED + "✘"));

        if (!builders.isEmpty()) {
            lore.add(ChatColor.GRAY + "Builder:");
            for (String builder : builders) {
                User user = User.getUserByName(builder);
                if (user == null) {
                    lore.add(ChatColor.GRAY + "- " + Rank.PLAYER.getColor() + builder);
                } else {
                    lore.add(ChatColor.GRAY + "- " + user.getRank().getColor() + user.getUsername());
                }
            }
        }
        if (date != null) {
            lore.add(ChatColor.GRAY + "Completion Date: " + ChatColor.YELLOW + date);
        }
        if(!titleitem) {
            if(Permissions.isTeamMember(p)) {
                lore.add("");
                lore.add(ChatColor.YELLOW + "Click for more options");
            }
            if(latlong != null) {
                if(Permissions.isTeamMember(p)) {
                    lore.add(CustomColors.YELLOW.getChatColor() + "Right-Click to teleport");
                } else {
                    lore.add("");
                    lore.add(ChatColor.YELLOW + "Click to teleport");
                }
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
//        JsonObject json = RequestHandler.getInstance().createJsonObject(this);
//        json.getAsJsonObject("values").addProperty("progress", progress);
//
//        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
//            if(doRequest(json, "Progress", String.valueOf(progress), p)) {
//                this.progress = progress;
//                refreshStatus();
//            } else {
//                sendErrorMessage(p);
//            }
//        });
    }

    public void setDetails(boolean details, Player p) {
//        JsonObject json = RequestHandler.getInstance().createJsonObject(this);
//        json.getAsJsonObject("values").addProperty("details", details);
//
//        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
//            if (doRequest(json, "Details", String.valueOf(details), p)) {
//                this.details = details;
//                refreshStatus();
//            } else {
//                sendErrorMessage(p);
//            }
//        });
    }

    public void setBuilder(String name, Player p, boolean add) {
//        JsonObject json = RequestHandler.getInstance().createJsonObject(this);
//
//        if((add && builders.contains(name)) || (!add && !builders.contains(name))) return;
//
//        // Add or remove builder form builders list
//        if(add) {
//            builders.add(name);
//        } else {
//            builders.remove(name);
//        }
//
//        json.getAsJsonObject("values").addProperty("builder", String.join(",", builders));
//
//        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
//            if(!RequestHandler.getInstance().POST("api/blocks/update", json).get("error").getAsBoolean()) {
//                User user = User.getByName(name);
//                p.sendMessage(Main.getPREFIX() + ChatColor.GREEN + "Builder " +
//                        (user == null ? Rank.PLAYER.getColor() : user.getRank().getColor()) + name +
//                        ChatColor.GREEN + " successfully " + (add ? "added to" : "removed from") + " Block " + ChatColor.YELLOW + "#" + id +
//                        ChatColor.GREEN + " of " + ChatColor.YELLOW + district.getName());
//
//                refreshStatus();
//            } else {
//                if(add) {
//                    builders.remove(name);
//                } else {
//                    builders.add(name);
//                }
//                sendErrorMessage(p);
//            }
//        });
    }

    private boolean doRequest(JsonObject json, String type, String newValue, Player p) {
//        p.sendMessage(Main.getPREFIX() + ChatColor.GRAY + "Updating block...");
//        if (!RequestHandler.getInstance().POST("api/blocks/update", json).get("error").getAsBoolean()) {
//            p.sendMessage(Main.getPREFIX() + ChatColor.GREEN + type + " of " + ChatColor.YELLOW + district.getName()
//                    + " #" + id + ChatColor.GREEN + " successfully set" + (newValue != null ? " to " + ChatColor.YELLOW + newValue : ""));
//            return true;
//        } else {
//            p.sendMessage(Main.getPREFIX() + ChatColor.RED + "Couldn't update block data! Please try again.");
//            return false;
//        }
        return false;
    }

    private void refreshStatus() {
        if (progress >= 100 && details) {
            status = Status.DONE;
        } else if (progress >= 100) {
            status = Status.DETAILING;
        } else if (progress > 0 || details) {
            status = Status.BUILDING;
        } else if (!builders.isEmpty()) {
            status = Status.RESERVED;
        } else {
            status = Status.NOT_STARTED;
        }
    }

    private void sendErrorMessage(Player p) {
        p.sendMessage(Main.getPREFIX() + ChatColor.RED + "Couldn't update block data! Please try again.");
    }

    // -----===== Static Methods =====-----

    public static ArrayList<Block> getBlocksOfDistrict(District district) {
        return API.getBlocks().stream().filter(b -> b.district.equals(district)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static Block getBlock(District district, int id) {
        return API.getBlocks().stream().filter(b -> b.district.equals(district) && b.id == id).findFirst().orElse(null);
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
