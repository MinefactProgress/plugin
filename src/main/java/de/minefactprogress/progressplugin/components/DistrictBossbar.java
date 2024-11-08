package de.minefactprogress.progressplugin.components;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.entities.city.District;
import de.minefactprogress.progressplugin.utils.LocationUtils;
import de.minefactprogress.progressplugin.utils.MathUtils;
import de.minefactprogress.progressplugin.utils.Utils;
import de.minefactprogress.progressplugin.utils.conversion.CoordinateConversion;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DistrictBossbar {

    @Getter @Setter
    private HashMap<UUID, BossBar> bars;
    private int UPDATE_INTERVAL = 2;

    public DistrictBossbar() {
        bars = new HashMap<>();
    }

    public void startSchedulers() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(),this::update, 0,UPDATE_INTERVAL* 20L);
    }

    private void update() {

        for(UUID uuid:bars.keySet()) {
            Player p = Bukkit.getPlayer(uuid);

            if(p == null) continue;

            District district = getCurrentDistrict(p);
            if(district == null) district = District.getDistrictByName("New York City");

            bars.get(p.getUniqueId()).name(Component.text( ChatColor.GRAY+district.getName()+ " (" + district.getStatus().getChatColor()+MathUtils.roundTo2Decimals(district.getProgress()) +ChatColor.GRAY+ "%)"));
            bars.get(p.getUniqueId()).progress((float)(district.getProgress()/100));
            bars.get(p.getUniqueId()).color(BossBar.Color.valueOf(district.getStatus().getColor().replace("GOLD","YELLOW")));
        }
    }

    public void updatePlayer(Player p) {
        p.showBossBar(bars.get(p.getUniqueId()));
    }

    private HashMap<Integer, ArrayList<Point2D.Double>> convertToAreas(List<District> object) {
        HashMap<Integer,ArrayList<Point2D.Double>> result = new HashMap<>();
        for(District e : object) {
            result.put(e.getId(), e.getArea());
        }
        return result;
    }

    public District getCurrentDistrict(Player player) {
        if(bars.containsKey(player.getUniqueId())) {
            return LocationUtils.getCurrentDistrict(player.getLocation());
        }
        return null;
    }

    public void togglePlayer(Player player) {
        if(bars.containsKey(player.getUniqueId())) {
            removePlayer(player);
            return;
        }
        addPlayer(player);
    }
    public void addPlayer(Player player) {
        bars.put(player.getUniqueId(), BossBar.bossBar(Component.text("New York City"),0, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS));
        player.showBossBar(bars.get(player.getUniqueId()));
    }
    public void removePlayer(Player player) {
        if(bars.containsKey(player.getUniqueId())) {
            player.hideBossBar(bars.get(player.getUniqueId()));
            bars.remove(player.getUniqueId());
        }
    }

}
