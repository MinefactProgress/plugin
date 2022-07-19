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
import java.util.UUID;

@Getter
@AllArgsConstructor
public class User implements Comparable<User> {

    public static final ArrayList<User> users = new ArrayList<>();

    private final UUID uuid;
    private final String name;
    private final Rank rank;

    public User(JsonObject json) {
        this.uuid = UUID.fromString(json.get("uuid").getAsString());
        this.name = json.get("username").getAsString();
        this.rank = Rank.getByName(json.get("rank").getAsString());
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

    public static User getByUUID(UUID uuid) {
        return users.stream().filter(u -> u.uuid.equals(uuid)).findFirst().orElse(null);
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
}
