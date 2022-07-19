package de.minefactprogress.progressplugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static boolean containsStringFromList(String string, List<String> list) {
        for (String s : list)
            if (string.contains(s))
                return true;

        return false;
    }

    // Returns the highest Block Y-Level of a given X-Y Coordinate
    public static int getHighestY(World world, int x, int z) {
        int y = 255;
        while (world.getBlockAt(x, y, z).getType() == Material.AIR || world.getBlockAt(x, y, z).getType() == Material.VOID_AIR) {
            y--;
            if (y == 0)
                return 0;
        }
        return y;
    }

    // Returns a random online player
    public static Player getRandomPlayer() {
        for (Player player : Bukkit.getOnlinePlayers())
            return player;

        return null;
    }

    public static boolean inside(Point2D.Double p, ArrayList<Point2D.Double> polygon) {
        int intersections = 0;
        Point2D.Double prev = polygon.get(polygon.size() - 1);
        for (Point2D.Double next : polygon) {
            if ((prev.y <= p.y && p.y < next.y) || (prev.y >= p.y && p.y > next.y)) {
                double dy = next.y - prev.y;
                double dx = next.x - prev.x;
                double x = (p.y - prev.y) / dy * dx + prev.x;
                if (x > p.x) {
                    intersections++;
                }
            }
            prev = next;
        }
        return intersections % 2 == 1;
    }

}
