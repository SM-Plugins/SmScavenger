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

    public static boolean check(Player player, String permission) {
        return player.hasPermission(mainCommandName + "." + permission);
    }

    public static boolean help(Player player) {
        StringBuilder listOfCommands = new StringBuilder();

        // Loop though commands
        for (SubCommand command : getSubCommands()) {
            if (!check(player, command.getPermission())) continue;

            listOfCommands.append("&e/{mainCommand} {command} &7{desc}\n"
                    .replace("{mainCommand}", mainCommandName)
                    .replace("{command}", command.getName())
                    .replace("{desc}", command.getDescription()));
        }

        String message = "&8&m&l--------]&r &6&l{plugin} &8&m&l[--------\n" +
                "&7Version &f" + plugin.getDescription().getVersion() + "\n" +
                "&7Created by &fSmudge\n" +
                "&8&m&l-------------------------\n" +
                listOfCommands.toString() +
                "&8&m&l-------------------------";

        Send.player(player, message.replace("{plugin}", plugin.getName()));
        return true;
    }

    public static boolean noPermission(Player player) {
        Send.playerError(player, "No Permission");
        return true;
    }

    public static boolean noArgument(Player player, String subCommand, int arguments) {
        StringBuilder message = new StringBuilder("/{main} {subcommand}"
                .replace("{main}", mainCommandName)
                .replace("{subcommand}", subCommand));
        for (int i = 0; i < arguments; i++) {message.append(" <Argument>");}
        Send.playerError(player, message.toString());
        return true;
    }

    public static SubCommand getCommandFromName(String name) {
        for (int i = 0; i < getSubCommands().size(); i++) {
            if (!Objects.equals(getSubCommands().get(i).getName(), name)) continue;
            return getSubCommands().get(i);
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {return help(player);}

            for (int i = 0; i < getSubCommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {
                    // Check permission
                    if (!check(player, getSubCommands().get(i).getPermission())) return noPermission(player);
                    // Check arguments
                    System.out.println(args.length);
                    if (getSubCommands().get(i).getRequiredArguments() + 1 > args.length) return noArgument(player,
                            getSubCommands().get(i).getName(),
                            getSubCommands().get(i).getRequiredArguments());

                    getSubCommands().get(i).preform(player, args, plugin);
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        // SubCommand
        if (args.length == 1) {
            ArrayList<String> subcommandsArguments = new ArrayList<>();

            for (int i = 0; i < getSubCommands().size(); i++) {
                Player player = (Player) sender;

                if (!check(player, getSubCommands().get(i).getPermission())) continue;
                subcommandsArguments.add(getSubCommands().get(i).getName());
            }
            return subcommandsArguments;

        } else {
            SubCommand commandInArg1 = getCommandFromName(args[0]);
            List<String> alt = new ArrayList<>();
            alt.add("No Arguments required");

            try {
                List<String> tab = Objects.requireNonNull(commandInArg1).getTabComplete().get(args.length - 1);
                if (tab == null) {return alt;}
                return Objects.requireNonNull(commandInArg1).getTabComplete().get(args.length - 1);
            } catch (Exception e) {return alt;}
        }
    }
}