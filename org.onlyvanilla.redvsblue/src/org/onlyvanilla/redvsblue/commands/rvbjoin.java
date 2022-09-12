package org.onlyvanilla.redvsblue.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.onlyvanilla.redvsblue.Main;

import net.md_5.bungee.api.ChatColor;

public class rvbjoin implements CommandExecutor  {
	
	private Main mainClass = Main.getInstance();
	
	List<String> participantList = (List<String>)mainClass.getParticipants().getStringList("participants");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		  if (sender instanceof Player) {
	            Player p = (Player) sender;
	            	if(cmd.getName().equalsIgnoreCase("rvbjoin")) {
	            		if(checkIfSignedUp(p) == false) {
	            			EnterPlayer(p, participantList);
	            	}
	            }
		  }         	
		return true;
	}
	
	public boolean checkIfSignedUp(Player p) {	
		for(String s : (List<String>)mainClass.getParticipants().getStringList("participants")) {
			if(s.equals(p.getName())) { 
				p.sendMessage(ChatColor.RED + "You are already on the event roster!");
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5F, 0.5F);
				return true;
			}
		}
		return false;
	}
	
	public void EnterPlayer(Player p, List<String> participantList) {
					
			//might cause mixups
			participantList.add(p.getName());
			mainClass.getParticipants().set("participants", participantList);
			
			for(Player pl : Bukkit.getServer().getOnlinePlayers()) {
				pl.getWorld().playSound(pl.getLocation(), Sound.ENTITY_PANDA_SNEEZE, 1.0F, 1.0F);
			}
					
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage(ChatColor.GOLD + "  ★  "  + p.getName() + " has joined" + ChatColor.RED + ""
																						   + ChatColor.BOLD + " RED"
																						   + ChatColor.YELLOW + " VS"
																						   + ChatColor.BLUE + ""
																						   + ChatColor.BOLD + " BLUE");
			Bukkit.broadcastMessage(" ");
			
			p.sendMessage(ChatColor.RED + "PLEASE JOIN THE DISCORD TO BE ADDED INTO THE EVENT CHANNEL! /DISCORD.");
			p.sendMessage(ChatColor.DARK_RED + "You will be added to a team once the admin is online. Any points you gain will transfer to your new team.");
			
			mainClass.saveParticipantsFile();
			mainClass.reloadParticipantsFile();
	}
}
