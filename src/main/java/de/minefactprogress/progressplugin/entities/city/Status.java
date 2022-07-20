package de.minefactprogress.progressplugin.entities.city;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

@RequiredArgsConstructor
@Getter
public enum Status {

    NOT_STARTED(0, "Not Started", ChatColor.RED),
    RESERVED(1, "Reserved", ChatColor.BLUE),
    BUILDING(2, "Building", ChatColor.GOLD),
    DETAILING(3, "Detailing", ChatColor.YELLOW),
    DONE(4, "Done", ChatColor.GREEN);

    private final int id;
    private final String name;
    private final ChatColor color;

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
        return color + name;
    }
}
