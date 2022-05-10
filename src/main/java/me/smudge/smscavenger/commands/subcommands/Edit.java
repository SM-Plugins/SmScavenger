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
import me.smudge.smscavenger.guis.guis.GMenu;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;

public class Edit extends SubCommand {
    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public String getDescription() {
        return "Edit treasure";
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

        // Create a new instance of the menu
        GMenu menu = new GMenu();
        menu.open(player);

        return true;
    }
}
