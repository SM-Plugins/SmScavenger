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

package me.smudge.smscavenger.events;

import me.smudge.smscavenger.configs.CConfig;
import me.smudge.smscavenger.configs.CData;
import me.smudge.smscavenger.configs.CLocations;
import me.smudge.smscavenger.configs.CTreasures;
import me.smudge.smscavenger.utility.Send;
import me.smudge.smscavenger.utility.Treasure;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class EClickEvent implements Listener {

    @EventHandler
    public static void onClickEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Was it a click event
        if (event.getAction() != Action.LEFT_CLICK_BLOCK
            && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        Location location = Objects.requireNonNull(block).getLocation();

        // Is this block in location file and present
        if (!CLocations.contains(location)) return;
        if (!CLocations.isPresent(location)) return;

        // Have they redeemed their max amount
        CData config = new CData();
        if (!(CConfig.getMax() <= -1) && config.getPlayerTotal(player.getName()) >= CConfig.getMax()) {
            Send.playerError(player, "You have already found &f" + CConfig.getMax());
            return;
        }

        // Remove block
        block.setType(Material.AIR);
        CLocations.setPresent(location, false);

        // Get treasure
        Treasure treasure = CTreasures.getTreasure(CLocations.getTreasureID(location));

        // Add info to config
        String path = player.getName();
        config.get().set(path + ".permission", CConfig.getPermissionOfPlayer(player));
        config.incrementTreasure(path, treasure.getID());
        config.save();

        // Particle and sounds
        World world = location.getWorld();
        assert world != null;

        world.spawnParticle(treasure.getParticleType(), location, treasure.getParticleAmount());
        world.playSound(location, treasure.getSoundType(), 1, 1);

        // Broadcast
        String message = CConfig.getMessageTreasureFound();
        Send.all(message
                .replace("{player}", player.getName())
                .replace("{treasureID}", treasure.getID()));
    }
}
