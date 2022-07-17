package me.smudge.smscavenger.utility;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkSpawner {

    public static void spawn(Location location, Treasure treasure) {
        location.setX(location.getBlockX() + 0.5);
        location.setZ(location.getBlockZ() + 0.5);
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();

        meta.setPower(1);

        try {
            for (Color color : treasure.getFireworkColors()) {
                meta.addEffect(FireworkEffect.builder().withColor(color).flicker(true).build());
            }
        } catch (Exception ignored) {}

        firework.setFireworkMeta(meta);
        Task.runTaskIn(10, firework::detonate);
    }
}
