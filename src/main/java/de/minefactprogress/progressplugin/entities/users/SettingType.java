package de.minefactprogress.progressplugin.entities.users;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum SettingType {

    MINECRAFT_DISTRICT_BAR("District Bar"),
    MINECRAFT_MAP_VISIBLE("Map Visibility"),
    MINECRAFT_DEBUG_MODE("Debug Mode");

    private final String name;

    public static SettingType getByName(String name) {
        return Arrays.stream(SettingType.values()).filter(s -> s.name.equals(name)).findFirst().orElse(null);
    }
}
