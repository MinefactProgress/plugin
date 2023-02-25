package de.minefactprogress.progressplugin.components;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParticleDrawer {

    private static final double STEP_SIZE = 0.1;

    public static void drawLine(Player p, Location start, Location end) {
        Location loc = start.clone();
        Vector direction = end.toVector().subtract(start.toVector()).normalize();
        double distance = start.distance(end) * STEP_SIZE;

        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1.0f);
        for(double i = 0; i < distance; i += STEP_SIZE) {
            p.spawnParticle(Particle.REDSTONE, loc, 1, dustOptions);
            loc.add(direction);
        }
    }
}
