package de.minefactprogress.progressplugin.entities.users;

import com.google.gson.annotations.SerializedName;
import de.minefactprogress.progressplugin.utils.Permissions;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public enum Rank {

    @SerializedName("Owner")
    OWNER(1, "Owner", ChatColor.DARK_RED, Permissions.PermOwner, true),
    @SerializedName("Administrator")
    ADMINISTRATOR(2, "Administrator", ChatColor.RED, Permissions.PermAdmin, true),
    @SerializedName("Moderator")
    MODERATOR(3, "Moderator", ChatColor.DARK_AQUA, Permissions.PermModerator, true),
    @SerializedName("Developer")
    DEVELOPER(4, "Developer", ChatColor.AQUA, Permissions.PermDeveloper, true),
    @SerializedName("Supporter")
    SUPPORTER(5, "Supporter", ChatColor.BLUE, Permissions.PermSupporter, true),
    @SerializedName("Architect")
    ARCHITECT(6, "Architect", ChatColor.DARK_BLUE, Permissions.PermBuilder, true),
    @SerializedName("Communication")
    COMMUNICATION(7, "Communication", ChatColor.DARK_PURPLE, Permissions.PermYoutuber, false),
    @SerializedName("Premium")
    PREMIUM(8, "Premium", ChatColor.GOLD, Permissions.PermPremium, false),
    @SerializedName("Professional")
    PROFESSIONAL(9, "Professional", ChatColor.GREEN, Permissions.PermProfessional, false),
    @SerializedName("Advanced")
    ADVANCED(10, "Advanced", ChatColor.GREEN, Permissions.PermAdvanced, false),
    @SerializedName("Player")
    PLAYER(11, "Player", ChatColor.GREEN, "", false)
    ;

    private final int priority;
    private final String name;
    private final ChatColor color;
    private final String permission;
    private final boolean isStaff;

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public static Rank getByPriority(int priority) {
        for(Rank rank : Rank.values()) {
            if(rank.getPriority() == priority) {
                return rank;
            }
        }
        return PLAYER;
    }

    public static Rank getByName(String name) {
        for(Rank rank : Rank.values()) {
            if(rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return PLAYER;
    }

    public static Rank getByPermission(Player p) {
        for(Rank rank : Rank.values()) {
            if(Permissions.hasPermission(p, rank.permission)) {
                return rank;
            }
        }
        return PLAYER;
    }

    @Override
    public String toString() {
        return color + name;
    }


}
