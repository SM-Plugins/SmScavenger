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

import me.smudge.smscavenger.configs.CData;

import java.util.ArrayList;
import java.util.Collections;

public class Leaderboard {

    CData config;

    public Leaderboard() {this.config = new CData();}

    /**
     * @param path Which leaderboard to get
     * @return Leaderboard
     */
    public ArrayList<Rank> getLeaderboard(String path) {
        ArrayList<Rank> leaderboard = new ArrayList<>();

        for (String name : this.config.get().getKeys(false)) {
            // Create record
            Rank record = new Rank(
                    name,
                    this.config.get().getInt(name + "." + path),
                    this.config.get().getString(name + ".permission")
            );
            // Add record to leaderboard
            leaderboard.add(record);
        }
        // Sort and return
        Collections.sort(leaderboard);
        return leaderboard;
    }
}
