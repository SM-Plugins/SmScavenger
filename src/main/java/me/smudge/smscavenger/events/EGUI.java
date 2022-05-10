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

import me.smudge.smscavenger.guis.GUI;
import me.smudge.smscavenger.utility.Send;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EGUI implements Listener {

    private static final HashMap<String, GUI.GUIChatAction> chatEvents = new HashMap<>();
    public static boolean cancelMovable;

    @EventHandler
    public void onClick(InventoryClickEvent event){
        // Check if its in players inventory
        if (!(event.getWhoClicked() instanceof Player)) {return;}
        if (event.getRawSlot() > event.getInventory().getSize()) return;

        Player player = (Player) event.getWhoClicked();
        UUID playerUUID = player.getUniqueId();

        UUID inventoryUUID = GUI.openInventories.get(playerUUID);
        if (inventoryUUID != null){

            GUI gui = GUI.getInventoriesByUUID().get(inventoryUUID);
            GUI.GUIClickAction clickAction = gui.getClickActions().get(event.getSlot());
            GUI.GUIChatAction chatAction = gui.getChatActions().get(event.getSlot());

            GUI.GUIChangeAction changeAction = null;
            try { changeAction = gui.getChangeActions().get(event.getSlot());
            } catch (Exception ignored) {};

            if (changeAction == null) {event.setCancelled(true);}

            if (clickAction != null){clickAction.click(player);}

            if (chatAction != null) {
                chatEvents.put(player.getName(), chatAction);
                player.closeInventory();
                Send.player(player, "{prefix} Please type in chat the value for this option.");
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Inventory inventory = event.getInventory();

        UUID inventoryUUID = GUI.openInventories.get(playerUUID);
        GUI.openInventories.remove(playerUUID);

        if (chatEvents.containsKey(player.getName())) return;
        if (cancelMovable) {cancelMovable = false; return;}

        if (inventoryUUID != null){
            GUI gui = GUI.getInventoriesByUUID().get(inventoryUUID);

            if (gui.getCloseAction() != null) {gui.getCloseAction().close(player);}

            if (gui.getChangeActions() == null) return;
            for (Map.Entry<Integer, GUI.GUIChangeAction> action : gui.getChangeActions().entrySet()) {
                if (inventory.getItem(action.getKey()) == null) return;
                action.getValue().change(inventory.getItem(action.getKey()));
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        GUI.openInventories.remove(playerUUID);
    }

    @EventHandler
    public void onChatEvent(PlayerChatEvent event) {
        if (!chatEvents.containsKey(event.getPlayer().getName())) return;

        Player player = event.getPlayer();
        String value = event.getMessage();
        chatEvents.get(player.getName()).chat(value);
        chatEvents.remove(player.getName());

        event.setCancelled(true);
    }
}
