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

package me.smudge.smscavenger.configs;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CConfig {

    private static File file;
    private static FileConfiguration configFile;

    // Generates or finds file
    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("SmScavenger").getDataFolder(), "config.yml");
        if (!file.exists()) {try {file.createNewFile();} catch (IOException ignored) {}}
        configFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return configFile;
    }

    public static void save() {
        try {configFile.save(file);
        } catch (IOException e) {System.out.println("Could not save file");}
    }

    public static void reload() {
        configFile = YamlConfiguration.loadConfiguration(file);
    }

    public static void setupDefaults() {
        CConfig.get().addDefault("prefix", "&4&l[&6&lServer&4&l]&r&a");
        CConfig.get().addDefault("max", -1);

        CConfig.get().addDefault("save data as", "data");

        ArrayList<String> permissions = new ArrayList<>();
        permissions.add("red");
        permissions.add("orange");
        permissions.add("yellow");
        permissions.add("green");
        permissions.add("blue");
        permissions.add("purple");
        CConfig.get().addDefault("permissions to look for", permissions);

        CConfig.get().addDefault("messages.reload", "{prefix} All configs have been reloaded <3");
        CConfig.get().addDefault("messages.set location", "{prefix} Location has been set in &flocations.yml");
        CConfig.get().addDefault("messages.remove location", "{prefix} Location has been removed");
        CConfig.get().addDefault("message.treasure found", "{prefix} {player} just found: &f{treasureID}");

        CConfig.get().addDefault("leaderboard format", "&6&l{rank} &f&l{permission} &e{player} &7- &a{amount}");
    }

    /** @return Integer max amount of treasure a player can find */
    public static int getMax() {
        return CConfig.get().getInt("max");
    }

    /** @return Message prefix */
    public static String getPrefix() {
        return CConfig.get().getString("prefix");
    }

    /** @return Reloaded message */
    public static String getMessageReloaded() {
        return CConfig.get().getString("messages.reload");
    }

    /** @return Location set message */
    public static String getMessageSetLocation() {
        return CConfig.get().getString("messages.set location");
    }

    /** @return Location remove message */
    public static String getMessageRemoveLocation() {
        return CConfig.get().getString("messages.remove location");
    }

    /** @return Treasure found message */
    public static String getMessageTreasureFound() {
        return CConfig.get().getString("message.treasure found");
    }

    /** @return Leaderboard format */
    public static String getMessageLeaderboard() {
        return CConfig.get().getString("leaderboard format");
    }

    /** @return Get default data file name */
    public static String getDataFileName() {return CConfig.get().getString("save data as");}

    /**
     * @param name Set the default data file name
     */
    public static void setDataFileName(String name) {
        CConfig.get().set("save data as", name);
        CConfig.save();
    }

    /** @return Permissions to look for and store in data */
    public static ArrayList<String> getPermissionsToLookFor() {
        return (ArrayList<String>) CConfig.get().getStringList("permissions to look for");
    }

    /**
     * @param player Player to get permissions of
     * @return Return one of their permissions
     */
    public static String getPermissionOfPlayer(Player player) {
        for (String permission : getPermissionsToLookFor()) {
            if (player.hasPermission(permission)) return permission;
        }
        return null;
    }
}
