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
import me.smudge.smscavenger.utility.Send;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;

public class RemoveLocation extends SubCommand {
    @Override
    public String getName() {
        return "removelocation";
    }

    @Override
    public String getDescription() {
        return "Removes a location of a treasure";
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getTabComplete() {
        return null;
    }

    @Override
    public String getPermission() {
        return "admin";
    }

    @Override
    public int getRequiredArguments() {
        return 0;
    }

    @Override
    public boolean preform(Player player, String[] args, Plugin plugin) {
        Location location = player.getLocation();

        // Remove location from data
        if (!CLocations.removeLocation(location)) return noLocationError(player);

        // Remove treasure
        location.getBlock().setType(Material.AIR);

        // Send message
        Send.player(player, CConfig.getMessageRemoveLocation());
        return true;
    }

    private boolean noLocationError(Player player) {
        Send.playerError(player, "There was no treasure location set here");
        return true;
    }
}