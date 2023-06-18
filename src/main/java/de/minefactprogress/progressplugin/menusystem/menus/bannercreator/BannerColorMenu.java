package de.minefactprogress.progressplugin.menusystem.menus.bannercreator;

import com.google.gson.JsonObject;
import de.minefactprogress.progressplugin.Main;
import de.minefactprogress.progressplugin.api.API;
import de.minefactprogress.progressplugin.api.Routes;
import de.minefactprogress.progressplugin.components.BannerHandler;
import de.minefactprogress.progressplugin.entities.Banner;
import de.minefactprogress.progressplugin.menusystem.Menu;
import de.minefactprogress.progressplugin.menusystem.MenuStorage;
import de.minefactprogress.progressplugin.utils.Constants;
import de.minefactprogress.progressplugin.utils.CustomColors;
import de.minefactprogress.progressplugin.utils.Item;
import de.minefactprogress.progressplugin.utils.Reflections;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BannerColorMenu extends Menu {

    private final String resetName = ChatColor.RED + "Reset Banner";
    private final String createName = ChatColor.GREEN + "Create Banner";

    private final Boolean letterBaseColor;

    public BannerColorMenu(MenuStorage menuStorage, Menu previousMenu) {
        this(menuStorage, previousMenu, null);
    }

    public BannerColorMenu(MenuStorage menuStorage, Menu previousMenu, Boolean letterBaseColor) {
        super(menuStorage, previousMenu);
        this.letterBaseColor =  letterBaseColor;
    }

    @Override
    public Component menuName() {
        return Component.text(letterBaseColor == null ? "Select Pattern Color" : "Select " + (letterBaseColor ? "Base" : "Letter") + " Color");
    }

    @Override
    public int rows() {
        return 5;
    }

    @Override
    public ItemStack titleItem() {
        return menuStorage.getBanner();
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = menuStorage.getOwner();
        ItemStack item = e.getCurrentItem();

        if (item == null) return;

        String itemName = PlainTextComponentSerializer.plainText().serializeOrNull(item.getItemMeta().displayName());

        if (itemName == null) return;

        if (item.getType() == Material.PLAYER_HEAD) {
            if (itemName.equals(ChatColor.stripColor(resetName))) {
                menuStorage.setBanner(null);
                new BannerBaseMenu(menuStorage, null).open();
            } else if (itemName.equals(ChatColor.stripColor(createName))) {
                if (e.getClick().isLeftClick()) {
                    p.getInventory().addItem(menuStorage.getBanner());
                    p.closeInventory();
                    p.sendMessage(Main.getPREFIX_SERVER() + ChatColor.GRAY + "Banner created successfully");
                } else if (e.getClick().isRightClick()) {
                    new AnvilGUI.Builder()
                            .onClick((slot, state) -> {
                                if (slot != AnvilGUI.Slot.OUTPUT) {
                                    return Collections.emptyList();
                                }
                                if (this.menuStorage.getUser() == null) {
                                    state.getPlayer().sendMessage(Constants.PREFIX_SERVER + ChatColor.RED + "You need to link your minecraft account with your website account in order to save banners!");
                                    menuStorage.getOwner().sendMessage(Constants.PREFIX_SERVER + Constants.LINK_ACCOUNT);
                                    return Collections.singletonList(AnvilGUI.ResponseAction.close());
                                }
                                Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                                    state.getPlayer().sendMessage(Constants.PREFIX_SERVER + ChatColor.GRAY + "Saving banner...");

                                    JsonObject json = new JsonObject();
                                    json.addProperty("name", state.getText().trim());
                                    json.addProperty("data", Reflections.itemStackToBase64(this.menuStorage.getBanner()));
                                    json.addProperty("user", this.menuStorage.getUser().getUid());

                                    API.POST(Routes.BANNERS, json, res -> {
                                        API.loadBanners();
                                        state.getPlayer().sendMessage(Constants.PREFIX_SERVER + ChatColor.GREEN + "Banner saved successfully");
                                    }, state.getPlayer());
                                });
                                return Collections.singletonList(AnvilGUI.ResponseAction.close());
                            })
                            .text(" ")
                            .itemLeft(this.menuStorage.getBanner())
                            .title("Enter a name")
                            .plugin(Main.getInstance())
                            .open(p);
                }
            }
        } else if (item.getType().name().contains("DYE")) {
            if (letterBaseColor == null) {
                menuStorage.setPatternColor(BannerHandler.getDyeColorByName(itemName.replace(" ", "_")));
                new BannerPatternMenu(menuStorage, this).open();
            } else {
                if (letterBaseColor) {
                    menuStorage.setLetterBaseColor(BannerHandler.getDyeColorByName(itemName.replace(" ", "_")));
                } else {
                    menuStorage.setLetterPatternColor(BannerHandler.getDyeColorByName(itemName.replace(" ", "_")));
                }
                new BannerLetterSelectionMenu(menuStorage, null).open();
            }

        }
    }

    @Override
    public void setMenuItems() {
        for(DyeColor dyeColor : DyeColor.values()) {
            Color color = dyeColor.getColor();
            inventory.addItem(BannerHandler.createDye(
                    Component.text(dyeColor.name().charAt(0) + dyeColor.name().substring(1).replace("_", " ").toLowerCase(),
                            CustomColors.of(String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()))), dyeColor));
        }

        if (letterBaseColor == null) {
            inventory.setItem(2, Item.createCustomHead("9382", resetName, null));
            inventory.setItem(6, Item.createCustomHead("21771", createName, new ArrayList<>(Arrays.asList("", CustomColors.YELLOW.getChatColor() + "Right-Click to save banner"))));
        }
    }
}
