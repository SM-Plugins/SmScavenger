/*
 *   _____            _____
 *  / ____|          / ____|
 * | (___  _ __ ___ | (___   ___ __ ___   _____ _ __   __ _  ___ _ __
 *  \___ \| '_ ` _ \ \___ \ / __/ _` \ \ / / _ \ '_ \ / _` |/ _ \ '__|
 *  ____) | | | | | |____) | (_| (_| |\ V /  __/ | | | (_| |  __/ |
 * |_____/|_| |_| |_|_____/ \___\__,_| \_/ \___|_| |_|\__, |\___|_|
 *                                                     __/ |
 *                                                    |___/
 * SmScavenger is a treasure hunt game where players look for treasure
 * you hide to get rewards.
 *
 * Author : Smudge
 */

package me.smudge.smscavenger.guis.guis;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.smudge.smscavenger.configs.CTreasures;
import me.smudge.smscavenger.events.EGUI;
import me.smudge.smscavenger.guis.GUI;
import me.smudge.smscavenger.utility.Send;
import me.smudge.smscavenger.utility.Task;
import me.smudge.smscavenger.utility.Treasure;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.IntStream;

public class GTreasure extends GUI {

    String treasureID;
    Treasure treasure;

    /**
     * @param treasureID Treasure to edit
     */
    public GTreasure(String treasureID) {
        super(54, Send.convert("&8&l" + treasureID));

        this.treasureID = treasureID;
        this.treasure = CTreasures.getTreasure(this.treasureID);

        this.setClose(this::openMenuWithDelay);

        this.setFillers();
        this.setMaterial();
        this.setEditItems();
    }

    /**
     * @param player Player to open the GUI
     */
    public void openMenuWithDelay(Player player) {
        Task.runTaskIn(5, () -> {

            GMenu menu = new GMenu();
            menu.open(player);

        });
    }

    private void setFillers() {
        ItemStack filler = this.getFillerItem();
        IntStream.range(0, 9).forEachOrdered(slot -> {setItem(slot, filler);});
        IntStream.range(9, 13).forEachOrdered(slot -> {setItem(slot, filler);});
        IntStream.range(14, 18).forEachOrdered(slot -> {setItem(slot, filler);});
        IntStream.range(18, 27).forEachOrdered(slot -> {setItem(slot, filler);});
        IntStream.range(36, 45).forEachOrdered(slot -> {setItem(slot, filler);});
    }

    private void setMaterial() {
        ItemStack item = this.treasure.getItemStack();
        ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(Send.convert("&a&lTreasure Block"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(Send.convert("&7Replace me with a item"));
        lore.add(Send.convert("&7to change the treasure material"));
        lore.add(Send.convert("&7or HDB head"));
        meta.setLore(lore);
        item.setItemMeta(meta);

        setChangeableItem(13, item, itemStack -> {
            if (itemStack.getType() == Material.PLAYER_HEAD) {
                HeadDatabaseAPI api = new HeadDatabaseAPI();
                CTreasures.get().set(this.treasureID + ".HDB", api.getItemID(itemStack));
                CTreasures.get().set(this.treasureID + ".material", false);
                CTreasures.save();

            } else {
                CTreasures.get().set(this.treasureID + ".material", itemStack.getType().toString());
                CTreasures.get().set(this.treasureID + ".HDB", null);
                CTreasures.save();
            }
        });
    }

    private void setEditItems() {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Send.convert("&6&lChange ID"));
        item.setItemMeta(meta);

        setChatItem(27, item, value -> {
            this.treasure.setID(value);
            CTreasures.get().set(this.treasureID, null);
            CTreasures.createTreasure(this.treasure);
            CTreasures.save();

            GTreasure editor = new GTreasure(value);
            editor.open(this.player);
        });

        meta.setDisplayName(Send.convert("&6&lChange Reward Kit"));
        item.setItemMeta(meta);
        item.setType(Material.CHEST_MINECART);

        setChatItem(28, item, value -> {
            CTreasures.get().set(this.treasureID + ".rewards.essetials kit", value);
            CTreasures.save();

            GTreasure editor = new GTreasure(this.treasureID);
            editor.open(this.player);
        });

        meta.setDisplayName(Send.convert("&6&lChange Particle Type"));
        item.setItemMeta(meta);
        item.setType(Material.FIREWORK_ROCKET);

        setChatItem(29, item, value -> {
            CTreasures.get().set(this.treasureID + ".particle.type", value);
            CTreasures.save();

            GTreasure editor = new GTreasure(this.treasureID);
            editor.open(this.player);
        });

        meta.setDisplayName(Send.convert("&6&lChange Particle Amount"));
        item.setItemMeta(meta);
        item.setType(Material.FIREWORK_ROCKET);

        setChatItem(30, item, value -> {
            CTreasures.get().set(this.treasureID + ".particle.amount", Integer.valueOf(value));
            CTreasures.save();

            GTreasure editor = new GTreasure(this.treasureID);
            editor.open(this.player);
        });

        meta.setDisplayName(Send.convert("&6&lChange Sound Type"));
        item.setItemMeta(meta);
        item.setType(Material.MUSIC_DISC_CHIRP);

        setChatItem(31, item, value -> {
            CTreasures.get().set(this.treasureID + ".sound.type", value);
            CTreasures.save();

            GTreasure editor = new GTreasure(this.treasureID);
            editor.open(this.player);
        });

        meta.setDisplayName(Send.convert("&c&lDelete"));
        item.setItemMeta(meta);
        item.setType(Material.RED_STAINED_GLASS_PANE);

        setItemClick(49, item, player -> {
            CTreasures.get().set(this.treasureID, null);
            CTreasures.save();

            EGUI.cancelMovable = true;
            player.closeInventory();

            GMenu menu = new GMenu();
            menu.open(player);
        });
    }
}
