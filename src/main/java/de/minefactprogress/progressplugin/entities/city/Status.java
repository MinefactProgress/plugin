package de.minefactprogress.progressplugin.entities.city;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

@RequiredArgsConstructor
@Getter
public enum Status {

    NOT_STARTED(0, "Not Started", "RED"),
    RESERVED(1, "Reserved", "BLUE"),
    BUILDING(2, "Building", "GOLD"),
    DETAILING(3, "Detailing", "YELLOW"),
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

    @Override
    public String toString() {
        return ChatColor.valueOf(color) + name;
    }
}
