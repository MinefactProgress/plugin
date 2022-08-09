package de.minefactprogress.progressplugin.menusystem.menus.confirmation;

import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.menusystem.ConfirmMenu;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import org.bukkit.entity.Player;

public class DetailsConfirmMenu extends ConfirmMenu {

    public DetailsConfirmMenu(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
    }

    @Override
    public void handleConfirm() {
        Player p = menuStorage.getOwner();
        Block block = menuStorage.getBlock();

        block.setDetails(menuStorage.isEditDetails(), p);
        p.closeInventory();
    }

    @Override
    public void handleCancel() {
        menuStorage.getOwner().closeInventory();
    }
}
