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

package me.smudge.smscavenger.commands;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class SubCommand {
    // Name of the subcommand
    public abstract String getName();
    // Description shown in help message
    public abstract String getDescription();
    // Tab complete for arguments
    public abstract HashMap<Integer, ArrayList<String>> getTabComplete();
    // <MainCommand.ThisPermission>
    public abstract String getPermission();
    // The amount of arguments required after the subcommand
    public abstract int getRequiredArguments();

    // When the command is run
    public abstract boolean preform(Player player, String[] args, Plugin plugin);

}
