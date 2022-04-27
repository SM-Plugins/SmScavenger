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

package me.smudge.smscavenger.utility;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.atomic.AtomicInteger;

public class Task {

    public static Plugin plugin;

    /**
     * @param ticks How long to wait untill the method is called
     * @param runnable Method to be called
     */
    public static void runTaskIn(int ticks, Runnable runnable) {
        AtomicInteger i = new AtomicInteger();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskTimer(plugin, task -> {

            if (i.incrementAndGet() >= ticks) {
                runnable.run();
                task.cancel();
            }

        }, 1, 1);
    }
}
