package de.minefactprogress.progressplugin.entities;

import com.google.gson.annotations.SerializedName;
import de.minefactprogress.progressplugin.entities.users.User;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
public class Banner {

    private int id;
    private String name;
    @SerializedName("data")
    private ItemStack item;
    private User user;

    public ItemStack toItemStack() {
        ItemStack banner = item.clone();
        ItemMeta bannerMeta = banner.getItemMeta();

        bannerMeta.displayName(Component.text(ChatColor.GOLD + name));
        bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        bannerMeta.lore(new ArrayList<>(Arrays.asList(
                Component.text(ChatColor.DARK_GRAY + "ID: " + id),
                Component.text(""),
                Component.text(ChatColor.GRAY + "Created by: " + user)
        )));

        banner.setItemMeta(bannerMeta);
        return banner;
    }
}
