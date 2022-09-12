package org.onlyvanilla.redvsblue.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.onlyvanilla.redvsblue.Main;
import org.onlyvanilla.redvsblue.statsandpoints.getPlayerStats;

import net.md_5.bungee.api.ChatColor;

public class rvblist implements CommandExecutor {
	
	private Main mainClass = Main.getInstance();
	
	//getPlayerStats instance
	static getPlayerStats gps1 = new getPlayerStats();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
    		if(cmd.getName().equalsIgnoreCase("rvblist")) {
    			getList(p);
    	}
		return true;
	}
	public void getList(Player p) {
		p.sendMessage(ChatColor.GRAY + "-----" + ChatColor.RED + "RED " + ChatColor.YELLOW + "VS"
				+ ChatColor.BLUE + " BLUE" 
				+ ChatColor.GRAY + "-----");

		int i = 0;
		for(String s : (List<String>)mainClass.getParticipants().getStringList("participants")) {
			i++;
			p.sendMessage(ChatColor.YELLOW + "" + i + ". " + ChatColor.GRAY + s + " " + gps1.findPlayerTeam(s));
		}
	}
}
