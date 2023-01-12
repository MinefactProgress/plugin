package de.minefactprogress.progressplugin.entities.users;

import com.google.gson.JsonObject;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.RequestHandler;
import de.minefactprogress.progressplugin.utils.Logger;
import de.minefactprogress.progressplugin.utils.Permissions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class User implements Comparable<User> {

    public static final ArrayList<User> users = new ArrayList<>();

    private final UUID uuid;
    private final String name;
    private final Rank rank;
    private final int permissionLevel;

    public User(JsonObject json) {
        this.uuid = UUID.fromString(json.get("minecraft").getAsJsonObject().get("uuid").getAsString());
        this.name = json.get("minecraft").getAsJsonObject().get("username").getAsString();
        this.rank = Rank.getByName(json.get("minecraft").getAsJsonObject().get("rank").getAsString());
        this.permissionLevel = json.get("permission").getAsInt();
    }

    public User(UUID uniqueId, String name, Rank rank) {
        this.uuid = uniqueId;
        this.name = name;
        this.rank = rank;
        // TODO: is this right?
        this.permissionLevel = 0;
    }


    @Override
    public int compareTo(User u) {
        if(rank.equals(u.rank)) {
            return name.compareTo(u.name);
        }
        return rank.getPriority() - u.rank.getPriority();
    }

    @Override
    public String toString() {
        return rank.getColor() + name;
    }

    // -----===== Static Methods =====-----

    public static User getByUUID(UUID uuid) {
        return users.stream().filter(u -> u.uuid.equals(uuid)).findFirst().orElse(null);
    }

    public static User getByName(String name) {
        return users.stream().filter(u -> u.name.equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static void register(Player p) {
        if(Permissions.isTeamMember(p)) {
            Rank rank = Rank.getByPermission(p);
            JsonObject json = new JsonObject();
            json.addProperty("uuid", p.getUniqueId().toString());
            json.addProperty("username", p.getName());
            json.addProperty("rank", rank != null ? rank.getName() : "Player");

            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                JsonObject res = RequestHandler.getInstance().POST("api/minecraft/users/register", json);
                if(!res.get("error").getAsBoolean()) {
                    Logger.info("Successfully registered player " + p.getName() + " with rank " + (rank != null ? rank.getName() : "Player"));
                } else {
                    Logger.error("§cAn error occurred while registering Player §e" + p.getName(), json);
                }
            });
        }
    }

    // -----===== Sorting Enum =====-----

    public enum Sorting implements Comparator<User> {
        RANK {
            @Override
            public int compare(User u1, User u2) {
                if(u1.getRank().getPriority() == u2.getRank().getPriority()) {
                    return u1.getName().toLowerCase().compareTo(u2.getName().toLowerCase());
                }
                return u1.getRank().getPriority() - u2.getRank().getPriority();
            }
        },
        NAME {
            @Override
            public int compare(User u1, User u2) {
                return u1.getName().toLowerCase().compareTo(u2.getName().toLowerCase());
            }
        },
        CLAIMS {
            @Override
            public int compare(User u1, User u2) {
                return 0;
            }
        }
    }
}
