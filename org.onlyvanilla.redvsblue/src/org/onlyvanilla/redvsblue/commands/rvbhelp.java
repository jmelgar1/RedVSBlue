package org.onlyvanilla.redvsblue.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class rvbhelp implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            	if(cmd.getName().equalsIgnoreCase("rvbhelp")) {
            		sendMessage(player);
            }
        }
        return true;
    } 
    public void sendMessage(Player p) {
    	p.sendMessage(ChatColor.GRAY + "-----------" + ChatColor.RED + "RED " + ChatColor.YELLOW + "VS"
				+ ChatColor.BLUE + " BLUE" 
				+ ChatColor.GRAY + "-----------");
    	
    	p.sendMessage(ChatColor.YELLOW + "- /rvbhelp " + ChatColor.GREEN + "(This page)");
    	p.sendMessage(ChatColor.YELLOW + "- /rvbjoin " + ChatColor.GREEN + "(Join the event. It's free)");
    	p.sendMessage(ChatColor.YELLOW + "- /rvblist " + ChatColor.GREEN + "(List current participants)");
    	p.sendMessage(ChatColor.YELLOW + "- /rvbstandings " + ChatColor.GREEN + "(View top 5 players and team points)");
    	p.sendMessage(ChatColor.YELLOW + "- /rvbtasks " + ChatColor.GREEN + "(View the tasks for this tournament)");
    	p.sendMessage(ChatColor.YELLOW + "- /rvbprizes " + ChatColor.GREEN + "(View tournament prizes)");
    	p.sendMessage(ChatColor.YELLOW + "- /rvbrules " + ChatColor.GREEN + "(View tournament rules)");
    	p.sendMessage(ChatColor.YELLOW + "- /teamdiscord " + ChatColor.GREEN + "(Coming soon)");
    	p.sendMessage(ChatColor.GRAY + "-------------------------------------");
    	
    }
}
