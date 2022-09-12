package org.onlyvanilla.redvsblue.commands;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

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
import org.bukkit.inventory.meta.SkullMeta;
import org.onlyvanilla.redvsblue.Main;
import org.onlyvanilla.redvsblue.statsandpoints.getPlayerStats;
import org.onlyvanilla.redvsblue.statsandpoints.getTotalTeamPoints;
import org.onlyvanilla.redvsblue.statsandpoints.sortTopTeammates;

import net.md_5.bungee.api.ChatColor;

public class rvbstandings implements CommandExecutor, Listener{
	
	private static Inventory inv;
	
	//getTotalTeamPoints instance
	static getTotalTeamPoints gttp1 = new getTotalTeamPoints();
	
	//sortTopTeammates instance
	static sortTopTeammates stt1 = new sortTopTeammates();
	
	//getPlayerStats instance
	static getPlayerStats gpst1 = new getPlayerStats();
	
	//Main instance
	private static Main mainClass = Main.getInstance();
	
	//getPlayerStats instance
	static getPlayerStats gps1 = new getPlayerStats();
	
	//Get playerdataconfig
	static FileConfiguration playerDataConfig = mainClass.getPlayerData();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("rvbstandings")) {         	
        	inv = Bukkit.createInventory(null, 9, "Red VS Blue | Standings");
        	openInventory(p);
        	initializeItems(p);
        }	
		return true;
	}
	
	public static void initializeItems(Player p) {
		//get teamdata from playerdata.yml
		ConfigurationSection teamData = playerDataConfig.getConfigurationSection("TeamPoints");
		
		//get user data
		int totalPlayerPoints = playerDataConfig.getConfigurationSection(p.getName()).getConfigurationSection("stats").getInt("total-points");

		//get red team points
		int redTeamPoints = teamData.getInt("red-team");
		//get blue team points
		int blueTeamPoints = teamData.getInt("blue-team");
		
		if(redTeamPoints >= blueTeamPoints) {
			inv.addItem(createGuiItem(Material.RED_WOOL, ChatColor.RED + "" + ChatColor.BOLD + "RED TEAM", ChatColor.GRAY + "Points: " + redTeamPoints));
			inv.addItem(createGuiItem(Material.BLUE_WOOL, ChatColor.BLUE + "" + ChatColor.BOLD + "BLUE TEAM", ChatColor.GRAY + "Points: " + blueTeamPoints));
		} else {
			inv.addItem(createGuiItem(Material.BLUE_WOOL, ChatColor.BLUE + "" + ChatColor.BOLD + "BLUE TEAM", ChatColor.GRAY + "Points: " + blueTeamPoints));
			inv.addItem(createGuiItem(Material.RED_WOOL, ChatColor.RED + "" + ChatColor.BOLD + "RED TEAM", ChatColor.GRAY + "Points: " + redTeamPoints));
		}
		
		if(mainClass.getPlayerData().getConfigurationSection(p.getName()).getString("team").equals("red")) {
			inv.addItem(createGuiItem(Material.LIGHT_GRAY_WOOL, ChatColor.GRAY + "" + ChatColor.BOLD + "YOUR POINTS", ChatColor.GRAY + "Points: " + totalPlayerPoints,
					ChatColor.RED + "You are on team red!"));
		} else if(mainClass.getPlayerData().getConfigurationSection(p.getName()).getString("team").equals("blue")) {
			inv.addItem(createGuiItem(Material.LIGHT_GRAY_WOOL, ChatColor.GRAY + "" + ChatColor.BOLD + "YOUR POINTS", ChatColor.GRAY + "Points: " + totalPlayerPoints,
					ChatColor.BLUE + "You are on team blue!"));
		} else {
			inv.addItem(createGuiItem(Material.LIGHT_GRAY_WOOL, ChatColor.GRAY + "" + ChatColor.BOLD + "YOUR POINTS", ChatColor.GRAY + "Points: " + totalPlayerPoints,
					ChatColor.GRAY + "You are not on a team! Use /rvbjoin to join a team!"));
		}
		
		//sort players
		Map<String, Integer> unsortedTopPlayers = stt1.getPlayers(playerDataConfig);
		
		LinkedHashMap<String, Integer> reverseSortedTopPlayers = new LinkedHashMap<>();
		
		unsortedTopPlayers.entrySet()
		.stream()
		.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
		.forEachOrdered(x -> reverseSortedTopPlayers.put(x.getKey(), x.getValue()));
		
		int i = 1;
		for(Map.Entry<String, Integer> entry : reverseSortedTopPlayers.entrySet()) {
			if(i < 6) {
				String IGN = entry.getKey();
				int points = entry.getValue();
				
				ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta meta = (SkullMeta) skull.getItemMeta();
				meta.setOwner(IGN);
				skull.setItemMeta(meta);
				
				if(playerDataConfig.getConfigurationSection(IGN).getString("team").equals("blue")) {
					getPlayerTeamSkull(i, skull, points, IGN);
				} else if(playerDataConfig.getConfigurationSection(IGN).getString("team").equals("red")){
					getPlayerTeamSkull(i, skull, points, IGN);
				} else {
					continue;
				}

				i++;
			} else {
				break;
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
    
    public static void getPlayerTeamSkull(int i, ItemStack skull, int points, String IGN) {
    	inv.setItem(i+3, createGuiSkull(skull, ChatColor.YELLOW + IGN,
				ChatColor.GRAY + "Points: " + ChatColor.WHITE + points,
				ChatColor.GRAY + "Team: " + gps1.findPlayerTeam(IGN),
				ChatColor.GOLD + "#" + i));
    }
}
