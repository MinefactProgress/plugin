package de.minefactprogress.progressplugin.utils;

import org.bukkit.ChatColor;

public class Constants {

    // API
    // TODO
    // private static final String BASE_URL = "https://progressbackend.minefact.de";
    public static final String BASE_URL = "http://localhost:8080";
    public static final String API_URL = BASE_URL + "/v1";
    public static final String API_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjEsImlhdCI6MTY3NTA5NDQ0N30.Fsh5FYVvA6VzDFqoIRfh_fXAKm2hG3GiFCMVJOeG9GI";

    // Prefixes
    public static final String PREFIX = "§7[§bProgress§7] §r";
    public static final String PREFIX_DEBUG = "§7[§4Progress§7] §c";
    public static final String PREFIX_SERVER = ChatColor.GOLD + "Server >> " + ChatColor.RESET;
}
