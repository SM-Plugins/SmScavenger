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

package me.smudge.smscavenger.guis;

import me.smudge.smscavenger.utility.Send;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public abstract class GUI {

    /**
     * inventoriesByUUID<Inventory UUID, GUI instance>
     * openInventories<Player UUID, Inventory UUID>
     */
    public static Map<UUID, GUI> inventoriesByUUID = new HashMap<>();
    public static Map<UUID, UUID> openInventories = new HashMap<>();

    private final UUID uuid; // GUI UUID
    protected Inventory inventory; // GUI Inventory
    private final Map<Integer, GUIClickAction> clickActions;
    private final Map<Integer, GUIChangeAction> changeActions;
    private final Map<Integer, GUIChatAction> chatActions;
    private GUICloseAction closeAction;

    protected Player player; // Most resent to open

    /**
     * @param inventorySize Size of the inventory
     * @param inventoryName Name of the inventory
     */
    public GUI(int inventorySize, String inventoryName) {
        uuid = UUID.randomUUID();
        inventory = Bukkit.createInventory(null, inventorySize, Send.convert(inventoryName));
        clickActions = new HashMap<>();
        closeAction = null;
        changeActions = new HashMap<>();
        chatActions = new HashMap<>();
        inventoriesByUUID.put(getUuid(), this);
    }

    /** When a player clicks the item */
    public interface GUIClickAction {
        void click(Player player);
    }

    /** When a player changes an item */
    public interface GUIChangeAction {
        void change(ItemStack item);
    }

    /** When the inventory closes */
    public interface GUICloseAction {
        void close(Player player);
    }

    /** When a player clicks this item they
     * will be asked to input a value */
    public interface GUIChatAction {
        void chat(String value);
    }

    /**
     * @param slot The slot to put the item
     * @param item The item to put into the slow
     * @param action GUI action
     */
    public void setItemClick(int slot, ItemStack item, GUIClickAction action) {
        inventory.setItem(slot, item);
        if (action != null){clickActions.put(slot, action);}
    }

    /**
     * @param slot The slot to put the item
     * @param item The item to put into the slow
     * @param action GUI action
     */
    public void setChangeableItem(int slot, ItemStack item, GUIChangeAction action) {
        inventory.setItem(slot, item);
        if (action != null) { changeActions.put(slot, action); }
    }

    /**
     * @param slot The slot to put the item
     * @param item The item to put into the slow
     * @param action GUI action
     */
    public void setChatItem(int slot, ItemStack item, GUIChatAction action) {
        inventory.setItem(slot, item);
        if (action != null) { chatActions.put(slot, action); }
    }

    /**
     * @param slot The slot to put the item
     * @param item The item to put into the slow
     */
    public void setItem(int slot, ItemStack item) {
        setItemClick(slot, item, null);
    }

    /**
     * @param action GUI action
     */
    public void setClose(GUICloseAction action) {
        if (action != null){this.closeAction = action;}
    }

    /**
     * @param player Player that will open the GUI
     */
    public void open(Player player) {
        player.openInventory(inventory);
        openInventories.put(player.getUniqueId(), getUuid());
        this.player = player;
    }

    public void delete() {
        for (Player player : Bukkit.getOnlinePlayers()){
            UUID u = openInventories.get(player.getUniqueId());
            if (u.equals(getUuid())) {player.closeInventory();}
        }
        inventoriesByUUID.remove(getUuid());
    }

    /** @return UUID of this GUI */
    public UUID getUuid() {return uuid;}

    /** @return Inventory of this GUI */
    public Inventory getInventory() {return inventory;}

    /** @return Map of inventoriesByUUID<Inventory UUID, GUI instance> */
    public static Map<UUID, GUI> getInventoriesByUUID() {return inventoriesByUUID;}

    /** @return Map of openInventories<Player UUID, Inventory UUID> */
    public static Map<UUID, UUID> getOpenInventories() {return openInventories;}

    /** @return Map of clickActions<Slot, Action> */
    public Map<Integer, GUIClickAction> getClickActions() {return clickActions;}

    /** @return Map of closeActions<Slot, Action> */
    public GUICloseAction getCloseAction() {return this.closeAction;}

    /** @return Map of moveableActions<Slot, Action> */
    public Map<Integer, GUIChangeAction> getChangeActions() {return this.changeActions;}

    /** @return Map of chatActions<Slot, Action> */
    public Map<Integer, GUIChatAction> getChatActions() {return this.chatActions;}

    /** @return ItemStack filler item */
    public ItemStack getFillerItem() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(Send.convert("&7"));
        item.setItemMeta(meta);
        return item;
    }
}
