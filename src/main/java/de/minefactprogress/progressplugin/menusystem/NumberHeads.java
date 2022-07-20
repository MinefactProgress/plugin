package de.minefactprogress.progressplugin.menusystem;

import de.minefactprogress.progressplugin.utils.Item;
import org.bukkit.inventory.ItemStack;

public enum NumberHeads {
    _1(1, "11264"),
    _2(2, "11263"),
    _3(3, "11262"),
    _4(4, "11261"),
    _5(5, "11260"),
    _6(6, "11259"),
    _7(7, "11258"),
    _8(8, "11257"),
    _9(9, "11256"),
    _10(10, "11255"),
    _11(11, "11254"),
    _12(12, "11253"),
    _13(13, "11252"),
    _14(14, "11251"),
    _15(15, "11250"),
    _16(16, "11249"),
    _17(17, "11248"),
    _18(18, "11247"),
    _19(19, "11246"),
    _20(20, "12245"),
    _21(21, "12960"),
    _22(22, "12959"),
    _23(23, "12958"),
    _24(24, "12957"),
    _25(25, "12956"),
    BLANK(0, "11238");

    private final int number;
    private final ItemStack head;

    NumberHeads(int number, String headID) {
        this.number = number;
        this.head = Item.createCustomHead(headID, "§7Current Page §f- §e" + number, null);
    }

    public static NumberHeads getFromNumber(int number) {
        for (NumberHeads head : NumberHeads.values()) {
            if (head.getNumber() == number) {
                return head;
            }
        }
        return BLANK;
    }

    public int getNumber() {
        return number;
    }

    public ItemStack getHead() {
        return head;
    }
}
