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

	// thread.sleep erstezten
	// hashmap mit nonclick items adden
	// popupsound einstellen

	public Player Player;
	public String Name;
	public Integer Bars;
	public InventoryAction Action;
	public Map<Integer, ItemStack> Stacks, NonStacks;
	public Integer BuildDelay;
	public Boolean BuildInventory;
	public Sound BuildSound;
	public Plugin Plugin;

	public Inventory(Player Player, String Name, Integer Bars, InventoryAction Action, Integer BuildDelay, Sound BuildSound, Plugin Plugin) {
		this.Player = Player;
		this.Name = Name;
		this.Bars = Bars;
		this.Action = Action;
		this.Plugin = Plugin;
		this.BuildInventory = true;
		this.BuildDelay = (BuildDelay != null ? BuildDelay : 250);
		this.BuildSound = (BuildSound != null ? BuildSound : Sound.CHICKEN_EGG_POP);
		InventoryListener.registerListener(this);
	}
	
	public Inventory(Player Player, String Name, Integer Bars, InventoryAction Action, Plugin Plugin) {
		this.Player = Player;
		this.Name = Name;
		this.Bars = Bars;
		this.Action = Action;
		this.Plugin = Plugin;
		this.BuildInventory = false;
		this.BuildDelay = (BuildDelay != null ? BuildDelay : 250);
		this.BuildSound = (BuildSound != null ? BuildSound : Sound.CHICKEN_EGG_POP);
		InventoryListener.registerListener(this);
	}

	public org.bukkit.inventory.Inventory putClass(Map<Integer, ItemStack> Stacks, Map<Integer, ItemStack> NonStacks, Boolean openRun) {
		org.bukkit.inventory.Inventory inv = Bukkit.createInventory(Player.getPlayer(), 9 * Bars, Name);
		if (BuildInventory) {
			if (openRun)
				this.Player.openInventory(inv);
			this.BuildInventory(inv, Stacks, NonStacks);
		} else {
			if(Stacks != null)
			for (Integer Slot : Stacks.keySet())
				inv.setItem(Slot, Stacks.get(Slot));
			
			if(NonStacks != null)
			for(Integer Slot : NonStacks.keySet())
				inv.setItem(Slot, NonStacks.get(Slot));
			
			if (openRun)
				this.Player.openInventory(inv);
		}
		this.Stacks = Stacks; this.NonStacks = NonStacks;
		return inv;
	}

	Integer CountSlot;
	Integer BuildSchedule;
	
	public void StopBuild(Boolean keepValues) {
		if(BuildSchedule != null) {
			Bukkit.getScheduler().cancelTask(BuildSchedule);
			if(!keepValues) {CountSlot = null; BuildSchedule = null;}
		}
	}
	
	private Boolean isBuild = false;
	
	private void BuildInventory(org.bukkit.inventory.Inventory inv, Map<Integer, ItemStack> Stacks, Map<Integer, ItemStack> NonStacks) {
		if (BuildInventory) {
			Map<Integer, ItemStack> Stack = new HashMap<>();
			
			for(Integer Slot : Stacks.keySet()) 
				Stack.put(Slot, Stacks.get(Slot));
			
			for(Integer Slot : NonStacks.keySet()) 
				Stack.put(Slot, NonStacks.get(Slot));
			
			
			CountSlot = 0;
			BuildSchedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin, new Runnable() {
				
				@Override
				public void run() {
					if(CountSlot < Stack.size()) {
						Integer Slot = getSlot(Stack, CountSlot);
						ItemStack itemStack = Stack.get(Slot);
						inv.setItem(Slot, itemStack);
						Player.updateInventory();
						Player.playSound(Player.getLocation(), BuildSound, 1, 1);
						isBuild = false;
						CountSlot ++;
					} else {
						isBuild = true;
						StopBuild(true);
					}
				}
				
			}, 0, this.BuildDelay);
			
		}
		this.Stacks = Stacks;
	}
	
	private Integer getSlot(Map<Integer, ItemStack> Stack, Integer Number) {
		int i = 0;
		for(Integer Slot : Stack.keySet()) {
			if(i == Number) {
				return Slot;
			}
			i++;
		}
		return -1;
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
					if (Actions.containsKey(Event.getInventory().getName())) {
						Actions.get(Event.getInventory().getName()).Action
								.onOpen(Actions.get(Event.getInventory().getName()).Player, Event.getInventory());
					}
				}

				@EventHandler
				public void on(InventoryCloseEvent Event) {
					if (Actions.containsKey(Event.getInventory().getName())) {
						Actions.get(Event.getInventory().getName()).StopBuild(false);
						Actions.get(Event.getInventory().getName()).Action.onClose(Actions.get(Event.getInventory().getName()).Player, Event.getInventory());
					}
				}

				@EventHandler
				public void on(InventoryClickEvent Event) {
					if (Actions.containsKey(Event.getInventory().getName())) {
						Inventory inv = Actions.get(Event.getInventory().getName());
						if (Event.getRawSlot() >= 0 && Event.getRawSlot() <= 9 * inv.Bars) {
							Event.setCancelled(true);
							if(inv.isBuild) {
								if (inv.Stacks.containsKey(Event.getSlot())) {
									inv.Action.onClick(inv.Player, Event.getClickedInventory(), Event.getSlot());
								}
							}
						}
					}
				}

				@EventHandler
				public void on(InventoryDragEvent Event) {
					if (Actions.containsKey(Event.getInventory().getName()))
						Event.setCancelled(true);
				}
			}, Plugin);
		}
	}

}