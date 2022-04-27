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

package me.smudge.smscavenger;

import me.smudge.smscavenger.commands.MainCommand;
import me.smudge.smscavenger.configs.CConfig;
import me.smudge.smscavenger.configs.CLocations;
import me.smudge.smscavenger.configs.CTreasures;
import me.smudge.smscavenger.events.EClickEvent;
import me.smudge.smscavenger.events.EGUI;
import me.smudge.smscavenger.utility.Task;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class SmScavenger extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {

        // Setup Configs
        CConfig.setup();
        CConfig.setupDefaults();
        CConfig.get().options().copyDefaults(true);
        CConfig.save();

        CLocations.setup();
        CLocations.get().options().copyDefaults(true);
        CLocations.save();

        CTreasures.setup();
        CTreasures.setupDefaults();
        CTreasures.get().options().copyDefaults(true);
        CTreasures.save();

        // Setup Commands
        getCommand("scav").setExecutor(new MainCommand(this, "scav"));

        // Setup Events
        getServer().getPluginManager().registerEvents(new EClickEvent(), this);
        getServer().getPluginManager().registerEvents(new EGUI(), this);

        // Setup PlaceholderAPI
        new PlaceholderAPI().register();

        // Setup Tasks
        Task.plugin = this;
    }
}
