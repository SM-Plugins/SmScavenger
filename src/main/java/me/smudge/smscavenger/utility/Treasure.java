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

package me.smudge.smscavenger.utility;

import me.smudge.smscavenger.configs.CLocations;
import me.smudge.smscavenger.configs.CTreasures;
import me.smudge.smscavenger.dependencys.HeadDatabase;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;

public class Treasure {

    String ID;
    String HDB;
    Material material;
    String command;
    Particle particleType;
    int particleAmount;
    Sound soundType;
    boolean firework;
    ArrayList<Color> fireworkColors = new ArrayList<>();
    boolean randomise;
    int respawnTime;

    /**
     * Initializes class
     *
     * @param identifier is the ID of the treasure in the config
     */
    public Treasure(String identifier) {
        this.ID = identifier;
    }

    /**
     * @param ID Treasure ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * @param material The material of the treasure
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * @param HDB The ID of the head
     */
    public void setMaterial(String HDB) {
        this.HDB = HDB;
    }

    /**
     * @param command Command to execute as a reward
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * @param particleType Particle type
     * @param particleAmount Particle amount
     */
    public void setParticle(Particle particleType, int particleAmount) {
        this.particleType = particleType;
        this.particleAmount = particleAmount;
    }

    /**
     * @param soundType Sound type
     */
    public void setSound(Sound soundType) {
        this.soundType = soundType;
    }

    /**
     * @param firework Will a firework appear
     */
    public void setFirework(boolean firework) {
        this.firework = firework;
    }

    /**
     * @param color Color to add the firework
     */
    public void addFireworkColor(Color color) {
        this.fireworkColors.add(color);
    }

    /**
     * @param randomise Randomise which location is used
     */
    public void setRandomise(boolean randomise) {this.randomise = randomise;}

    /**
     * @param timer The time between breaking and respawning the treasure
     */
    public void setTimer(int timer) {
        this.respawnTime = timer;
    }

    /**
     * @param location Location of where to place the treasure
     */
    public void place(Location location) {
        Block block = location.getBlock();

        if (this.HDB != null) {
            if (HeadDatabase.get() == null) {
                Send.log(this.ID + ": Tried placing but the plugin HeadDatabase doesnt exist. {loc} location has been deleted"
                        .replace("{loc}", CLocations.getLocationID(location)));

                CLocations.removeLocation(location);
                return;
            }
            block.setType(Material.PLAYER_HEAD);
            HeadDatabase.get().setBlockSkin(block, this.HDB);
        }

        if (this.material != null && this.material != Material.AIR) {
            block.setType(this.material);
        }
    }

    /**
     * @return String kit reward
     */
    public String getCommand() {
        return this.command;
    }

    /**
     * @return String ID
     */
    public String getID() {
        return this.ID;
    }

    /**
     * @return Material of treasure
     */
    public Material getMaterial() {
        try { return this.material; }
        catch (Exception e) {return Material.AIR;}
    }

    /**
     * @return HDB ID
     */
    public String getHDB() {
        try { return this.HDB; }
        catch (Exception e) {return null;}
    }

    /**
     * @return Particle particle type
     */
    public Particle getParticleType() {
        return this.particleType;
    }

    /**
     * @return Intager particle amount
     */
    public int getParticleAmount() {
        return this.particleAmount;
    }

    /**
     * @return Sound sound type
     */
    public Sound getSoundType() {
        return this.soundType;
    }

    /**
     * @return boolean is there a firework
     */
    public boolean getFirework() {
        return this.firework;
    }

    /**
     * @return List of colors in the firework
     */
    public ArrayList<Color> getFireworkColors() {
        return this.fireworkColors;
    }

    /**
     * @return boolean will the locations be randomised
     */
    public boolean getRandomise() {
        return this.randomise;
    }

    /**
     * @return Amount of time till respawn
     */
    public int getTimer() {
        return this.respawnTime;
    }

    /**
     * @return ItemStack treasure item
     */
    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(this.getMaterial());

        if (this.getHDB() == null) return item;

        if (HeadDatabase.get() == null) return new ItemStack(Material.PLAYER_HEAD);
        return Objects.requireNonNull(HeadDatabase.get()).getItemHead(this.getHDB());
    }

    public void delete() {
        CTreasures.get().set(this.ID, null);
        CTreasures.save();
    }
}
