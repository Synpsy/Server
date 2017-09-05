package net.inventory.item;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

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

    public static ItemStack BuildSkull(String Owner, String DisplayName, Integer Byte) {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, Short.valueOf(String.valueOf(Byte)).shortValue());
        SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
        skullMeta.setDisplayName(DisplayName);
        skullMeta.setOwner(Owner);
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

    public static ItemStack BuildSkull(String Owner, String DisplayName, String[] Lore, Integer Byte) {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, Short.valueOf(String.valueOf(Byte)).shortValue());
        SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
        skullMeta.setDisplayName(DisplayName);
        skullMeta.setOwner(Owner);
        if (Lore != null) {
            ArrayList<String> List = new ArrayList();
            String[] var7 = Lore;
            int var8 = Lore.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                String lore = var7[var9];
                List.add(lore);
            }

            skullMeta.setLore(List);
        }

        itemStack.setItemMeta(skullMeta);
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