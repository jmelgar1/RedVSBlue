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

public class rvbaddpoints implements CommandExecutor, Listener{
	
	//getTotalTeamPoints instance
	static getTotalTeamPoints gttp1 = new getTotalTeamPoints();
	
	//Main instance
	private static Main mainClass = Main.getInstance();
	//Get playerdataconfig
	static FileConfiguration playerDataConfig = mainClass.getPlayerData();
	
	//get teampoints section
	ConfigurationSection teamData = playerDataConfig.getConfigurationSection("TeamPoints");	

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("rvbaddpoints")) {    
        	if(p.hasPermission("rvb.rvbaddpoints")) {
        		
        		String team = args[0];
        		String points = args[1];
        		
        		if(team.equals("red")) {
        			addPointsToTeam("red-team", points, p);
        		} else if(team.equals("blue")){
        			addPointsToTeam("blue-team", points, p);
        		} else {
        			p.sendMessage(ChatColor.RED + "Enter valid team.");
        		}
        	}
        }	
		return true;
	}
	
	public void addPointsToTeam(String team, String points, Player p) {
		int teamPoints = teamData.getInt(team);
		teamPoints += Integer.valueOf(points);
		teamData.set(team, teamPoints);
		p.sendMessage(ChatColor.GREEN + "Added " + points + " to team blue! Total is now " + teamPoints);
		
		mainClass.saveParticipantsFile();
	}
}
