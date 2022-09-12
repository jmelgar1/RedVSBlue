package org.onlyvanilla.redvsblue.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
import org.bukkit.inventory.meta.SkullMeta;
import org.onlyvanilla.redvsblue.Main;
import org.onlyvanilla.redvsblue.statsandpoints.getPlayerStats;

import net.md_5.bungee.api.ChatColor;

public class rvbtasks implements CommandExecutor, Listener{
	
	private static Inventory inv;
	
	static //getPlayerStats instance
	getPlayerStats gps1 = new getPlayerStats();
	
	
	static //all items used in gui
	Material[] items = {Material.GLOW_INK_SAC, Material.DIAMOND_ORE, Material.SADDLE, Material.GUNPOWDER, Material.BRICK, Material.BRICK, Material.ENDER_EYE, Material.ENCHANTED_BOOK,
			Material.PAINTING, Material.PAINTING, Material.PRISMARINE_SHARD, Material.EMERALD_ORE, Material.PUFFERFISH, Material.GOLD_INGOT, Material.RAIL, Material.RAIL,
			Material.POTION, Material.ENCHANTED_BOOK, Material.PAINTING, Material.PAINTING, Material.SCULK_SENSOR, Material.ANCIENT_DEBRIS, Material.CARROT, Material.SLIME_BALL,
			Material.NOTE_BLOCK, Material.NOTE_BLOCK, Material.PARROT_SPAWN_EGG, Material.ENCHANTED_BOOK, Material.PAINTING, Material.PAINTING};
	
	static ChatColor[] colors = {ChatColor.AQUA, ChatColor.AQUA, ChatColor.LIGHT_PURPLE, ChatColor.DARK_GREEN, ChatColor.RED, ChatColor.RED, ChatColor.WHITE, ChatColor.DARK_PURPLE,
			ChatColor.YELLOW, ChatColor.YELLOW, ChatColor.BLUE, ChatColor.GREEN, ChatColor.BLUE, ChatColor.GOLD, ChatColor.GRAY, ChatColor.GRAY, ChatColor.LIGHT_PURPLE, ChatColor.DARK_PURPLE,
			ChatColor.YELLOW, ChatColor.YELLOW, ChatColor.DARK_BLUE, ChatColor.RED, ChatColor.GOLD, ChatColor.GREEN, ChatColor.WHITE, ChatColor.WHITE, ChatColor.GREEN,
			ChatColor.DARK_PURPLE, ChatColor.YELLOW, ChatColor.YELLOW};
	
	static String[] points = {"3", "20", "3000", "2", "10000", "10000", "500", "5000", "10000", "10000", "100", "30", "5", "3", "10000", "10000", "5", 
			"5000", "10000", "10000", "3000", "50", "5", "3", "10000", "10000", "10", "5000", "10000", "10000"};
	
	static String[] tasks = {"Kill Glow Squids", "Mine Diamond Ore", "Take a team screenshot", "Kill Creepers", "Build a team base", "Build a team base", "Kill Enderdragons",
			"Special Coordinates 1", "Map Art (Theme: Meme)", "Map Art (Theme: Meme)", "Kill Elder Guardians", "Mine Emerald Ore", "Catch Fish", "Kill Piglins",
			"Build the longest railroad", "Build the longest railroad", "Kill Witches", "Special Coordinates 2", "Map Art (Theme: Movie Poster)", "Map Art (Theme: Movie Poster)",
			"Kill Wardens", "Mine Ancient Debris", "Breed Animals (Will be replaced)", "Kill Slimes", "Build a song out of noteblocks", "Build a song out of noteblocks", "Kill Parrots",
			"Special Coordinates 3", "Map Art (Theme: Funny Portrait)", "Map Art (Theme: Funny Portrait)"};
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("rvbtasks")) {         	
        	inv = Bukkit.createInventory(null, 54, "RED VS BLUE | Tasks");
        	openInventory(p);
        	initializeItems(p);
        }	
		return true;
	}
	
	public static void initializeItems(Player p) {

	//player head info
	ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
	SkullMeta meta = (SkullMeta) skull.getItemMeta();
	meta.setOwner(p.getName());
	skull.setItemMeta(meta);
	
	//add rank
	inv.setItem(4, createGuiSkull(skull, ChatColor.GRAY + "" + ChatColor.BOLD + "USER: " + ChatColor.WHITE + p.getName(), 
			ChatColor.GRAY + "" + ChatColor.BOLD + "TEAM: " + ChatColor.WHITE + gps1.findPlayerTeam(p.getName())));
	
	int inventorySlot = 10;	
	for(int i = 0; i < 30; i++) {
		inv.setItem(inventorySlot, createGuiItem(items[i], colors[i] + tasks[i], 
				ChatColor.GRAY + "Points: " + ChatColor.WHITE + points[i], 
				ChatColor.GRAY + "Date: " + ChatColor.WHITE + "9/" + (i+1)));
		
		if(i==6 || i==13 || i==20 || i==27) {
			inventorySlot += 3;
		} else {
			inventorySlot++;
			}
		}	
	}
	protected static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();
		
		//set the name of item
		meta.setDisplayName(name);
		
		//set lore of item
		meta.setLore(Arrays.asList(lore));
		
		item.setItemMeta(meta);
		
		return item;
	}
	
    protected static ItemStack createGuiSkull(final ItemStack skull, final String name, final String... lore) {
    	final ItemMeta meta = skull.getItemMeta();
    	
    	meta.setDisplayName(name);
    	
    	meta.setLore(Arrays.asList(lore));
    	
    	skull.setItemMeta(meta);
    	
    	return skull;
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
