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

import me.smudge.smscavenger.utility.Send;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CData {

    File file;
    FileConfiguration configFile;
    String path;

    /**
     * @param path Specify the path
     */
    public CData(String path) {
        this.path = path;
        this.setup();
        this.get().options().copyDefaults(true);
        this.save();
    }

    /**
     * Get the path from CConfig
     */
    public CData() {
        this.path = CConfig.getDataFileName();
        this.setup();
        this.get().options().copyDefaults(true);
        this.save();
    }

    public void setup() {
        this.file = new File(Bukkit.getServer().getPluginManager().getPlugin("SmScavenger").getDataFolder() + "/data", this.path + ".yml");
        if (!this.file.exists()) {try {this.file.createNewFile();} catch (IOException ignored) {}}
        this.configFile = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfiguration get() {
        return this.configFile;
    }

    public void save() {
        try {
            this.configFile.save(file);
        } catch (IOException e) {
            System.out.println("Could not save file");
        }
    }

    public void reload() {this.configFile = YamlConfiguration.loadConfiguration(this.file);}

    public void reset() {
        if (!this.file.delete()) {
            Send.log("Data config error occurred");
            return;
        }
        this.setup();
        this.get().options().copyDefaults(true);
        this.save();
    }

    /**
     * @param path Path to increment Integer
     */
    private void incrementPath(String path) {
        try {
            int current = this.get().getInt(path);
            this.get().set(path, current + 1);
        } catch (Exception e) {
            this.get().set(path, 1);
        }
    }

    /**
     * @param path Path to increment treasure
     * @param treasureID Treasure ID
     */
    public void incrementTreasure(String path, String treasureID) {
        incrementPath(path + "." + treasureID);
        incrementPath(path + ".total");
        incrementPath(treasureID);
    }

    /**
     * @param treasureID Treasure ID
     * @return Integer amount of treasure found
     */
    public int getTotalTreasure(String treasureID) {
        try {return this.get().getInt(treasureID);
        } catch (Exception e) {return 0;}
    }

    /**
     * @param playerName Player to get total
     * @return Total treasure player has found
     */
    public int getPlayerTotal(String playerName) {
        try { return this.get().getInt(playerName + ".total");
        } catch (Exception e) {return 0;}
    }
}