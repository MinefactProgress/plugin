package de.minefactprogress.progressplugin.entities.users;

import org.bukkit.ChatColor;

public enum Rank {

    OWNER(1, "Owner", ChatColor.DARK_RED),
    ADMINISTRATOR(2, "Administrator", ChatColor.RED),
    MODERATOR(3, "Moderator", ChatColor.DARK_AQUA),
    DEVELOPER(4, "Developer", ChatColor.AQUA),
    SUPPORTER(5, "Supporter", ChatColor.BLUE),
    ARCHITECT(6, "Architect", ChatColor.DARK_BLUE),
    COMMUNICATION(7, "Communication", ChatColor.DARK_PURPLE),
    PREMIUM(8, "Premium", ChatColor.GOLD),
    PLAYER(9, "Player", ChatColor.GREEN)
    ;

    private final int priority;
    private final String name;
    private final ChatColor color;

    Rank(int priority, String name, ChatColor color) {
        this.priority = priority;
        this.name = name;
        this.color = color;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public static Rank getByPriority(int priority) {
        for(Rank rank : Rank.values()) {
            if(rank.getPriority() == priority) {
                return rank;
            }
        }
        return null;
    }

    public static Rank getByName(String name) {
        for(Rank rank : Rank.values()) {
            if(rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return color + name;
    }


}
