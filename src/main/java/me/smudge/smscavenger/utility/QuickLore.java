package me.smudge.smscavenger.utility;

import org.bukkit.entity.Item;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class QuickLore {

    public static void set(ItemMeta meta, String message) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(Send.convert(message));
        meta.setLore(lore);
    }

    public static void add(ItemMeta meta, String message) {
        ArrayList<String> lore = (ArrayList<String>) meta.getLore();
        lore.add(Send.convert(message));
        meta.setLore(lore);
    }
}
