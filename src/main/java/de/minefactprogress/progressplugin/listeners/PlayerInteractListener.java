package de.minefactprogress.progressplugin.listeners;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.menusystem.menus.NewYorkCityMenu;
import de.minefactprogress.progressplugin.utils.ProgressUtils;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();

        if(item == null || item.getItemMeta() == null) return;

        if(e.getAction().isRightClick()) {
            String itemName = PlainTextComponentSerializer.plainText().serializeOrNull(item.getItemMeta().displayName());
            if(item.getType() == Material.EMERALD && itemName != null && itemName.equals("Progress")) {
                e.setCancelled(true);
                if(ProgressUtils.checkForData(p)) {
                    new NewYorkCityMenu(Main.getInstance().getMenuStorage(p), null).open();
                }
            }
        }
    }
}
