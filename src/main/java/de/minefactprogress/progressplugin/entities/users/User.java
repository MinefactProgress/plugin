package de.minefactprogress.progressplugin.entities.users;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.api.Routes;
import de.minefactprogress.progressplugin.entities.city.Block;
import de.minefactprogress.progressplugin.utils.Constants;
import de.minefactprogress.progressplugin.utils.CustomColors;
import de.minefactprogress.progressplugin.utils.Item;
import de.minefactprogress.progressplugin.utils.Permissions;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

@Getter
@Setter
public class User implements Comparable<User> {

    private int uid;
    private String username;
    @SerializedName("mc_uuid")
    private UUID uuid;
    private int permission;
    private Rank rank;
    private ArrayList<UserSetting> settings;

    public ItemStack toItemStack(boolean hasTeleportPermission) {
        ArrayList<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(ChatColor.GRAY + "Status: " + (!isOnline() ? ChatColor.RED + "Offline" : ChatColor.GREEN + "Online"));
        lore.add(ChatColor.GRAY + "Rank: " + rank);
        lore.add(ChatColor.GRAY + "Claims: " + ChatColor.AQUA + countClaims());
        lore.add("");
        lore.add(ChatColor.YELLOW + "Click to view claims");

        if(isOnline()) {
            if (hasTeleportPermission) {
                lore.add(CustomColors.YELLOW.getChatColor() + "Right-Click to teleport to player");
            } else {
                lore.add(CustomColors.YELLOW.getChatColor() + "Right-Click to request teleport to player");
            }
        }

        return Item.createPlayerHead((rank == null ? Rank.PLAYER : rank).getColor() + username, username, lore);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public Rank getRank() {
        return this.rank != null ? this.rank : Rank.PLAYER;
    }

    public boolean isStaff() {
        return rank != null && rank.isStaff();
    }

    public boolean isOnline() {
        return Bukkit.getPlayer(username) != null;
    }

    public int countClaims() {
        int claims = 0;
        for(Block block : API.getBlocks()) {
            if(block.getBuilders().contains(this.username)) {
                claims++;
            }
        }
        return claims;
    }

    public boolean hasDebugPerms() {
        return permission >= WebsitePermissions.ADMIN;
    }

    public String getSetting(SettingType key) {
        UserSetting setting = settings.stream().filter(s -> s.getKey().equalsIgnoreCase(key.name())).findFirst().orElse(null);
        return setting == null ? null : setting.getValue();
    }

    public void setSetting(SettingType key, String value) {
        settings.stream().filter(s -> s.getKey().equalsIgnoreCase(key.name())).findFirst().ifPresent(setting -> {
            setting.setValue(value);
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                JsonObject json = new JsonObject();
                json.addProperty("key", key.name());
                json.addProperty("value", value);

                API.POST(Routes.USERS + "/" + uid + "/settings", json, res -> {
                    getPlayer().sendMessage(Constants.PREFIX + ChatColor.GRAY + "Successfully "
                            + (value.equals("true") ? ChatColor.GREEN + "enabled " : ChatColor.RED + "disabled ") + ChatColor.GRAY + key.getName());
                }, getPlayer());
            });
        });
    }

    @Override
    public String toString() {
        return (rank != null ? rank.getColor() : Rank.PLAYER.getColor()) + username;
    }

    public static User getUserByUUID(UUID uuid) {
        for(User user : API.getUsers()) {
            if(user.uuid != null && user.uuid.equals(uuid)) {
                return user;
            }
        }
        return null;
    }

    public static User getUserByName(String name) {
        for(User user : API.getUsers()) {
            if(user.username.equals(name)) {
                return user;
            }
        }
        return null;
    }

    public static User getUserById(int name) {
        for(User user : API.getUsers()) {
            if(user.uid == name) {
                return user;
            }
        }
        return null;
    }

    @Override
    public int compareTo(@NotNull User u) {
        if(rank.equals(u.rank)) {
            return username.compareTo(u.username);
        }
        return rank.getPriority() - u.rank.getPriority();
    }

    public enum Sorting implements Comparator<User> {
        RANK {
            @Override
            public int compare(User u1, User u2) {
                if(u1.getRank().getPriority() == u2.getRank().getPriority()) {
                    return u1.getUsername().toLowerCase().compareTo(u2.getUsername().toLowerCase());
                }
                return u1.getRank().getPriority() - u2.getRank().getPriority();
            }
        },
        NAME {
            @Override
            public int compare(User u1, User u2) {
                return u1.getUsername().toLowerCase().compareTo(u2.getUsername().toLowerCase());
            }
        },
        CLAIMS {
            @Override
            public int compare(User u1, User u2) {
                return u2.countClaims() - u1.countClaims();
            }
        }
    }

    private static class WebsitePermissions {
        public static final int DEFAULT = 0;
        public static final int EVENT = 1;
        public static final int BUILDER = 2;
        public static final int MODERATOR = 3;
        public static final int ADMIN = 4;
    }
}
