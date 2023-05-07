package de.minefactprogress.progressplugin.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;


public class Permissions {
    public static String PermOwner = "server.owner";
    public static String PermAdmin = "server.admin";
    public static String PermModerator = "server.moderator";
    public static String PermSupporter = "server.supporter";
    public static String PermPremium = "server.premium";
    public static String PermYoutuber = "server.youtuber";
    public static String PermBuilder = "server.builder";
    public static String PermDeveloper = "server.developer";
    public static String PermDonator = "server.donator";
    public static String PermAdvanced = "server.advanced";
    public static String PermProfessional = "server.professional";

    /**
     * Manual permission check since p.hasPermission() is always true if player is op
     * @param p - the player
     * @param permission - the permission to check
     * @return - whether the player has the permission or not
     */
    public static boolean hasPermission(Player p, String permission) {
        return p.getEffectivePermissions()
                .stream()
                .anyMatch(perm -> perm.getPermission().equals(permission));
    }

    public static boolean isMember(Player p) {
        return (
                !hasPermission(p, Permissions.PermPremium)
                        && !hasPermission(p, Permissions.PermAdmin)
                        && !hasPermission(p, Permissions.PermDeveloper)
                        && !hasPermission(p, Permissions.PermSupporter)
                        && !hasPermission(p, Permissions.PermOwner)
                        && !hasPermission(p, Permissions.PermBuilder)
                        && !hasPermission(p, Permissions.PermYoutuber)
                        && !hasPermission(p, Permissions.PermModerator)
        );
    }

    public static boolean isTeamMember(Player p) {
        return (
                hasPermission(p, Permissions.PermAdmin)
                        || hasPermission(p, Permissions.PermDeveloper)
                        || hasPermission(p, Permissions.PermSupporter)
                        || hasPermission(p, Permissions.PermOwner)
                        || hasPermission(p, Permissions.PermBuilder)
                        || hasPermission(p, Permissions.PermYoutuber)
                        || hasPermission(p, Permissions.PermModerator)
        );
    }

    public static String getPrefixColorString(String permission) {

        if (permission.equals(PermOwner)) {
            return "§4";
        } else if (permission.equals(PermAdmin)) {
            return "§c";
        } else if (permission.equals(PermModerator)) {
            return "§3";
        } else if (permission.equals(PermDeveloper)) {
            return "§b";
        } else if (permission.equals(PermSupporter)) {
            return "§9";
        } else if (permission.equals(PermBuilder)) {
            return "§1";
        } else if (permission.equals(PermYoutuber)) {
            return "§5";
        } else if (permission.equals(PermPremium)) {
            return "§6";
        } else {
            return "§a";
        }
    }

    public static String getPrefixColorString(Player p) {
        if (hasPermission(p, PermAdmin)) {
            return "§c";
        } else if (hasPermission(p, PermDeveloper)) {
            return "§b";
        } else if (hasPermission(p, PermSupporter)) {
            return "§9";
        } else if (hasPermission(p, PermBuilder)) {
            return "§1";
        } else if (hasPermission(p, PermYoutuber)) {
            return "§5";
        } else if (hasPermission(p, PermPremium)) {
            return "§6";
        } else {
            return "§a";
        }
    }

    public static ChatColor getPrefixChatColor(Player p) {
        if (hasPermission(p, PermAdmin)) {
            return ChatColor.RED;
        } else if (hasPermission(p, PermDeveloper)) {
            return ChatColor.AQUA;
        } else if (hasPermission(p, PermSupporter)) {
            return ChatColor.BLUE;
        } else if (hasPermission(p, PermBuilder)) {
            return ChatColor.DARK_BLUE;
        } else if (hasPermission(p, PermYoutuber)) {
            return ChatColor.DARK_PURPLE;
        } else if (hasPermission(p, PermPremium)) {
            return ChatColor.GOLD;
        } else {
            return ChatColor.GREEN;
        }
    }

    public static Color getPrefixColor(Player p) {
        if (hasPermission(p, PermAdmin)) {
            return Color.RED;
        } else if (hasPermission(p, PermDeveloper)) {
            return Color.AQUA;
        } else if (hasPermission(p, PermSupporter)) {
            return Color.BLUE;
        } else if (hasPermission(p, PermBuilder)) {
            return Color.NAVY;
        } else if (hasPermission(p, PermYoutuber)) {
            return Color.PURPLE;
        } else if (hasPermission(p, PermPremium)) {
            return Color.ORANGE;
        } else {
            return Color.LIME;
        }
    }
}
