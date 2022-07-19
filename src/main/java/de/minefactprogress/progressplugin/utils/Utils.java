package de.minefactprogress.progressplugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

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


}
