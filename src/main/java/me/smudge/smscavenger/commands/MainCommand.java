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

import me.smudge.smscavenger.SmScavenger;
import me.smudge.smscavenger.commands.subcommands.*;
import me.smudge.smscavenger.utility.Send;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainCommand implements TabExecutor {

    private static final ArrayList<SubCommand> subCommands = new ArrayList<>();
    public static ArrayList<SubCommand> getSubCommands() {
        return subCommands;
    }

    public static String mainCommandName;
    public static Plugin plugin;

    public MainCommand(SmScavenger pluginUpdate, String nameUpdate){
        plugin = pluginUpdate;
        mainCommandName = nameUpdate;

        subCommands.add(new Reload());
        subCommands.add(new SetLocation());
        subCommands.add(new RemoveLocation());
        subCommands.add(new Spawn());
        subCommands.add(new Find());
        subCommands.add(new Edit());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        // Send help Message
        if (args.length == 0) {return sendHelpMessage(player);}

        // Get Command
        SubCommand subCommand = getCommandFromName(args[0]);
        if (subCommand == null) {return sendHelpMessage(player);}

        // Check permission
        if (!checkPermission(player, subCommand.getPermission())) return noPermissionMessage(player);

        // Check arguments
        if (subCommand.getRequiredArguments() + 1 > args.length)
            return noArgumentMessage(player, subCommand.getName(), subCommand.getRequiredArguments());

        // Run command
        subCommand.preform(player, args, plugin);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return null;
        Player player = (Player) sender;

        // Choose SubCommand
        if (args.length == 1) {
            ArrayList<String> subcommandsArguments = new ArrayList<>();

            for (int i = 0; i < getSubCommands().size(); i++) {
                // Check permissions
                if (!checkPermission(player, getSubCommands().get(i).getPermission())) continue;
                // Add
                subcommandsArguments.add(getSubCommands().get(i).getName());
            }
            return subcommandsArguments;

        } else {
            SubCommand subCommand = getCommandFromName(args[0]);
            // Alternative, if there are no arguments to choose from
            List<String> alt = new ArrayList<>();
            alt.add("No Arguments required");

            try {
                List<String> tab = Objects.requireNonNull(subCommand).getTabComplete().get(args.length - 1);
                if (tab == null) {return alt;}
                return Objects.requireNonNull(subCommand).getTabComplete().get(args.length - 1);

            } catch (Exception e) {return alt;}
        }
    }

    /**
     * @param player Player to test permission
     * @param permission permission to test
     * @return if they have the permission
     */
    public static boolean checkPermission(Player player, String permission) {
        return player.hasPermission(mainCommandName + "." + permission);
    }

    /**
     * @param player Player to send message
     * @return If successful
     */
    public static boolean noPermissionMessage(Player player) {
        Send.playerError(player, "No Permission");
        return true;
    }

    /**
     * @param player Player to sent help message
     * @return If successful
     */
    public static boolean sendHelpMessage(Player player) {
        StringBuilder listOfCommands = new StringBuilder();

        // Loop though commands and format
        for (SubCommand command : getSubCommands()) {
            if (!checkPermission(player, command.getPermission())) continue;

            listOfCommands.append("&e/{mainCommand} {command} &7{desc}\n"
                    .replace("{mainCommand}", mainCommandName)
                    .replace("{command}", command.getName())
                    .replace("{desc}", command.getDescription()));
        }

        // Format message
        String message = "&8&m&l--------]&r &6&l{plugin} &8&m&l[--------\n" +
                "&7Version &f" + plugin.getDescription().getVersion() + "\n" +
                "&7Created by &fSmudge\n" +
                "&8&m&l-------------------------\n" +
                listOfCommands.toString() +
                "&8&m&l-------------------------";

        // Send message
        Send.player(player, message.replace("{plugin}", plugin.getName()));
        return true;
    }

    /**
     * @param player Player to send message
     * @param subCommand Command that the player tried to use
     * @param arguments Amount of arguments required
     * @return If successful
     */
    public static boolean noArgumentMessage(Player player, String subCommand, int arguments) {
        StringBuilder message = new StringBuilder("/{main} {subcommand}"
                .replace("{main}", mainCommandName)
                .replace("{subcommand}", subCommand));

        for (int i = 0; i < arguments; i++) {message.append(" <Argument>");}

        Send.playerError(player, message.toString());
        return true;
    }

    /**
     * @param name String name of command
     * @return SubCommand class
     */
    public static SubCommand getCommandFromName(String name) {
        for (int i = 0; i < getSubCommands().size(); i++) {
            if (!name.equalsIgnoreCase(getSubCommands().get(i).getName())) continue;
            return getSubCommands().get(i);
        }
        return null;
    }
}