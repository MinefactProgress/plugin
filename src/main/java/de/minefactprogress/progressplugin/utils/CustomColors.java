package de.minefactprogress.progressplugin.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
@RequiredArgsConstructor
@Getter
public enum CustomColors {

    BLUE("#28A8D3"),
    YELLOW("#ffff91");

    private final String hexColor;

    public TextColor getComponentColor() {
        return TextColor.fromHexString(hexColor);
    }

    public ChatColor getChatColor() {
        return ChatColor.of(hexColor);
    }
}
