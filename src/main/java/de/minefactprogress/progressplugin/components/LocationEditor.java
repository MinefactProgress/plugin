package de.minefactprogress.progressplugin.components;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.District;
import de.minefactprogress.progressplugin.utils.Constants;
import de.minefactprogress.progressplugin.utils.Item;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocationEditor {

    private static final int RADIUS = 100;
    private static final double HEIGHT_OFFSET = -0.75;
    public static final String NAME_SELECT_DISTRICT = ChatColor.YELLOW + "Select District";
    public static final String NAME_PREVIOUS_BLOCK = ChatColor.AQUA + "Previous Block";
    public static final String NAME_NEXT_BLOCK = ChatColor.AQUA + "Next Block";
    public static final String NAME_DYNAMIC_HEIGHT = ChatColor.GRAY + "Dynamic Outline Height: ";
    public static final String NAME_RESET_SELECTION = ChatColor.RED + "Reset Selection";
    public static final String NAME_LEAVE_EDITOR = ChatColor.DARK_RED + "Leave LocationEditor";

    private static final HashMap<Player, LocationEditor> editors = new HashMap<>();

    private final ArrayList<ArmorStand> armorStands = new ArrayList<>();
    private final ArrayList<double[]> selectedCoords = new ArrayList<>();
    private final Player player;
    private ItemStack[] inventory;
    @Getter @Setter
    private District district;
    @Getter @Setter
    private int blockID;
    @Getter
    private Double fixedHeight = null;

    public LocationEditor(Player player) {
        this.player = player;
        this.district = null;
        this.blockID = 0;

        loadInventory();
        editors.put(player, this);
        ScoreboardHandler.sendScoreboard(player);
        player.sendMessage(Constants.PREFIX + ChatColor.GRAY + "LocationEditor " + ChatColor.GREEN + "started");
    }

    private void loadInventory() {
        Inventory inv = player.getInventory();
        inventory = inv.getContents();
        inv.clear();

        inv.setItem(0, Item.createCustomHead("2981", NAME_SELECT_DISTRICT, null));
        inv.setItem(2, Item.createCustomHead("9226", NAME_PREVIOUS_BLOCK, null));
        inv.setItem(3, Item.createCustomHead("9223", NAME_NEXT_BLOCK, null));
        inv.setItem(5, Item.createCustomHead("9868", NAME_DYNAMIC_HEIGHT + ChatColor.GREEN + "Enabled", null));
        inv.setItem(7, Item.createCustomHead("9382", NAME_RESET_SELECTION, null));
        inv.setItem(8, Item.create(Material.BARRIER, NAME_LEAVE_EDITOR));
    }

    public void destroy() {
        for(ArmorStand as : armorStands) {
            as.remove();
        }
        player.getInventory().setContents(inventory);
        armorStands.clear();
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        editors.remove(player);
        player.sendMessage(Constants.PREFIX + ChatColor.GRAY + "LocationEditor " + ChatColor.RED + "stopped");
    }

    public void addPoint(Location loc) {
        ArmorStand as = getArmorStand(loc);
        if(as == null) return;

        if(selectedCoords.size() > 0 && selectedCoords.get(0)[0] == loc.getX() && selectedCoords.get(0)[1] == loc.getZ()) {
            // Finish selection
            if(selectedCoords.size() < 3) {
                player.sendMessage(Constants.PREFIX + ChatColor.RED + "The selection must consist of at least 3 points");
                return;
            }
            // TODO: Save selection
            clearSelection();
            player.sendMessage(Constants.PREFIX + ChatColor.GREEN + "Selection saved");
            return;
        }
        if(isSelected(as)) {
            player.sendMessage(Constants.PREFIX + ChatColor.RED + "This Point is already added");
            return;
        }
        selectedCoords.add(new double[]{loc.getX(), loc.getZ()});

        as.customName(Component.text(ChatColor.RED + "Point #" + selectedCoords.size()));
        as.setItem(EquipmentSlot.HEAD, Item.createCustomHead("9356", null, null));
        player.sendMessage(Constants.PREFIX + ChatColor.GRAY + "Point added");
    }

    private void spawnParticleLines() {
        double offset = 1.7;
        if(selectedCoords.size() < 2) return;

        for(int i = 1; i < selectedCoords.size(); i++) {
            Location startLoc = getArmorStand(selectedCoords.get(i-1)).getLocation().add(0, offset, 0);
            Location endLoc = getArmorStand(selectedCoords.get(i)).getLocation().add(0, offset, 0);

            ParticleDrawer.drawLine(player, startLoc, endLoc);
        }
        if(selectedCoords.size() >= 3) {
            Location startLoc = getArmorStand(selectedCoords.get(0)).getLocation().add(0, offset, 0);
            Location endLoc = getArmorStand(selectedCoords.get(selectedCoords.size()-1)).getLocation().add(0, offset, 0);

            ParticleDrawer.drawLine(player, startLoc, endLoc);
        }
    }

    public void setFixedHeight(Double height) {
        this.fixedHeight = (height == null) ? null : height + HEIGHT_OFFSET;
    }

    public ArmorStand getArmorStand(Location loc) {
        return armorStands.stream().filter(a -> a.getLocation().getX() == loc.getX() && a.getLocation().getZ() == loc.getZ()).findFirst().orElse(null);
    }

    public ArmorStand getArmorStand(double[] coords) {
        if(coords.length != 2) return null;

        Location loc = new Location(player.getWorld(), coords[0], player.getLocation().getY(), coords[1]);
        return getArmorStand(loc);
    }

    public Location getNearestPoint(Location loc) {
        double distance = Double.MAX_VALUE;
        Location location = null;
        for(ArmorStand as : armorStands) {
            double currentDistance = as.getLocation().distance(loc);
            if(currentDistance < distance) {
                distance = currentDistance;
                location = as.getLocation();
            }
        }
        return location;
    }

    public int countSelectedPoints() {
        return selectedCoords.size();
    }

    public int countVisiblePoints() {
        return armorStands.size();
    }

    public boolean isSelected(ArmorStand as) {
        for(double[] coords : selectedCoords) {
            if(coords[0] == as.getLocation().getX() && coords[1] == as.getLocation().getZ()) {
                return true;
            }
        }
        return false;
    }

    public void clearSelection() {
        for(ArmorStand as : armorStands) {
            if(isSelected(as)) {
                as.customName(Component.text(ChatColor.GREEN + "Click to add point"));
                as.setItem(EquipmentSlot.HEAD, Item.createCustomHead("9896", null, null));
            }
        }
        selectedCoords.clear();
        player.sendMessage(Constants.PREFIX + ChatColor.GRAY + "Selection cleared");
    }

    public boolean isOccupied(Location loc) {
        for(ArmorStand as : armorStands) {
            if(as.getLocation().getX() == loc.getX() && as.getLocation().getZ() == loc.getZ()) {
                return true;
            }
        }
        return false;
    }

    public boolean isUsedArmorstand(ArmorStand armorStand) {
        for(ArmorStand as : armorStands) {
            if(as.equals(armorStand)) return true;
        }
        return false;
    }

    public static boolean isUsedArmorstandGlobal(ArmorStand as) {
        for(Map.Entry<Player, LocationEditor> entry : editors.entrySet()) {
            if(entry.getValue().isUsedArmorstand(as)) return true;
        }
        return false;
    }

    public static LocationEditor get(Player p) {
        return editors.get(p);
    }

    public static void destroyAll() {
        for(Map.Entry<Player, LocationEditor> entry : editors.entrySet()) {
            entry.getValue().destroy();
        }
    }

    public static void startScheduler() {
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
            for(Map.Entry<Player, LocationEditor> entry : editors.entrySet()) {
                Player p = entry.getKey();
                Double fixedHeight = entry.getValue().getFixedHeight();

                // Spawn new Armorstands
                for(Block block : API.getBlocks()) {
                    for(Point2D.Double coords : block.getArea()) {
                        Location loc = new Location(p.getWorld(), coords.getX(), fixedHeight != null ? fixedHeight : (p.getLocation().getY() + HEIGHT_OFFSET), coords.getY());

                        // TODO: Problem ist, dass getArea lat lon zurückliefert statt mc coords

                        if(loc.distance(p.getLocation()) <= RADIUS && !entry.getValue().isOccupied(loc)) {
                            ArmorStand as = createArmorstand(loc);
                            entry.getValue().armorStands.add(as);

                            for(Player all : Bukkit.getOnlinePlayers()) {
                                if(!all.equals(p)) {
                                    all.hideEntity(Main.getInstance(), as);
                                }
                            }
                        }
                    }
                }

                // Remove not needed Armor Stand
                entry.getValue().armorStands.removeIf(e -> {
                    boolean remove = e.getLocation().distance(p.getLocation()) > RADIUS && !entry.getValue().isSelected(e);
                    if(remove) {
                        e.remove();
                    }
                    return remove;
                });

                // Teleport armor stands on player height
                for (ArmorStand as : entry.getValue().armorStands) {
                    if(as.getLocation().getY() != p.getLocation().getY()) {
                        as.teleport(new Location(as.getWorld(), as.getLocation().getX(), fixedHeight != null ? fixedHeight : (p.getLocation().getY() + HEIGHT_OFFSET), as.getLocation().getZ()));
                    }
                }
                entry.getValue().spawnParticleLines();
                ScoreboardHandler.updateScoreboard(entry.getKey());
            }
        }, 0L, 5L);
    }

    private static ArmorStand createArmorstand(Location loc) {
        ArmorStand as = loc.getWorld().spawn(loc, ArmorStand.class);
        as.setGravity(false);
        as.setCanPickupItems(false);
        as.setInvulnerable(true);
        as.setInvisible(true);
        as.customName(Component.text(ChatColor.GREEN + "Click to add point"));
        as.setCustomNameVisible(true);
        as.setItem(EquipmentSlot.HEAD, Item.createCustomHead("9896", null, null));
        return as;
    }
}
