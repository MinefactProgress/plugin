package de.minefactprogress.progressplugin.utils;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.arcaniax.hdb.enums.CategoryEnum;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

/**
 * Utility class for creating Items/ItemStacks
 */
public class Item {
    public static HashMap<String, ItemStack> nonPlayerSkulls = new HashMap<>();

    private ItemStack item;
    private Material material;
    private String displayName;
    private int amount = -1;
    private ArrayList<String> lore;
    private boolean hideAttributes;
    private boolean hideEnchantments;
    private final List<String> canDestroyItems = new ArrayList<String>();
    private final List<String> canPlaceItems = new ArrayList<String>();

    private final HashMap<Enchantment, Integer> enchantments = new HashMap<>();

    public Item() {
    }

    public Item(Material material) {
        this.material = material;
    }

    public Item(ItemStack item) {
        this.item = item;
    }

    public Item setType(Material material) {
        this.material = material;
        return this;
    }

    public Item setDisplayName(String name) {
        this.displayName = name;
        return this;
    }

    public Item setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public Item setLore(ArrayList<String> lore) {
        this.lore = lore;
        return this;
    }

    public Item addEnchantment(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, Integer.valueOf(level));
        return this;
    }

    public Item hideAttributes(boolean hide) {
        this.hideAttributes = hide;
        return this;
    }

    public Item hideEnchantments(boolean enchants) {
        this.hideEnchantments = enchants;
        return this;
    }

    public Item addCanDestroyItem(String itemName) {
        this.canDestroyItems.add(itemName);
        return this;
    }

    public Item addCanPlaceItem(String itemName) {
        this.canPlaceItems.add(itemName);
        return this;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(Material.BARRIER);

        if (this.material != null)
            item.setType(material);

        if (this.item != null)
            item = this.item.clone();

        if (this.amount != -1)
            item.setAmount(this.amount);
        else
            item.setAmount(1);

        if (item.getEnchantments().keySet().size() == 0)
            for (Enchantment en : this.enchantments.keySet())
                item.addUnsafeEnchantment(en, this.enchantments.get(en).intValue());


        ItemMeta itemmeta = item.getItemMeta();

        if (!itemmeta.hasDisplayName())
            itemmeta.setDisplayName(this.displayName);

        if (!itemmeta.hasLore())
            itemmeta.setLore(this.lore);

        if (this.hideAttributes)
            itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if (this.hideEnchantments)
            itemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(itemmeta);

        // NMS shit i do not want to fix right now. We belive in the glory of net.minecraft.server
        /*if (canDestroyItems.size() > 0) {
            net.minecraft.server.v1_16_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);

            NBTTagList idsTag = new NBTTagList();
            for (String tag : canDestroyItems)
                idsTag.add(NBTTagString.a(tag));

            NBTTagCompound tag = stack.hasTag() ? stack.getTag() : new NBTTagCompound();
            tag.set("CanDestroy", idsTag);

            stack.setTag(tag);
            item = CraftItemStack.asBukkitCopy(stack);

			/*
			Set<NamespacedKey> nameSpacedKeySet = new HashSet<NamespacedKey>();
			for(String itemName : canDestroyItems)
				nameSpacedKeySet.add(NamespacedKey.minecraft(itemName));
			itemmeta.setDestroyableKeys(canDestroyItems);
        }

        if (canPlaceItems.size() > 0) {
            net.minecraft.server.v1_16_R3.ItemStack stack = CraftItemStack.asNMSCopy(item);

            NBTTagList idsTag = new NBTTagList();
            for (String tag : canPlaceItems)
                idsTag.add(NBTTagString.a(tag));

            NBTTagCompound tag = stack.hasTag() ? stack.getTag() : new NBTTagCompound();
            tag.set("CanPlaceOn", idsTag);

            stack.setTag(tag);
            item = CraftItemStack.asBukkitCopy(stack);

			/*
			Set<NamespacedKey> nameSpacedKeySet = new HashSet<NamespacedKey>();
			for(String itemName : canPlaceItems)
				nameSpacedKeySet.add(NamespacedKey.minecraft(itemName));
			itemmeta.setPlaceableKeys(nameSpacedKeySet);
        }*/


        return item;
    }

    public static ItemStack create(Material material) {
        return new ItemStack(material);
    }

    public static ItemStack create(Material material, int amount) {
        ItemStack item = new ItemStack(material, amount);
        return item;
    }

    public static ItemStack create(Material material, String name) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(name);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack create(Material material, String name, int amount) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(name);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack create(Material material, String name, ArrayList<String> lore) {
        ItemStack item = new ItemStack(material, 1, (short) 0);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(name);
        itemmeta.setLore(lore);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack create(Material material, String name, short durability, ArrayList<String> lore) {
        ItemStack item = new ItemStack(material, 1, durability);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(name);
        itemmeta.setLore(lore);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack create(Material material, String name, int amount, ArrayList<String> lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(name);
        itemmeta.setLore(lore);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack createLeatherArmor(Material material, String name, Color color, ArrayList<String> lore) {
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta itemmeta = (LeatherArmorMeta) item.getItemMeta();
        itemmeta.setDisplayName(name);
        itemmeta.setLore(lore);
        itemmeta.setColor(color);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack create(Material material, String name, ArrayList<String> lore, Enchantment enchnt1, Integer level1) {
        ItemStack item = new ItemStack(material);
        item.addUnsafeEnchantment(enchnt1, level1.intValue());
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(name);
        itemmeta.setLore(lore);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack create(Material material, String name, ArrayList<String> lore, Enchantment enchnt1, Integer level1, Enchantment enchnt2, Integer level2) {
        ItemStack item = new ItemStack(material);
        item.addUnsafeEnchantment(enchnt1, level1.intValue());
        item.addUnsafeEnchantment(enchnt2, level2.intValue());
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(name);
        itemmeta.setLore(lore);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack create(Material material, String name, ArrayList<String> lore, Enchantment enchnt1, Integer level1, Enchantment enchnt2, Integer level2, Enchantment enchnt3, Integer level3) {
        ItemStack item = new ItemStack(material);
        item.addUnsafeEnchantment(enchnt1, level1.intValue());
        item.addUnsafeEnchantment(enchnt2, level2.intValue());
        item.addUnsafeEnchantment(enchnt3, level3.intValue());
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(name);
        itemmeta.setLore(lore);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack createLeatherArmor(Material material, Color color) {
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta itemmeta = (LeatherArmorMeta) item.getItemMeta();
        itemmeta.setColor(color);
        item.setItemMeta(itemmeta);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        return item;
    }

    public static ItemStack createLeatherArmor(Material material, String name, Color color, ArrayList<String> lore, Enchantment enchnt1, Integer level1) {
        ItemStack item = new ItemStack(material);
        item.addUnsafeEnchantment(enchnt1, level1.intValue());
        LeatherArmorMeta itemmeta = (LeatherArmorMeta) item.getItemMeta();
        itemmeta.setDisplayName(name);
        itemmeta.setLore(lore);
        itemmeta.setColor(color);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack createLeatherArmor(Material material, String name, Color color, ArrayList<String> lore, Enchantment enchnt1, Integer level1, Enchantment enchnt2, Integer level2) {
        ItemStack item = new ItemStack(material);
        item.addUnsafeEnchantment(enchnt1, level1.intValue());
        item.addUnsafeEnchantment(enchnt2, level2.intValue());
        LeatherArmorMeta itemmeta = (LeatherArmorMeta) item.getItemMeta();
        itemmeta.setDisplayName(name);
        itemmeta.setLore(lore);
        itemmeta.setColor(color);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack createLeatherArmor(Material material, String name, Color color, ArrayList<String> lore, Enchantment enchnt1, Integer level1, Enchantment enchnt2, Integer level2, Enchantment enchnt3, Integer level3) {
        ItemStack item = new ItemStack(material);
        item.addUnsafeEnchantment(enchnt1, level1.intValue());
        item.addUnsafeEnchantment(enchnt2, level2.intValue());
        item.addUnsafeEnchantment(enchnt3, level3.intValue());
        LeatherArmorMeta itemmeta = (LeatherArmorMeta) item.getItemMeta();
        itemmeta.setDisplayName(name);
        itemmeta.setLore(lore);
        itemmeta.setColor(color);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack createPlayerHead(String name, String owner) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta itemmeta = (SkullMeta) item.getItemMeta();
        itemmeta.setDisplayName(name);
        itemmeta.setOwner(owner);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack createPlayerHead(String name, String owner, ArrayList<String> lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta itemmeta = (SkullMeta) item.getItemMeta();
        itemmeta.setDisplayName(name);
        itemmeta.setOwner(owner);
        itemmeta.setLore(lore);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack createPlayerHead(String name, String owner, int amount, ArrayList<String> lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta itemmeta = (SkullMeta) item.getItemMeta();
        itemmeta.setDisplayName(name);
        itemmeta.setOwner(owner);
        itemmeta.setLore(lore);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack edit(ItemStack item, Material material) {
        item.setType(material);
        return item;
    }

    public static ItemStack edit(ItemStack item, int amount) {
        item.setAmount(amount);
        return item;
    }

    public static ItemStack edit(ItemStack item, String name) {
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(name);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack edit(ItemStack item, int amount, String name) {
        item.setAmount(amount);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(name);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack edit(ItemStack item, ArrayList<String> lore) {
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setLore(lore);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack edit(ItemStack item, int amount, String name, ArrayList<String> lore) {
        item.setAmount(amount);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(name);
        itemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemmeta.setLore(lore);
        item.setItemMeta(itemmeta);
        return item;
    }

    public static ItemStack edit(ItemStack item, int amount, int data) {
        item.setAmount(amount);
        item.getData().setData((byte) data);
        return item;
    }

    public static ItemStack createNonPlayerSkull(String url, String name, ArrayList<String> lore) {
        String loreString = "";
        if (lore != null)
            for (String s : lore)
                loreString = loreString + s;
        try {
            if (nonPlayerSkulls.containsKey(url + name + loreString))
                return nonPlayerSkulls.get(url + name + loreString);
            byte[] encodedByteData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
            String encodedData = new String(encodedByteData);
            HeadDatabaseAPI api = new HeadDatabaseAPI();
            if (api.getItemHead(url) == null)
                api.addHead(CategoryEnum.CUSTOM, url, encodedData);
            ItemStack item = api.getItemHead(url);
            SkullMeta im = (SkullMeta) item.getItemMeta();
            im.setDisplayName(name);
            im.setLore(lore);
            item.setItemMeta(im);
            nonPlayerSkulls.remove(url + name + loreString);
            nonPlayerSkulls.put(url + name + loreString, item);
            return item;
        } catch (NoClassDefFoundError ex) {
            return create(Material.PLAYER_HEAD, name, lore);
        }
    }

    public static ItemStack createCustomHead(String id, String name, ArrayList<String> lore) {
        String loreString = "";
        if (lore != null)
            for (String s : lore)
                loreString = loreString + s;
        try {
            if (nonPlayerSkulls.containsKey(id + name + loreString))
                return nonPlayerSkulls.get(id + name + loreString);
            HeadDatabaseAPI api = new HeadDatabaseAPI();
            if (api.getItemHead(id) == null)
                return create(Material.PLAYER_HEAD, name, lore);

            ItemStack item = api.getItemHead(id);
            SkullMeta im = (SkullMeta) item.getItemMeta();
            im.setDisplayName(name);
            im.setLore(lore);
            item.setItemMeta(im);
            nonPlayerSkulls.remove(id + name + loreString);
            nonPlayerSkulls.put(id + name + loreString, item);
            return item;
        } catch (NoClassDefFoundError ex) {
            return create(Material.PLAYER_HEAD, name, lore);
        }
    }
}
