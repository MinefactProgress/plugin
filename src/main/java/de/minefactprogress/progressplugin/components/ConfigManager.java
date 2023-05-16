package de.minefactprogress.progressplugin.components;

import de.minefactprogress.progressplugin.utils.Config;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

public class ConfigManager {

    public static void setStandard() {
        FileConfiguration cfg = Config.getFileConfiguration("config");
        cfg.options().copyDefaults(true);
        cfg.addDefault("productionMode", false);

        try {
            cfg.save(Config.getFile("config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
