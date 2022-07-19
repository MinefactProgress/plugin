package de.minefactprogress.progressplugin.entities.users;

import com.google.gson.JsonObject;
import lombok.Getter;

import java.util.ArrayList;
import java.util.UUID;

@Getter
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
}
