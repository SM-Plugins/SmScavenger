package me.smudge.smscavenger.configs;

import me.smudge.smscavenger.utility.Send;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class CLog {

    private static File file;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("SmScavenger").getDataFolder(), "data/log.txt");
        if (!file.exists()) {try {file.createNewFile();} catch (IOException ignored) {}}
    }

    public static void add(String string) {
        try {
            Files.write(Paths.get(file.getPath()), string.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Unable to write");
        }
    }
}
