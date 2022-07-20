package de.minefactprogress.progressplugin.components;

import com.google.gson.*;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.RequestHandler;
import de.minefactprogress.progressplugin.entities.city.District;
import de.minefactprogress.progressplugin.utils.Utils;
import de.minefactprogress.progressplugin.utils.conversion.CoordinateConversion;
import de.minefactprogress.progressplugin.utils.conversion.projection.OutOfProjectionBoundsException;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

public class DistrictBossbar {

    @Getter @Setter
    private HashMap<Player, BossBar> bars;
    private int UPDATE_INTERVAL = 2;

    public DistrictBossbar() {
        bars = new HashMap<>();
    }

    public void startSchedulers() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(),this::update, 0,UPDATE_INTERVAL* 20L);
    }

    private void update() {
        ArrayList<District> districts = District.districts;
        HashMap<Integer, ArrayList<Point2D.Double>> areas = convertToAreas(districts);

        for(Player p:bars.keySet()) {
            double x = p.getLocation().getX();
            double y = p.getLocation().getZ();
            double[] playerPos = new double[0];
            try {
                playerPos = CoordinateConversion.convertToGeo(x,y);
            } catch (OutOfProjectionBoundsException e) {
                e.printStackTrace();
            }
            for(int i = 1; i <= areas.size();i++) {
                if(areas.get(i).size()==0) continue;
                if(Utils.inside(new Point2D.Double(playerPos[0],playerPos[1]),areas.get(i))) {
                    District district = District.getDistrictByID(i);
                    bars.get(p).name(Component.text(district.getName()));
                    bars.get(p).progress((float)(district.getProgress()/100));
                    bars.get(p).color(BossBar.Color.valueOf(district.getStatus().getColor().replace("GOLD","YELLOW")));
                }
            }
        }
    }

    private HashMap<Integer, ArrayList<Point2D.Double>> convertToAreas(ArrayList<District> object) {
        HashMap<Integer,ArrayList<Point2D.Double>> result = new HashMap<>();
        for(District e: object) {
            result.put(e.getId(),e.getArea());
        }
        return result;
    }

    public void togglePlayer(Player player) {
        if(bars.containsKey(player)) {
            removePlayer(player);
            return;
        }
        addPlayer(player);
    }
    public void addPlayer(Player player) {
        bars.put(player, BossBar.bossBar(Component.text("New York City"),0, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS));
        player.showBossBar(bars.get(player));
    }
    public void removePlayer(Player player) {
        player.hideBossBar(bars.get(player));
        bars.remove(player);
    }

}
