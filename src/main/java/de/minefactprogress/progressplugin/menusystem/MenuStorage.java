package de.minefactprogress.progressplugin.menusystem;

import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.entities.city.District;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

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
}
