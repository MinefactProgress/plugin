package de.minefactprogress.progressplugin.utils;

public class DateUtils {

    public static String formatDateFromISOString(String iso) {
        String[] split1 = iso.split("T");
        if (split1.length != 2) return null;

        String[] split2 = split1[0].split("-");
        if (split2.length != 3) return null;

        return split2[2] + "." + split2[1] + "." + split2[0];
    }
}
