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

package me.smudge.smscavenger.dependencys;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.smudge.smscavenger.configs.CConfig;
import me.smudge.smscavenger.configs.CData;
import me.smudge.smscavenger.configs.CLocations;
import me.smudge.smscavenger.utility.Leaderboard;
import me.smudge.smscavenger.utility.Rank;
import me.smudge.smscavenger.utility.Send;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlaceholderAPI extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "smscavenger";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Smudge";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "null";

        String[] args = params.split("_");

        if (Objects.equals(args[0], "current")) return getCurrent(args[1]);
        if (Objects.equals(args[0], "total")) return getTotal(args[1]);
        if (Objects.equals(args[0], "leaderboard")) return getLeaderboard(args[1], args[2]);

        return "null";
    }

    /**
     * @param type Which leaderboard to get
     * @param rank What rank to get from the leaderboard
     * @return String leaderboard rank
     */
    private String getLeaderboard(String type, String rank) {
        Leaderboard leaderboard = new Leaderboard();

        Rank record;
        try {record = leaderboard.getLeaderboard(type).get(Integer.parseInt(rank) - 1);
        } catch (Exception e) {return "null";}

        return CConfig.getMessageLeaderboard()
                .replace("{rank}", rank)
                .replace("{player}", record.getName())
                .replace("{amount}", String.valueOf(record.getPoints()))
                .replace("{permission}", record.getPermission());
    }

    /**
     * @param treasureID Treasure ID
     * @return String current found treasures
     */
    private String getCurrent(String treasureID) {
        CData config = new CData();
        return String.valueOf(config.getTotalTreasure(treasureID));
    }

    /**
     * @param treasureID Treasure ID
     * @return String total treasures hidden
     */
    private String getTotal(String treasureID) {
        return CLocations.getAmountOfLocationsToSpawn(treasureID);
    }
}