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
import me.smudge.smscavenger.configs.CData;
import me.smudge.smscavenger.configs.CLocations;
import me.smudge.smscavenger.configs.CTreasures;
import me.smudge.smscavenger.utility.Send;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;

public class Reload extends SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads configs";
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
        CConfig.reload();
        CLocations.reload();
        CTreasures.reload();

        // Get and reload
        CData data = new CData();
        data.reload();

        Send.player(player, CConfig.getMessageReloaded());
        return true;
    }
}