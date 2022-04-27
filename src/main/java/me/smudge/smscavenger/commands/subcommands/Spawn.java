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

package me.smudge.smscavenger.commands.subcommands;

import me.smudge.smscavenger.commands.MainCommand;
import me.smudge.smscavenger.commands.SubCommand;
import me.smudge.smscavenger.configs.CConfig;
import me.smudge.smscavenger.configs.CData;
import me.smudge.smscavenger.configs.CLocations;
import me.smudge.smscavenger.utility.Send;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Spawn extends SubCommand {
    @Override
    public String getName() {
        return "spawn";
    }

    @Override
    public String getDescription() {
        return "Spawns all treasure";
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getTabComplete() {
        HashMap<Integer, ArrayList<String>> tab = new HashMap<>();

        ArrayList<String> line1 = new ArrayList<>();
        line1.add("andDeleteData");
        line1.add("saveto");
        tab.put(1, line1);

        ArrayList<String> line2 = new ArrayList<>();
        line2.add("[name]");
        tab.put(2, line2);

        return tab;
    }

    @Override
    public String getPermission() {
        return "admin";
    }

    @Override
    public int getRequiredArguments() {
        return 1;
    }

    @Override
    public boolean preform(Player player, String[] args, Plugin plugin) {
        Send.player(player, "{prefix} Spawning treasure...");

        // Setup data file
        if (args[1].toLowerCase(Locale.ROOT).equals("anddeletedata")) deleteData();
        if (args[1].toLowerCase(Locale.ROOT).contains("saveto")) CConfig.setDataFileName(args[2]);

        // Place all treasure
        CLocations.placeAllTreasure();

        Send.player(player, "{prefix} Treasure has been placed");
        return true;
    }

    private static void deleteData() {
        CData config = new CData();
        config.reset();
    }
}