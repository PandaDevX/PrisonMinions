package com.lifeonblack.prisonminions.lib.minion;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class MinionEquipment {

    private final String texture;
    private final int red, green, blue;
    private ItemStack mainHand;
    public MinionEquipment(String texture, int red, int green, int blue, ItemStack mainHand) {
        this.texture = texture;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.mainHand = mainHand;
    }

    public ItemStack getHead() {
        ItemStack head= new ItemStack(Material.PLAYER_HEAD);

        ItemMeta headMeta = head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));

        Field profileField = null;

        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        profileField.setAccessible(true);

        try {
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        head.setItemMeta(headMeta);
        return head;
    }

    private ItemStack build(Material type) {
        ItemStack itemStack = new ItemStack(type);
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        if(meta == null) return itemStack;
        meta.setColor(Color.fromRGB(red, green, blue));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack getChestPlate() {
        return build(Material.LEATHER_CHESTPLATE);
    }

    public ItemStack getLeggings() {
        return build(Material.LEATHER_LEGGINGS);
    }

    public ItemStack getBoots() {
        return build(Material.LEATHER_BOOTS);
    }

    public ItemStack getMainHand() {
        return mainHand;
    }

    public void setMainHand(ItemStack itemStack) {
        this.mainHand = itemStack;
    }
}
