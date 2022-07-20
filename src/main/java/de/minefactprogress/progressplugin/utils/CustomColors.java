package de.minefactprogress.progressplugin.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.format.TextColor;

@RequiredArgsConstructor
@Getter
public enum CustomColors {

    BLUE(TextColor.fromHexString("#28A8D3")),
    YELLOW(TextColor.fromHexString("#ffff91"));

    private final TextColor color;
}
