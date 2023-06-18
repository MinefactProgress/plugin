package de.minefactprogress.progressplugin.components;

import de.minefactprogress.progressplugin.utils.CustomColors;
import de.minefactprogress.progressplugin.utils.Item;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BannerHandler {

    public static ItemStack createBanner(Component name, DyeColor color, ArrayList<Pattern> patterns) {
        ItemStack item = new Item(Material.getMaterial(color.name() + "_BANNER")).build();
        BannerMeta meta = (BannerMeta) item.getItemMeta();
        meta.displayName(name);
        meta.setPatterns(patterns);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addPattern(ItemStack banner, Pattern pattern) {
        ItemStack item = new ItemStack(banner);
        BannerMeta meta = (BannerMeta) banner.getItemMeta();
        meta.addPattern(pattern);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createDye(Component name, DyeColor color) {
        ItemStack item = new Item(Material.getMaterial(color.name() + "_DYE")).build();
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createDyeWithLore(Component name, DyeColor color, String loreLine) {
        ItemStack item = new Item(Material.getMaterial(color.name() + "_DYE")).build();
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        meta.lore(new ArrayList<>(Collections.singleton(Component.text(loreLine, CustomColors.of(String.format("#%02x%02x%02x", color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue()))))));

        item.setItemMeta(meta);
        return item;
    }

    public static DyeColor getDyeColorByName(String name) {
        for (DyeColor color : DyeColor.values()) {
            if (color.name().equalsIgnoreCase(name)) {
                return color;
            }
        }
        return null;
    }

    public enum Letter {
        A("9297",
                new PatternPart(PatternType.STRIPE_RIGHT, true),
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.STRIPE_MIDDLE, true),
                new PatternPart(PatternType.STRIPE_TOP, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        B("9296",
                new PatternPart(PatternType.STRIPE_RIGHT, true),
                new PatternPart(PatternType.STRIPE_BOTTOM, true),
                new PatternPart(PatternType.STRIPE_TOP, true),
                new PatternPart(PatternType.CURLY_BORDER, false),
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.STRIPE_MIDDLE, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        C("9295",
                new PatternPart(PatternType.STRIPE_TOP, true),
                new PatternPart(PatternType.STRIPE_BOTTOM, true),
                new PatternPart(PatternType.STRIPE_RIGHT, true),
                new PatternPart(PatternType.STRIPE_MIDDLE, false),
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        D("9294",
                new PatternPart(PatternType.STRIPE_RIGHT, true),
                new PatternPart(PatternType.STRIPE_BOTTOM, true),
                new PatternPart(PatternType.STRIPE_TOP, true),
                new PatternPart(PatternType.CURLY_BORDER, false),
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        E("9293",
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.STRIPE_TOP, true),
                new PatternPart(PatternType.STRIPE_MIDDLE, true),
                new PatternPart(PatternType.STRIPE_BOTTOM, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        F("9292",
                new PatternPart(PatternType.STRIPE_MIDDLE, true),
                new PatternPart(PatternType.STRIPE_RIGHT, false),
                new PatternPart(PatternType.STRIPE_TOP, true),
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        G("9291",
                new PatternPart(PatternType.STRIPE_RIGHT, true),
                new PatternPart(PatternType.HALF_HORIZONTAL, false),
                new PatternPart(PatternType.STRIPE_BOTTOM, true),
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.STRIPE_TOP, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        H("9290", true,
                new PatternPart(PatternType.STRIPE_TOP, false),
                new PatternPart(PatternType.STRIPE_BOTTOM, false),
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.STRIPE_RIGHT, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        I("9289",
                new PatternPart(PatternType.STRIPE_CENTER, true),
                new PatternPart(PatternType.STRIPE_TOP, true),
                new PatternPart(PatternType.STRIPE_BOTTOM, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        J("9288",
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.HALF_HORIZONTAL, false),
                new PatternPart(PatternType.STRIPE_BOTTOM, true),
                new PatternPart(PatternType.STRIPE_RIGHT, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        K("9287",
                new PatternPart(PatternType.STRIPE_DOWNRIGHT, true),
                new PatternPart(PatternType.HALF_HORIZONTAL, false),
                new PatternPart(PatternType.STRIPE_DOWNLEFT, true),
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        L("9286",
                new PatternPart(PatternType.STRIPE_BOTTOM, true),
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        M("9285",
                new PatternPart(PatternType.TRIANGLE_TOP, true),
                new PatternPart(PatternType.TRIANGLES_TOP, false),
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.STRIPE_RIGHT, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        N("9284",
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.TRIANGLE_TOP, false),
                new PatternPart(PatternType.STRIPE_DOWNRIGHT, true),
                new PatternPart(PatternType.STRIPE_RIGHT, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        O("9283",
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.STRIPE_RIGHT, true),
                new PatternPart(PatternType.STRIPE_BOTTOM, true),
                new PatternPart(PatternType.STRIPE_TOP, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        P("9282",
                new PatternPart(PatternType.STRIPE_RIGHT, true),
                new PatternPart(PatternType.HALF_HORIZONTAL_MIRROR, false),
                new PatternPart(PatternType.STRIPE_MIDDLE, true),
                new PatternPart(PatternType.STRIPE_TOP, true),
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        Q("9281", true,
                new PatternPart(PatternType.RHOMBUS_MIDDLE, false),
                new PatternPart(PatternType.STRIPE_RIGHT, true),
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.SQUARE_BOTTOM_RIGHT, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        R("9280",
                new PatternPart(PatternType.HALF_HORIZONTAL, true),
                new PatternPart(PatternType.STRIPE_CENTER, false),
                new PatternPart(PatternType.STRIPE_TOP, true),
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.STRIPE_DOWNRIGHT, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        S("9279", true,
                new PatternPart(PatternType.RHOMBUS_MIDDLE, false),
                new PatternPart(PatternType.STRIPE_MIDDLE, false),
                new PatternPart(PatternType.STRIPE_DOWNRIGHT, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        T("9278",
                new PatternPart(PatternType.STRIPE_TOP, true),
                new PatternPart(PatternType.STRIPE_CENTER, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        U("9277",
                new PatternPart(PatternType.STRIPE_BOTTOM, true),
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.STRIPE_RIGHT, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        V("9276",
                new PatternPart(PatternType.STRIPE_DOWNLEFT, true),
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.TRIANGLE_BOTTOM, false),
                new PatternPart(PatternType.STRIPE_DOWNLEFT, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        W("9275",
                new PatternPart(PatternType.TRIANGLE_BOTTOM, true),
                new PatternPart(PatternType.TRIANGLES_BOTTOM, false),
                new PatternPart(PatternType.STRIPE_LEFT, true),
                new PatternPart(PatternType.STRIPE_RIGHT, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        X("9274",
                new PatternPart(PatternType.CROSS, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        Y("9273",
                new PatternPart(PatternType.STRIPE_DOWNRIGHT, true),
                new PatternPart(PatternType.HALF_HORIZONTAL_MIRROR, false),
                new PatternPart(PatternType.STRIPE_DOWNLEFT, true),
                new PatternPart(PatternType.BORDER, false)
        ),
        Z("9272",
                new PatternPart(PatternType.STRIPE_TOP, true),
                new PatternPart(PatternType.STRIPE_DOWNLEFT, true),
                new PatternPart(PatternType.STRIPE_BOTTOM, true),
                new PatternPart(PatternType.BORDER, false)
        );

        private final String headID;
        private final List<PatternPart> patterns;
        private final boolean reverseBaseColor;

        Letter(String headID, PatternPart... patterns) {
            this(headID, false, patterns);
        }

        Letter(String headID, boolean reverseBaseColor, PatternPart... patterns) {
            this.patterns = new ArrayList<>(List.of(patterns));
            this.headID = headID;
            this.reverseBaseColor = reverseBaseColor;
        }

        public String getHeadID() {
            return headID;
        }

        public List<PatternPart> getPatternTypes() {
            return patterns;
        }

        public boolean isReverseBaseColor() {
            return reverseBaseColor;
        }

        public ItemStack getAsItemStack(DyeColor baseColor, DyeColor letterColor) {
            return createBanner(
                    Component.text(name()),
                    reverseBaseColor ? letterColor : baseColor,
                    new ArrayList<>(getPatternTypes().stream().map(pt -> new Pattern(pt.isLetter ? letterColor : baseColor, pt.patternType)).collect(Collectors.toList()))
            );
        }
    }

    @RequiredArgsConstructor @Getter
    public static class PatternPart {
        private final PatternType patternType;
        private final boolean isLetter;
    }
}
