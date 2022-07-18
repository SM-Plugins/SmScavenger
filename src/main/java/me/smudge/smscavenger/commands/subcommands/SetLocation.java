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

import me.smudge.smscavenger.commands.SubCommand;
import me.smudge.smscavenger.configs.CConfig;
import me.smudge.smscavenger.configs.CLocations;
import me.smudge.smscavenger.configs.CTreasures;
import me.smudge.smscavenger.utility.Send;
import me.smudge.smscavenger.utility.Treasure;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;

public class SetLocation extends SubCommand {
    @Override
    public String getName() {
        return "setlocation";
    }

    @Override
    public String getDescription() {
        return "Sets a location of a treasure";
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getTabComplete() {
        HashMap<Integer, ArrayList<String>> tab = new HashMap<>();
        tab.put(1, CTreasures.getIDList());

        ArrayList<String> temp = new ArrayList<>();
        temp.add("Optional X");
        tab.put(2, temp);

        temp = new ArrayList<>();
        temp.add("Optional Y");
        tab.put(3, temp);

        temp = new ArrayList<>();
        temp.add("Optional Z");
        tab.put(4, temp);

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
        String treasureID = args[1];
        Location location = player.getLocation();

        // Optional cords
        if (args.length >= 5) {
            location = new Location(
                    player.getWorld(),
                    Integer.parseInt(args[2]),
                    Integer.parseInt(args[3]),
                    Integer.parseInt(args[4])
            );
        }

        // Save location data
        CLocations.setLocation(location, treasureID);

        // Place treasure
        Treasure treasure = CTreasures.getTreasure(treasureID);
        treasure.place(location);

        // Set present
        CLocations.setPresent(location);

        Send.player(player, CConfig.getMessageSetLocation());
        return true;
    }
}