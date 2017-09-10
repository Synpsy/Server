package net.inventory.item;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.plugin.log.PluginLogger;

public class ItemBuilder {

    public static ItemStack Build(Material Material, Integer Amount, Integer Byte, String DisplayName) {
        ItemStack itemStack = new ItemStack(Material, Amount.intValue(), Short.valueOf(String.valueOf(Byte)).shortValue());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(DisplayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack Build(Material Material, Integer Amount, Integer Byte, String DisplayName, String[] Lore) {
        ItemStack itemStack = new ItemStack(Material, Amount.intValue(), Short.valueOf(String.valueOf(Byte)).shortValue());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(DisplayName);
        if (Lore != null) {
            ArrayList<String> List = new ArrayList();
            String[] var8 = Lore;
            int var9 = Lore.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                String lore = var8[var10];
                List.add(lore);
            }

            itemMeta.setLore(List);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack Glow(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
}