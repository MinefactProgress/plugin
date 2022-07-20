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
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

public class DistrictBossbar {

    @Getter @Setter
    private ArrayList<Player> players;
    private BossBar bar;

    public DistrictBossbar() {
        bar = Bukkit.createBossBar("New York City", BarColor.BLUE, BarStyle.SOLID);
        players = new ArrayList<>();
    }

    public void startSchedulers() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(),this::update, 0,30*20);
    }

    private void update() {
        ArrayList<District> districts = District.districts;
        HashMap<Integer, ArrayList<Point2D.Double>> areas = convertToAreas(districts);

        for(Player p:players) {
            double x = p.getLocation().getX();
            double y = p.getLocation().getZ();
            double[] playerPos = new double[0];
            try {
                playerPos = CoordinateConversion.convertToGeo(x,y);
            } catch (OutOfProjectionBoundsException e) {
                e.printStackTrace();
            }

            System.out.println("player: "+playerPos[0]+","+playerPos[1]);
            for(int i = 1; i <= areas.size();i++) {
                if(areas.get(i).size()==0) continue;
                if(Utils.inside(new Point2D.Double(playerPos[0],playerPos[1]),areas.get(i))) {
                    p.sendMessage("inside "+District.getDistrictByID(i).getName());
                    System.out.println("Inside "+District.getDistrictByID(i).getName());
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
        if(players.contains(player)) {
            removePlayer(player);
            return;
        }
        addPlayer(player);
    }
    public void addPlayer(Player player) {
        players.add(player);
    }
    public void removePlayer(Player player) {
        players.remove(player);
    }

}
