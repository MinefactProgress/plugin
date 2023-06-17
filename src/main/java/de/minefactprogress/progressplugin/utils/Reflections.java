package de.minefactprogress.progressplugin.utils;

import de.minefactprogress.progressplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Reflections {

    public static String convertLocationToString(Location loc, boolean XYZinInteger, boolean containsDirection) {
        if (loc == null)
            return null;

        String location = loc.getWorld().getName() + "/";

        if (!XYZinInteger)
            location = location + loc.getX() + "/" + loc.getY() + "/" + loc.getZ();
        else
            location = location + loc.getBlockX() + "/" + loc.getBlockY() + "/" + loc.getBlockZ();

        if (containsDirection)
            location = location + "/" + loc.getYaw() + "/" + loc.getPitch();

        return location;
    }

    public static Location convertStringToLocation(String loc) {
        if (loc == null)
            return null;

        String[] data = loc.split("/");
        Location location;

        if (data.length == 4)
            location = new Location(Bukkit.getWorld(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]));
        else if (data.length == 6)
            location = new Location(Bukkit.getWorld(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]), Float.parseFloat(data[4]), Float.parseFloat(data[5]));
        else
            return null;

        return location;
    }

    public static String itemStackToBase64(ItemStack item) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOut = new BukkitObjectOutputStream(out);

            dataOut.writeObject(item);
            dataOut.close();
            return Base64Coder.encodeLines(out.toByteArray());
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(Main.getPREFIX() + ChatColor.RED + "There was an error while converting an itemstack to base64");
            return null;
        }
    }

    public static ItemStack itemStackFromBase64(String base64) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(Base64Coder.decodeLines(base64));
            BukkitObjectInputStream dataIn = new BukkitObjectInputStream(in);
            ItemStack item = (ItemStack) dataIn.readObject();

            dataIn.close();
            return item;
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(Main.getPREFIX() + ChatColor.RED + "There was an error while converting an base64 to an itemstack");
            return null;
        }
    }
}
