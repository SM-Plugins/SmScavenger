package me.smudge.smscavenger.utility;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkSpawner {

    public static void spawn(Location location) {
        location.setX(location.getBlockX() + 0.5);
        location.setZ(location.getBlockZ() + 0.5);
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();

        meta.setPower(1);
        meta.addEffects(FireworkEffect.builder().withColor(Color.YELLOW).flicker(true).build());
        meta.addEffect(FireworkEffect.builder().withColor(Color.ORANGE).flicker(true).build());

        firework.setFireworkMeta(meta);
        Task.runTaskIn(10, firework::detonate);
    }
}
