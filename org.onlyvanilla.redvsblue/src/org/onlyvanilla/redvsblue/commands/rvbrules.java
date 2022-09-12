package org.onlyvanilla.redvsblue.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class rvbrules implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            	if(cmd.getName().equalsIgnoreCase("rvbrules")) {
            		sendMessage(player);
            }
        }
        return true;
    } 
    public void sendMessage(Player p) {
    	p.sendMessage(ChatColor.GRAY + "-----------" + ChatColor.RED + "RED " + ChatColor.YELLOW + "VS"
				+ ChatColor.BLUE + " BLUE" + ChatColor.YELLOW + " RULES"
				+ ChatColor.GRAY + "-----------");
    	
    	p.sendMessage(ChatColor.YELLOW + "- If inactive for 10 consecutive days you will be removed from the competition.");
    	p.sendMessage(ChatColor.GOLD + "- You must participate in 15/30 tasks to be eligible for rewards.");
    	p.sendMessage(ChatColor.YELLOW + "- Do not kill teammates unless agreed upon.");
    	p.sendMessage(ChatColor.GOLD + "- Do not attempt to sabotage your team in any way.");
    	p.sendMessage(ChatColor.GRAY + "-------------------------------------");
    	
    }
}
