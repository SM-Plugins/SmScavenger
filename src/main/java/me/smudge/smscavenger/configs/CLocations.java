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

import me.smudge.smscavenger.utility.Treasure;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CLocations {

    private static File file;
    private static FileConfiguration configFile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("SmScavenger").getDataFolder(), "locations.yml");
        if (!file.exists()) {try {file.createNewFile();} catch (IOException ignored) {}}
        configFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return configFile;
    }

    public static void save() {
        try {
            configFile.save(file);
        } catch (IOException e) {
            System.out.println("Could not save file");
        }
    }

    public static void reload() {
        configFile = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * @param location Location of treasure
     * @return String Location key
     */
    public static String getLocationID(Location location) {
        return String.valueOf(location.getBlockX()) +
                String.valueOf(location.getBlockY()) +
                String.valueOf(location.getBlockZ());
    }

    /**
     * @param location Location of Treasure
     * @param treasureID ID of the treasure
     */
    public static void setLocation(Location location, String treasureID) {
        String ID = getLocationID(location);

        CLocations.get().set(ID + ".x", location.getBlockX());
        CLocations.get().set(ID + ".y", location.getBlockY());
        CLocations.get().set(ID + ".z", location.getBlockZ());
        CLocations.get().set(ID + ".world", Objects.requireNonNull(location.getWorld()).getName());
        CLocations.get().set(ID + ".treasure ID", treasureID);

        CLocations.save();
    }

    /**
     * @param location Location of Treasure
     */
    public static void setPresent(Location location) {
        String ID = getLocationID(location);

        CLocations.get().set(ID + ".present", true);
        CLocations.save();
    }

    /**
     * @param location Location of Treasure
     * @param state Set the state of the treasure (present, not present)
     */
    public static void setPresent(Location location, boolean state) {
        String ID = getLocationID(location);

        CLocations.get().set(ID + ".present", state);
        CLocations.save();
    }

    /**
     * @param location Location of Treasure
     * @return boolean Is the treasure present
     */
    public static boolean isPresent(Location location) {
        String ID = getLocationID(location);
        return CLocations.get().getBoolean(ID + ".present");
    }

    /**
     * @param location Location of Treasure
     * @return boolean Was the removal successful
     */
    public static boolean removeLocation(Location location) {
        String ID = getLocationID(location);

        try {
            CLocations.get().set(ID, null);
            CLocations.save();
            return true;
        } catch (Exception e) {return false;}
    }

    /**
     * @param locationID Location ID
     * @return Location of the treasure
     */
    public static Location getLocation(String locationID) {
        return new Location(
                Bukkit.getWorld(Objects.requireNonNull(CLocations.get().getString(locationID + ".world"))),
                CLocations.get().getInt(locationID +".x"),
                CLocations.get().getInt(locationID +".y"),
                CLocations.get().getInt(locationID +".z")
        );
    }

    public static HashMap<String, Integer> getRandomTreasure() {
        HashMap<String, Integer> toReturn = new HashMap<>();
        for (Treasure treasure : CTreasures.getAllTreasure()) {
            if (!treasure.getRandomise()) continue;
            int amount = getTotalTreasure(treasure.getID());
            toReturn.put(treasure.getID(), amount / 2);
        }
        return toReturn;
    }

    public static void placeAllTreasure() {
        ArrayList<String> keys = new ArrayList<>(CLocations.get().getKeys(false));
        Collections.shuffle(keys);

        HashMap<String, Integer> randomTreasure = getRandomTreasure();

        for (String key : keys) {
            String ID = CLocations.get().getString(key + ".treasure ID");
            Location location = getLocation(key);

            if (!CTreasures.get().getKeys(false).contains(ID)) {
                CLocations.removeLocation(location); continue;
            }

            Treasure treasure = CTreasures.getTreasure(ID);
            location.getBlock().setType(Material.AIR);
            setPresent(location, false);

            if (treasure.getRandomise()) {
                if (randomTreasure.get(treasure.getID()) <= 0) continue;
                randomTreasure.put(treasure.getID(), randomTreasure.get(treasure.getID()) - 1);
            }

            treasure.place(location);
            setPresent(location);
        }
    }

    /**
     * @param location Location of block
     * @return boolean If that location is in the config
     */
    public static boolean contains(Location location) {
        String ID = getLocationID(location);
        return CLocations.get().getKeys(false).contains(ID);
    }

    /**
     * @param location Location of block
     * @return String Treasure ID at this location
     */
    public static String getTreasureID(Location location) {
        String ID = getLocationID(location);
        return CLocations.get().getString(ID + ".treasure ID");
    }

    /**
     * @param treasureID treasure ID
     * @return Integer amount of treasure
     */
    public static int getTotalTreasure(String treasureID) {
        int count = 0;
        for (String key : CLocations.get().getKeys(false)) {
            if (!Objects.equals(CLocations.get().getString(key + ".treasure ID"), treasureID)) continue;
            count += 1;
        }
        return count;
    }

    /**
     * @return ArrayList<Location> locations of treasures left to find
     */
    public static ArrayList<Location> getTreasuresLeft() {
        ArrayList<Location> locations = new ArrayList<>();

        for (String key : CLocations.get().getKeys(false)) {
            if (!CLocations.get().getBoolean(key + ".present")) continue;
            locations.add(getLocation(key));
        }

        return locations;
    }

    /**
     * @return ArrayList<Location> locations of treasures left to find
     */
    public static ArrayList<Location> getTreasuresLeft(int max) {
        ArrayList<Location> locations = new ArrayList<>();
        int current = 0;

        for (String key : CLocations.get().getKeys(false)) {
            if (!CLocations.get().getBoolean(key + ".present")) continue;
            if (current >= max) return locations;
            locations.add(getLocation(key));
            current += 1;
        }

        return locations;
    }

    public static int getAmountOfLocationsToSpawn() {
        int amount = 0;

        for (Treasure treasure : CTreasures.getAllTreasure()) {
            if (treasure.getRandomise()) amount += getTotalTreasure(treasure.getID()) / 2;
            else amount += getTotalTreasure(treasure.getID());
        }

        return amount;
    }

    public static String getAmountOfLocationsToSpawn(String treasureID) {
        Treasure treasure = CTreasures.getTreasure(treasureID);
        if (treasure.getRandomise()) return String.valueOf(getTotalTreasure(treasureID) / 2);
        else return String.valueOf(getTotalTreasure(treasureID));
    }

    public static void changeIDs(String before, String after) {
        for (String key : CLocations.get().getKeys(false)) {
            String value = CLocations.get().getString(key + ".treasure ID");
            if (!Objects.equals(value, before)) continue;
            CLocations.get().set(key + ".treasure ID", after);
        }
        CLocations.save();
    }
}