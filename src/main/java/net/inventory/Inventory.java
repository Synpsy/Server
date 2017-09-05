package net.inventory;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
 
public class Inventory implements Listener {

    public interface InventoryAction {
        void onOpen(Player Player, org.bukkit.inventory.Inventory inv);
        void onClick(Player Player, org.bukkit.inventory.Inventory inv, Integer Slot);
        void onClose(Player Player, org.bukkit.inventory.Inventory inv);
    }

    Player Player; String Name; Integer Bars; InventoryAction Action; Map<Integer, ItemStack> Stacks; Boolean BuildInventory;

    public Inventory(Player Player, String Name, Integer Bars, InventoryAction Action, Boolean BuildInventory, Plugin Plugin) {
        this.Player = Player; this.Name = Name; this.Bars = Bars; this.Action = Action; this.BuildInventory = BuildInventory;
        Bukkit.getPluginManager().registerEvents(this, Plugin);
    }

    public org.bukkit.inventory.Inventory putClass(Map<Integer, ItemStack> Stacks) {
        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(Player.getPlayer(), 9 * Bars, Name);
        if(BuildInventory) {
            this.Player.openInventory(inv);
            this.BuildInventory(inv, Stacks);
        } else {
            for (Integer Slot : Stacks.keySet()) inv.setItem(Slot, Stacks.get(Slot));
            this.Player.openInventory(inv);
        }
        this.Stacks = Stacks;
        return inv;
    }

    private void BuildInventory(org.bukkit.inventory.Inventory inv, Map<Integer, ItemStack> Stacks) {
        if(BuildInventory) {
            for(Integer Slot : Stacks.keySet()) {
                try {
                    Thread.sleep(250);
                    inv.setItem(Slot, Stacks.get(Slot));
                    this.Player.updateInventory();
                    this.Player.playSound(this.Player.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
                } catch (Exception ex) {
                    break;
                }
            }
        } this.Stacks = Stacks;
    }

    /* LISTENERS */

    @EventHandler
    public void on(InventoryOpenEvent Event) {
        if(Event.getInventory().getName().equals(this.Name)) {
            Action.onOpen(this.Player, Event.getInventory());
        }
    }

    @EventHandler
    public void on(InventoryCloseEvent Event) {
        if(Event.getInventory().getName().equals(this.Name)) {
            Action.onClose(this.Player, Event.getInventory());
        }
    }

    @EventHandler
    public void on(InventoryClickEvent Event) {
        if(Event.getInventory().getName().equals(this.Name)) {
            if (Event.getRawSlot() >= 0 && Event.getRawSlot() <= 9 * this.Bars) {
                Event.setCancelled(true);
                if(this.Stacks.containsKey(Event.getSlot())) {
                    Action.onClick(this.Player, Event.getClickedInventory(), Event.getSlot());
                }
            }
        }
    }

    @EventHandler
    public void on(InventoryDragEvent Event) {
        if(Event.getInventory().getName().equals(this.Name)) {
            Event.setCancelled(true);
        }
    }
    
}