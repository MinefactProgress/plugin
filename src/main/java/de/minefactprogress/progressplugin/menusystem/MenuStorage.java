package de.minefactprogress.progressplugin.menusystem;

import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.District;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
@Getter
@Setter
public class MenuStorage {

    private final Player owner;
    private District borough;
    private District subborough;
    private District district;
    private Block block;
    private boolean editDetails;

    // BannerCreator
    private ItemStack banner;
    private DyeColor patternColor;
}
