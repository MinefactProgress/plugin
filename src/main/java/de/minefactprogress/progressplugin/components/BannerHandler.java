package de.minefactprogress.progressplugin.components;

import de.minefactprogress.progressplugin.utils.Item;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class BannerHandler {

    public static ItemStack createBanner(Component name, DyeColor color, ArrayList<Pattern> patterns) {
        ItemStack item = new Item(Material.getMaterial(color.name() + "_BANNER")).build();
        BannerMeta meta = (BannerMeta) item.getItemMeta();
        meta.displayName(name);
        meta.setPatterns(patterns);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addPattern(ItemStack banner, Pattern pattern) {
        ItemStack item = new ItemStack(banner);
        BannerMeta meta = (BannerMeta) banner.getItemMeta();
        meta.addPattern(pattern);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createDye(Component name, DyeColor color) {
        ItemStack item = new Item(Material.getMaterial(color.name() + "_DYE")).build();
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);

        item.setItemMeta(meta);
        return item;
    }

    public static DyeColor getDyeColorByName(String name) {
        for (DyeColor color : DyeColor.values()) {
            if (color.name().equalsIgnoreCase(name)) {
                return color;
            }
        }
        return null;
    }
}
