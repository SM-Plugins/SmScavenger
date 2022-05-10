package me.smudge.smscavenger.dependencys;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;

public class HeadDatabase {

    public static HeadDatabaseAPI get() {
        if (Bukkit.getPluginManager().getPlugin("HeadDatabase") == null) {return null;}
        return new HeadDatabaseAPI();
    }
}
