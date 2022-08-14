package de.minefactprogress.progressplugin.components;

import de.minefactprogress.progressplugin.Main;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class NoClipHandler {

    @Getter
    private static final NoClipHandler instance;
    @Getter
    private final ArrayList<UUID> noClipPlayers;
    private final double delta = 0.4;

    static {
        instance = new NoClipHandler();
    }

    private NoClipHandler() {
        noClipPlayers = new ArrayList<>();

        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this::checkForBlocks, 1L, 1L);
    }

    public void addPlayer(Player player) {
        noClipPlayers.add(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        noClipPlayers.remove(player.getUniqueId());
    }

    private void checkForBlocks() {
        for(UUID uuid : noClipPlayers) {
            Player p = Bukkit.getPlayer(uuid);
            if(p != null && p.isOnline()) {
                if(p.getGameMode().equals(GameMode.CREATIVE)) {
                    boolean noClip;
                    if(p.getLocation().add(0, -0.1, 0).getBlock().getType().isCollidable() && p.isSneaking()) {
                        noClip = true;
                    } else {
                        noClip = isNoClip(p);
                    }
                    if(noClip) {
                        p.setGameMode(GameMode.SPECTATOR);
                    }
                } else if(p.getGameMode().equals(GameMode.SPECTATOR)) {
                    boolean noClip;
                    if(p.getLocation().add(0, -0.1, 0).getBlock().getType().isCollidable()) {
                        noClip = true;
                    } else {
                        noClip = isNoClip(p);
                    }
                    if(!noClip) {
                        p.setGameMode(GameMode.CREATIVE);
                    }
                }
            }
        }
    }

    private boolean isNoClip(Player p) {
        boolean noClip = false;

        if(p.getLocation().add(+delta, 0, 0).getBlock().getType().isCollidable()) {
            noClip = true;
        } else if(p.getLocation().add(-delta, 0, 0).getBlock().getType().isCollidable()) {
            noClip = true;
        } else if(p.getLocation().add(0, 0, +delta).getBlock().getType().isCollidable()) {
            noClip = true;
        } else if(p.getLocation().add(0, 0, -delta).getBlock().getType().isCollidable()) {
            noClip = true;
        } else if(p.getLocation().add(+delta, 1, 0).getBlock().getType().isCollidable()) {
            noClip = true;
        } else if(p.getLocation().add(-delta, 1, 0).getBlock().getType().isCollidable()) {
            noClip = true;
        } else if(p.getLocation().add(0, 1, +delta).getBlock().getType().isCollidable()) {
            noClip = true;
        } else if(p.getLocation().add(0, 1, -delta).getBlock().getType().isCollidable()) {
            noClip = true;
        } else if(p.getLocation().add(0, +1.9, 0).getBlock().getType().isCollidable()) {
            noClip = true;
        }

        return noClip;
    }
}
