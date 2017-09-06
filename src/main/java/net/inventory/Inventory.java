package net.inventory;

import java.util.HashMap;
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

    public Player Player; public String Name; public Integer Bars; public InventoryAction Action; public Map<Integer, ItemStack> Stacks; public Integer BuildDelay; public Boolean BuildInventory;

    public Inventory(Player Player, String Name, Integer Bars, InventoryAction Action, Boolean BuildInventory, Integer BuildDelay, Plugin Plugin) {
        this.Player = Player; this.Name = Name; this.Bars = Bars; this.Action = Action; this.BuildInventory = BuildInventory; this.BuildDelay = BuildDelay;
        InventoryListener.registerListener(this);
    }

    public org.bukkit.inventory.Inventory putClass(Map<Integer, ItemStack> Stacks, Boolean openRun) {
        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(Player.getPlayer(), 9 * Bars, Name);
        if(BuildInventory) {
        	if(openRun) 
        		this.Player.openInventory(inv);
        	this.BuildInventory(inv, Stacks);
        } else {
            for (Integer Slot : Stacks.keySet()) inv.setItem(Slot, Stacks.get(Slot));
            if(openRun)
            	this.Player.openInventory(inv);
        }
        this.Stacks = Stacks;
        return inv;
    }

    private void BuildInventory(org.bukkit.inventory.Inventory inv, Map<Integer, ItemStack> Stacks) {
        if(BuildInventory) {
            for(Integer Slot : Stacks.keySet()) {
                try {
                    Thread.sleep(this.BuildDelay);
                    inv.setItem(Slot, Stacks.get(Slot));
                    this.Player.updateInventory();
                    this.Player.playSound(this.Player.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
                } catch (Exception ex) {
                    break;
                }
            }
        } this.Stacks = Stacks;
    }
    
    public static class InventoryListener {
    	private static HashMap<String, Inventory> Actions = new HashMap<>();
    	public static void registerListener(Inventory Inventory) {
    		if (Actions.containsKey(Inventory.Name))
    			Actions.remove(Inventory.Name);
    		Actions.put(Inventory.Name, Inventory);
     	}
    	public static void unregisterListener(Inventory Inventory) {
    		if (Actions.containsKey(Inventory.Name))
    			Actions.remove(Inventory.Name);
    	}
    	public static void unregisterListener(String Name) {
    		if (Actions.containsKey(Name))
    			Actions.remove(Name);
    	}
    	
    	public static void registerListeners(Plugin Plugin) {
    		Bukkit.getPluginManager().registerEvents(new Listener() {

    		    @EventHandler
    		    public void on(InventoryOpenEvent Event) {
    		    	if(Actions.containsKey(Event.getInventory().getName())) {
    		    		Actions.get(Event.getInventory().getName()).Action.onOpen(Actions.get(Event.getInventory().getName()).Player, Event.getInventory());
    		        }
    		    }

    		    @EventHandler
    		    public void on(InventoryCloseEvent Event) {
    		    	if(Actions.containsKey(Event.getInventory().getName())) {
    		            Actions.get(Event.getInventory().getName()).Action.onClose(Actions.get(Event.getInventory().getName()).Player, Event.getInventory());
    		        }
    		    }

    		    @EventHandler
    		    public void on(InventoryClickEvent Event) {
    		    	if(Actions.containsKey(Event.getInventory().getName())) {
    		    		Inventory inv = Actions.get(Event.getInventory().getName());
    		            if (Event.getRawSlot() >= 0 && Event.getRawSlot() <= 9 * inv.Bars) {
    		                Event.setCancelled(true);
    		                if(inv.Stacks.containsKey(Event.getSlot())) {
    		                    inv.Action.onClick(inv.Player, Event.getClickedInventory(), Event.getSlot());
    		                }
    		            }
    		        }
    		    }

    		    @EventHandler
    		    public void on(InventoryDragEvent Event) {    		        
    		    	if(Actions.containsKey(Event.getInventory().getName())) 
    		    		Event.setCancelled(true);
    		    }
			}, Plugin);
    	}
    }
    
}