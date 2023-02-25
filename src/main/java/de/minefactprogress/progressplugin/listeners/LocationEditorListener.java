package de.minefactprogress.progressplugin.listeners;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.components.LocationEditor;
import de.minefactprogress.progressplugin.menusystem.menus.locationeditor.DistrictSelection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LocationEditorListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        LocationEditor locationEditor = LocationEditor.get(p);

        if(locationEditor != null && item != null && item.hasItemMeta()) {
            String displayName = PlainTextComponentSerializer.plainText().serializeOrNull(item.getItemMeta().displayName());
            if(displayName != null && e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                assert displayName != null;
                if(displayName.equals(ChatColor.stripColor(LocationEditor.NAME_SELECT_DISTRICT))) {
                    new DistrictSelection(Main.getInstance().getMenuStorage(p), null).open();
                } else if(displayName.equals(ChatColor.stripColor(LocationEditor.NAME_PREVIOUS_BLOCK))) {

                } else if(displayName.equals(ChatColor.stripColor(LocationEditor.NAME_NEXT_BLOCK))) {

                } else if(displayName.contains(ChatColor.stripColor(LocationEditor.NAME_DYNAMIC_HEIGHT))) {
                    ItemMeta meta = item.getItemMeta();
                    if(displayName.contains("Disabled")) {
                        locationEditor.setFixedHeight(null);
                        meta.displayName(Component.text(LocationEditor.NAME_DYNAMIC_HEIGHT + ChatColor.GREEN + "Enabled"));
                        item.setItemMeta(meta);
                    } else if(displayName.contains("Enabled")) {
                        locationEditor.setFixedHeight(p.getLocation().getY());
                        meta.displayName(Component.text(LocationEditor.NAME_DYNAMIC_HEIGHT + ChatColor.RED + "Disabled"));
                        item.setItemMeta(meta);
                    }
                } else if(displayName.equals(ChatColor.stripColor(LocationEditor.NAME_RESET_SELECTION))) {
                    locationEditor.clearSelection();
                } else if(displayName.equals(ChatColor.stripColor(LocationEditor.NAME_LEAVE_EDITOR))) {
                    locationEditor.destroy();
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        LocationEditor locationEditor = LocationEditor.get(p);

        if(locationEditor != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        LocationEditor locationEditor = LocationEditor.get(p);

        if(locationEditor != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();

        if(entity instanceof ArmorStand as && LocationEditor.isUsedArmorstandGlobal(as)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();

        if(entity instanceof ArmorStand as && LocationEditor.isUsedArmorstandGlobal(as)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();
        Entity target = e.getRightClicked();
        LocationEditor locationEditor = LocationEditor.get(p);

        if(locationEditor != null && target instanceof ArmorStand as && locationEditor.isUsedArmorstand(as)) {
            e.setCancelled(true);
            locationEditor.addPoint(as.getLocation());
        }
    }
}
