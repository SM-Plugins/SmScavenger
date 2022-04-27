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

import me.smudge.smscavenger.configs.CConfig;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Send {

    /**
     * Quick method to convert colour codes and add prefix
     * @param message Message to convert
     * @return String message converted
     */
    public static String convert(String message) {
        String prefix = CConfig.getPrefix();
        return ChatColor.translateAlternateColorCodes('&', message
                .replace("{prefix}", prefix));
    }

    /**
     * @param player Player to send the message to
     * @param message Message to send to the player
     */
    public static void player(Player player, String message) {
        String prefix = CConfig.getPrefix();
        player.sendMessage(convert(message.replace("{prefix}", Objects.requireNonNull(prefix))));
    }

    /**
     * @param player Player to send the component to
     * @param message Message component to send
     */
    public static void playerTextComponent(Player player, TextComponent message) {
        player.spigot().sendMessage(message);
    }

    /**
     * @param player Player to send error to
     * @param message Error to send to player
     */
    public static void playerError(Player player, String message) {
        player.sendMessage(convert("&7&l> &7" + message));
    }

    /**
     * @param message Message to send to all players
     */
    public static void all(String message) {
        String prefix = CConfig.getPrefix();
        Bukkit.broadcastMessage(convert(message.replace("{prefix}", Objects.requireNonNull(prefix))));
    }

    /**
     * @param message Message to log in console
     */
    public static void log(String message) {
        Bukkit.getLogger().info(message);
    }
}
