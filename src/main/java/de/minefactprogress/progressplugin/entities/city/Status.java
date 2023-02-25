package de.minefactprogress.progressplugin.entities.city;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

@RequiredArgsConstructor
@Getter
public enum Status {

    @SerializedName("0")
    NOT_STARTED(0, "Not Started", "RED"),
    @SerializedName("1")
    RESERVED(1, "Reserved", "BLUE"),
    @SerializedName("2")
    BUILDING(2, "Building", "GOLD"),
    @SerializedName("3")
    DETAILING(3, "Detailing", "YELLOW"),
    @SerializedName("4")
    DONE(4, "Done", "GREEN");

    private final int id;
    private final String name;
    private final String color;

    public static Status getByID(int id) {
        for (Status status : Status.values()) {
            if (status.id == id) {
                return status;
            }
        }
        return null;
    }

    public ChatColor getChatColor() {
        return ChatColor.valueOf(color);
    }

    @Override
    public String toString() {
        return getChatColor() + name;
    }
}
