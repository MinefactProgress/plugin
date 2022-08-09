package de.minefactprogress.progressplugin.menusystem.menus.confirmation;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.menusystem.ConfirmMenu;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AddBuilderConfirmMenu extends ConfirmMenu {

    public AddBuilderConfirmMenu(MenuStorage menuStorage, Menu previousMenu) {
        super(menuStorage, previousMenu);
    }

    @Override
    public void handleConfirm() {
        Player p = menuStorage.getOwner();

        if(menuStorage.getBlock().getBuilders().contains(p.getName())) {
            p.sendMessage(Main.getPREFIX() + ChatColor.RED + "You have already claimed this block");
            return;
        }

        menuStorage.getBlock().setBuilder(p.getName(), p, true);
        p.closeInventory();
    }

    @Override
    public void handleCancel() {
        menuStorage.getOwner().closeInventory();
    }
}
