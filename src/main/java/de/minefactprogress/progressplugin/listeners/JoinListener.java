package de.minefactprogress.progressplugin.listeners;

import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.utils.conversion.CoordinateConversion;
import de.minefactprogress.progressplugin.utils.conversion.projection.OutOfProjectionBoundsException;
import lombok.SneakyThrows;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @SneakyThrows
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Main.getDistrictBossbar().addPlayer(e.getPlayer());
        e.getPlayer().sendMessage("welcome");
        try {;
            double[] pos = CoordinateConversion.convertFromGeo(-73.98937085087103,40.740699535030515);
            e.getPlayer().sendMessage(pos[0]+","+pos[1]);
        } catch (OutOfProjectionBoundsException outOfProjectionBoundsException) {
            outOfProjectionBoundsException.printStackTrace();
        }
    }
}
