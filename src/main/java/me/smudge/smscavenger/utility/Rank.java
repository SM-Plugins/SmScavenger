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

import org.jetbrains.annotations.NotNull;

public class Rank implements Comparable<Rank> {

    String name;
    String permission;
    int points;

    /**
     * @param name Name of player
     * @param points Players points
     * @param permission Players specific permission
     */
    public Rank(String name, int points, String permission) {
        this.name = name;
        this.points = points;
        this.permission = permission;
    }

    /**
     * @return String player name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return Integer player points
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * @return String player permission
     */
    public String getPermission() {
        return this.permission;
    }

    @Override
    public int compareTo(@NotNull Rank rank) {
        return this.points;
    }
}
