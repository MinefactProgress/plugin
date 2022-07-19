package de.minefactprogress.progressplugin.entities.users;

import de.minefactprogress.progressplugin.utils.Permissions;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public enum Rank {

    OWNER(1, "Owner", ChatColor.DARK_RED, Permissions.PermOwner),
    ADMINISTRATOR(2, "Administrator", ChatColor.RED, Permissions.PermAdmin),
    MODERATOR(3, "Moderator", ChatColor.DARK_AQUA, Permissions.PermModerator),
    DEVELOPER(4, "Developer", ChatColor.AQUA, Permissions.PermDeveloper),
    SUPPORTER(5, "Supporter", ChatColor.BLUE, Permissions.PermSupporter),
    ARCHITECT(6, "Architect", ChatColor.DARK_BLUE, Permissions.PermBuilder),
    COMMUNICATION(7, "Communication", ChatColor.DARK_PURPLE, Permissions.PermYoutuber),
    PREMIUM(8, "Premium", ChatColor.GOLD, Permissions.PermPremium),
    PLAYER(9, "Player", ChatColor.GREEN, "")
    ;

    private final int priority;
    private final String name;
    private final ChatColor color;
    private final String permission;

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

    public static Rank getByPermission(Player p) {
        for(Rank rank : Rank.values()) {
            if(p.hasPermission(rank.permission)) {
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
