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

import me.smudge.smscavenger.configs.CTreasures;
import me.smudge.smscavenger.guis.GUI;
import me.smudge.smscavenger.utility.Send;
import me.smudge.smscavenger.utility.Treasure;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.IntStream;

public class GMenu extends GUI {

    public GMenu() {
        super(54, Send.convert("&8&lMenu"));

        this.setFillers();
        this.setCreate();
        this.setTreasures();
    }

    private void setFillers() {
        ItemStack filler = this.getFillerItem();
        IntStream.range(45, 49).forEachOrdered(slot -> {setItem(slot, filler);});
        IntStream.range(50, 54).forEachOrdered(slot -> {setItem(slot, filler);});
    }

    private void setCreate() {
        ItemStack add = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta meta = add.getItemMeta();
        assert meta != null;

        meta.setDisplayName(Send.convert("&a&lCreate new treasure"));
        add.setItemMeta(meta);
        setItemClick(49, add, player -> {
            CTreasures.get().set("newTreasure.material", "barrier");
            CTreasures.save();
            player.closeInventory();

            GMenu menu = new GMenu();
            menu.open(player);
        });
    }

    private void setTreasures() {
        ArrayList<Treasure> treasures = CTreasures.getAllTreasure();

        int slot = 0;
        for (Treasure treasure : treasures) {
            ItemStack item = treasure.getItemStack();
            ItemMeta itemMeta = item.getItemMeta();

            Objects.requireNonNull(itemMeta).setDisplayName(Send.convert("&6&l" + treasure.getID()));

            ArrayList<String> lore = new ArrayList<>();
            lore.add(Send.convert("&7TreasureID &f" + treasure.getID()));
            lore.add(Send.convert("&aMaterial &e" + treasure.getMaterial().toString()));
            lore.add(Send.convert("&aHDB ID &e" + treasure.getHDB()));
            lore.add(Send.convert("&7Reward kit &f" + treasure.getKitReward()));
            lore.add(Send.convert("&7Particle type &f" + treasure.getParticleType()));
            lore.add(Send.convert("&7Particle amount &f" + treasure.getParticleAmount()));
            lore.add(Send.convert("&7Sound type &f" + treasure.getSoundType()));
            lore.add(Send.convert("&7Firework on click &f" + treasure.getFirework()));
            lore.add(Send.convert("&7Random locations &f" + treasure.getRandomise()));

            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            // Set item and increment slot
            setItemClick(slot, item, player -> {
                player.closeInventory();

                GTreasure editor = new GTreasure(treasure.getID());
                editor.open(player);
            }); slot += 1;
        }
    }
}
