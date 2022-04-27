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
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

public class CTreasures {

    private static File file;
    private static FileConfiguration configFile;

    // Generates or finds file
    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("SmScavenger").getDataFolder(), "treasures.yml");
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

    public static void setupDefaults() {
        CTreasures.get().addDefault("loveheart.HDB", "9298");
        CTreasures.get().addDefault("loveheart.material", false);
        CTreasures.get().addDefault("loveheart.rewards.essetials kit", "loveheart");
        CTreasures.get().addDefault("loveheart.particle.type", "Fireworks_Spark");
        CTreasures.get().addDefault("loveheart.particle.amount", 10);
        CTreasures.get().addDefault("loveheart.sound.type", "Block_Note_Block_Chime");
    }

    /**
     * @param path Path to parse string
     * @param alt Return this when there is an error
     * @return Parsed String
     */
    public static String parseString(String path, String alt) {
        try {
            String parsed = CTreasures.get().getString(path);
            if (parsed == null) return alt;
            return parsed;
        } catch (Exception e) {return alt;}
    }

    /**
     * @param path Path to parse material
     * @param alt Return this when there is an error
     * @return Parsed Material
     */
    public static Material parseMaterial(String path, Material alt) {
        try {
            String parsed = CTreasures.get().getString(path);
            if (parsed == null) return alt;
            return Material.valueOf(parsed.toUpperCase(Locale.ROOT));
        } catch (Exception e) {return alt;}
    }

    /**
     * @param path Path to parse particle
     * @param alt Return this when there is an error
     * @return Parsed Particle
     */
    public static Particle parseParticle(String path, Particle alt) {
        try {
            String parsed = CTreasures.get().getString(path);
            if (parsed == null) return alt;
            return Particle.valueOf(parsed.toUpperCase(Locale.ROOT));
        } catch (Exception e) {return alt;}
    }

    /**
     * @param path Path to parse integer
     * @param alt Return this when there is an error
     * @return Parsed Integer
     */
    public static int parseInt(String path, int alt) {
        try {
            String parsed = CTreasures.get().getString(path);
            if (parsed == null) return alt;
            return alt;
        } catch (Exception e) {return alt;}
    }

    /**
     * @param path Path to parse sound
     * @param alt Return this when there is an error
     * @return Parsed Sound
     */
    public static Sound parseSound(String path, Sound alt) {
        try {
            String parsed = CTreasures.get().getString(path);
            if (parsed == null) return alt;
            return Sound.valueOf(parsed.toUpperCase(Locale.ROOT));
        } catch (Exception e) {return alt;}
    }

    /**
     * @param ID Treasure ID
     * @return Treasure
     */
    public static Treasure getTreasure(String ID) {
        Treasure treasure = new Treasure(ID);

        treasure.setMaterial(parseMaterial(ID + ".material", Material.AIR));
        treasure.setMaterial(parseString(ID + ".HDB", null));
        treasure.setReward(parseString(ID + ".rewards.essetials kit", null));
        treasure.setParticle(parseParticle(ID + ".particle.type", Particle.FIREWORKS_SPARK),
                parseInt(ID + ".particle.amount", 10));
        treasure.setSound(parseSound(ID + ".sound.type", Sound.BLOCK_NOTE_BLOCK_CHIME));

        return treasure;
    }

    /**
     * @param treasure Treasure to create
     */
    public static void createTreasure(Treasure treasure) {
        String ID = treasure.getID();

        CTreasures.get().set(ID + ".HDB", treasure.getHDB());
        CTreasures.get().set(ID + ".material", treasure.getMaterial().toString());
        CTreasures.get().set(ID + ".rewards.essetials kit", treasure.getKitReward());
        CTreasures.get().set(ID + ".particle.type", treasure.getParticleType().toString());
        CTreasures.get().set(ID + ".particle.amount", treasure.getParticleAmount());
        CTreasures.get().set(ID + ".sound.type", treasure.getSoundType().toString());

        CTreasures.save();
    }

    /**
     * @return Array of all treasures
     */
    public static ArrayList<Treasure> getAllTreasure() {
        ArrayList<Treasure> treasure = new ArrayList<Treasure>();
        for (String treasureID : CTreasures.get().getKeys(false)) {
            treasure.add(getTreasure(treasureID));
        }
        return treasure;
    }

    /**
     * @return Array of all IDs
     */
    public static ArrayList<String> getIDList() {
        Set<String> keys = CTreasures.get().getKeys(false);
        return new ArrayList<String>(keys);
    }
}