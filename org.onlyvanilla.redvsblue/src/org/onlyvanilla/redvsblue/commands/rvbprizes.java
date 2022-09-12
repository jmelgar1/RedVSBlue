package org.onlyvanilla.redvsblue.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.onlyvanilla.redvsblue.Main;
import org.onlyvanilla.redvsblue.statsandpoints.getTotalTeamPoints;
import org.onlyvanilla.redvsblue.statsandpoints.sortTopTeammates;

import net.md_5.bungee.api.ChatColor;

public class rvbprizes implements CommandExecutor, Listener{
	
	private static Inventory inv;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("rvbprizes")) {         	
        	inv = Bukkit.createInventory(null, 36, "Red VS Blue | Prizes");
        	openInventory(p);
        	initializeItems(p);
        }	
		return true;
	}
	
	public static void initializeItems(Player p) {
		
		//MVP Rewards
		inv.setItem(0, createGuiItem(Material.BEACON, 1, ChatColor.AQUA + "" + ChatColor.BOLD + "MVP", ChatColor.GRAY + "Given to the player with the most impact."));
		inv.setItem(9, createGuiItem(Material.NETHER_STAR, 1, ChatColor.AQUA + "" + ChatColor.BOLD + "MVP Trophy"));
		inv.setItem(18, createGuiItem(Material.EXPERIENCE_BOTTLE, 1, ChatColor.AQUA + "" + ChatColor.BOLD + "MVP Discord Rank"));
		
		//1st Place rewards
		inv.setItem(2, createGuiItem(Material.GOLD_BLOCK, 1, ChatColor.GOLD + "" + ChatColor.BOLD + "1st Place", ChatColor.GRAY + "Given to the player with the most points."));
		inv.setItem(11, createGuiItem(Material.GOLD_INGOT, 1, ChatColor.GOLD + "" + ChatColor.BOLD + "1st Place Trophy"));
		inv.setItem(20, createGuiItem(Material.NETHERITE_BLOCK, 3, ChatColor.WHITE + "Block of Netherite"));
		inv.setItem(29, createGuiItem(Material.ENCHANTED_GOLDEN_APPLE, 1, ChatColor.GOLD + "" + ChatColor.BOLD + "Tier 1 Donation Rank"));
		
		//2nd Place rewards
		inv.setItem(3, createGuiItem(Material.IRON_BLOCK, 1, net.md_5.bungee.api.ChatColor.of("#C0C0C0") + "" + ChatColor.BOLD + "2nd Place", ChatColor.GRAY + "Given to the player with the 2nd most points."));
		inv.setItem(12, createGuiItem(Material.IRON_INGOT, 1, net.md_5.bungee.api.ChatColor.of("#C0C0C0") + "" + ChatColor.BOLD + "2nd Place Trophy"));
		inv.setItem(21, createGuiItem(Material.NETHERITE_BLOCK, 2, ChatColor.WHITE + "Block of Netherite"));
		inv.setItem(30, createGuiItem(Material.GOLDEN_APPLE, 1, net.md_5.bungee.api.ChatColor.of("#C0C0C0") + "" + ChatColor.BOLD + "Tier 2 Donation Rank"));
	
		//3rd place rewards
		inv.setItem(4, createGuiItem(Material.COPPER_BLOCK, 1, net.md_5.bungee.api.ChatColor.of("#B87333") + "" + ChatColor.BOLD + "3rd Place", ChatColor.GRAY + "Given to the player with the 3rd most points."));
		inv.setItem(13, createGuiItem(Material.COPPER_INGOT, 1, net.md_5.bungee.api.ChatColor.of("#B87333") + "" + ChatColor.BOLD + "3rd Place Trophy"));
		inv.setItem(22, createGuiItem(Material.NETHERITE_BLOCK, 1, ChatColor.WHITE + "Block of Netherite"));
		inv.setItem(31, createGuiItem(Material.APPLE, 1, net.md_5.bungee.api.ChatColor.of("#B87333") + "" + ChatColor.BOLD + "Tier 3 Donation Rank"));
		
		//red team rewards
		inv.setItem(7, createGuiItem(Material.RED_WOOL, 1, ChatColor.RED + "" + ChatColor.BOLD + "Red Team", ChatColor.GRAY + "Given red team participants."));
		inv.setItem(16, createGuiItem(Material.RED_DYE, 1, ChatColor.RED + "Red Team | September 2022", ChatColor.GRAY + "Participant: " + p.getName(), 
				ChatColor.GRAY + "Quote: (Quote here)"));
		
		//blue team rewards
		inv.setItem(8, createGuiItem(Material.BLUE_WOOL, 1, ChatColor.BLUE + "" + ChatColor.BOLD + "Blue Team", ChatColor.GRAY + "Given blue team participants."));
		inv.setItem(17, createGuiItem(Material.BLUE_DYE, 1, ChatColor.BLUE + "Blue Team | September 2022", ChatColor.GRAY + "Participant: " + p.getName(), 
				ChatColor.GRAY + "Quote: (Quote here)"));
		
	}
	protected static ItemStack createGuiItem(final Material material, int amount, final String name, final String... lore) {
		final ItemStack item = new ItemStack(material, amount);
		final ItemMeta meta = item.getItemMeta();
		
		//set the name of item
		meta.setDisplayName(name);
		
		//set lore of item
		meta.setLore(Arrays.asList(lore));
		
		item.setItemMeta(meta);
		
		return item;
	}
	
	public void openInventory(final HumanEntity ent) {
		ent.openInventory(inv);
	}
	
	@EventHandler
	public void onInventoryClick(final InventoryClickEvent event) {
		if(!event.getInventory().equals(inv)) return;
		
		event.setCancelled(true);
		
		final ItemStack clickedItem = event.getCurrentItem();
		
		//verify current item is not null
		if (clickedItem == null || clickedItem.getType().isAir()) return;
		
		final Player p = (Player) event.getWhoClicked();
		
		//maybe add join function
	}
	
	@EventHandler
	public void onInventoryClick(final InventoryDragEvent event) {
		if(event.getInventory().equals(inv)) {
			event.setCancelled(true);
		}
	}
	
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e, Player p) {
    	HandlerList.unregisterAll(this);
    	Bukkit.getServer().getScheduler().runTaskLater(Main.getPlugin(Main.class), p::updateInventory, 1L);
    }
}
