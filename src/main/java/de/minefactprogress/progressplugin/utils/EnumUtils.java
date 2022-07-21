package de.minefactprogress.progressplugin.utils;

public class EnumUtils {

    public static <E extends Enum<E>> E getByID(Class<E> clazz, int id) {
        return clazz.getEnumConstants()[id];
    }

    public static <E extends Enum<E>> int getNextID(Class<E> clazz, int id) {
        return getNextID(clazz, id, 0);
    }

    public static <E extends Enum<E>> int getNextID(Class<E> clazz, int id, int start) {
        int length = clazz.getEnumConstants().length - 1;
        return id + 1 - start > length ? 0 : id + 1;
    }

    public static <E extends Enum<E>> int getPreviousID(Class<E> clazz, int id) {
        return getPreviousID(clazz, id, 0);
    }

    public static <E extends Enum<E>> int getPreviousID(Class<E> clazz, int id, int start) {
        int length = clazz.getEnumConstants().length - 1;
        return id - 1 < 0 ? length + start : id - 1;
    }
}
