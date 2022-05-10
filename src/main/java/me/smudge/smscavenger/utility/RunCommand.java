package me.smudge.smscavenger.utility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RunCommand {

    public static void asOp(Player player, String command) {

        if (player.isOp()) {
            Bukkit.dispatchCommand(player, command);
        } else {
            try {
                player.setOp(true);
                Bukkit.dispatchCommand(player, command);
            } finally {
                player.setOp(false);
            }
        }
    }
}
