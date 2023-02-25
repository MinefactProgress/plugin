package de.minefactprogress.progressplugin.components;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardHandler {

    public static void sendScoreboard(Player p) {
        LocationEditor locationEditor = LocationEditor.get(p);
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = scoreboard.getObjective("aaa") != null ? scoreboard.getObjective("aaa") : scoreboard.registerNewObjective("aaa", "bbb");

        if(obj == null || locationEditor == null) return;

        obj.displayName(Component.text(ChatColor.AQUA + "LocationEditor"));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Team district = scoreboard.registerNewTeam("district");
        Team block = scoreboard.registerNewTeam("block");
        Team points = scoreboard.registerNewTeam("points");

        obj.getScore("   ").setScore(8);
        obj.getScore(ChatColor.WHITE + "District" + ChatColor.GRAY + ": ").setScore(7);
        obj.getScore("§d").setScore(6);
        obj.getScore("  ").setScore(5);
        obj.getScore(ChatColor.WHITE + "Block" + ChatColor.GRAY + ": ").setScore(4);
        obj.getScore("§e").setScore(3);
        obj.getScore(" ").setScore(2);
        obj.getScore(ChatColor.WHITE + "Selected Points" + ChatColor.GRAY + ": ").setScore(1);
        obj.getScore("§f").setScore(0);

        district.addEntry("§d");
        district.prefix(Component.text(ChatColor.YELLOW + (locationEditor.getDistrict() == null ? "None" : locationEditor.getDistrict().getName())));

        block.addEntry("§e");
        block.prefix(Component.text(ChatColor.YELLOW + "" + (locationEditor.getBlockID() == 0 ? "None" : locationEditor.getBlockID())));

        points.addEntry("§f");
        points.prefix(Component.text(ChatColor.YELLOW + "" + locationEditor.countSelectedPoints() + ChatColor.GRAY + " (" + locationEditor.countVisiblePoints() + ")"));

        p.setScoreboard(scoreboard);
    }

    public static void updateScoreboard(Player p) {
        LocationEditor locationEditor = LocationEditor.get(p);
        Scoreboard scoreboard = p.getScoreboard();

        Team district = scoreboard.getTeam("district");
        Team block = scoreboard.getTeam("block");
        Team points = scoreboard.getTeam("points");

        if(district == null || block == null || points == null) return;

        district.prefix(Component.text(ChatColor.YELLOW + (locationEditor.getDistrict() == null ? "None" : locationEditor.getDistrict().getName())));
        block.prefix(Component.text(ChatColor.YELLOW + "" + (locationEditor.getBlockID() == 0 ? "None" : locationEditor.getBlockID())));
        points.prefix(Component.text(ChatColor.YELLOW + "" + locationEditor.countSelectedPoints() + ChatColor.GRAY + " (" + locationEditor.countVisiblePoints() + ")"));
    }
}
