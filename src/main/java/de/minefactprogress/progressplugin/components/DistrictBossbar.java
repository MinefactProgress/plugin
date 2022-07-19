package de.minefactprogress.progressplugin.components;

import com.google.gson.*;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.RequestHandler;
import de.minefactprogress.progressplugin.utils.Utils;
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
        String resp = RequestHandler.getInstance().GET("api/districts/get");

        if(resp == null) return;

        JsonElement jsonElement;
        try {
            jsonElement = JsonParser.parseString(resp);
        } catch (JsonSyntaxException e) {
            Bukkit.getLogger().warning("Could not parse users API response to JSON!");
            return;
        }

        if(!jsonElement.isJsonArray()) return;

        JsonArray jsonArray = jsonElement.getAsJsonArray();

        HashMap<String, ArrayList<Point2D.Double>> areaArray = convertToArray(jsonArray);
        for(Player p:players) {
            int x = p.getLocation().getBlockX();
            int y = p.getLocation().getBlockY();
            Point2D.Double playerPos = new Point2D.Double(x,y);

            System.out.println("player: "+playerPos);
            if(Utils.inside(playerPos,areaArray.get("New York City"))) {
                p.sendMessage("i");
                System.out.println("Inside nyc");
            } else {
                p.sendMessage("o");
                System.out.println("outside nyc");

            }
        }
    }

    private HashMap<String, ArrayList<Point2D.Double>> convertToArray(JsonArray object) {
        HashMap<String,ArrayList<Point2D.Double>> result = new HashMap<>();
        for(JsonElement e:object) {
            JsonObject el = e.getAsJsonObject();
            ArrayList<Point2D.Double> area = new ArrayList<>();
            for(JsonElement point:el.get("area").getAsJsonArray()) {
                String x = point.getAsJsonArray().get(0).getAsString();
                String y = point.getAsJsonArray().get(1).getAsString();
                Point2D.Double p = new Point2D.Double(Double.parseDouble(x),Double.parseDouble(y));
                area.add(p);
            }
            result.put(el.get("name").getAsString(),area);
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
