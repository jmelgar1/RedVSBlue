package org.onlyvanilla.redvsblue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.onlyvanilla.redvsblue.commands.rvbaddpoints;
import org.onlyvanilla.redvsblue.commands.rvbhelp;
import org.onlyvanilla.redvsblue.commands.rvbjoin;
import org.onlyvanilla.redvsblue.commands.rvblist;
import org.onlyvanilla.redvsblue.commands.rvbprizes;
import org.onlyvanilla.redvsblue.commands.rvbrules;
import org.onlyvanilla.redvsblue.commands.rvbstandings;
import org.onlyvanilla.redvsblue.commands.rvbtasks;
import org.onlyvanilla.redvsblue.statsandpoints.getPlayerStats;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener{
	
	//Main instance
	private static Main instance;
	
	//main config.yml
	FileConfiguration config;
	File cfile;
	
	//player data file
	private File playerDataFile;
	private FileConfiguration playerData;
	
	//participant file
	private File participantsFile;
	private FileConfiguration participants;
		
	public static void main(String[] args) {

	}
	
		@Override
		public void onEnable() {
			
			instance = this;
			System.out.println("(!) Red VS Blue Enabled");	
			
			//get main config
			this.saveDefaultConfig();
			config = getConfig();
			config.options().copyDefaults(true);
			saveConfig();
			cfile = new File(getDataFolder(), "config.yml");
	
			//create playerdatafilesss
			createPlayerDataFile();
			createParticipantFile();
			
			//plugin commands
			this.getCommand("rvbjoin").setExecutor(new rvbjoin());
			this.getCommand("rvblist").setExecutor(new rvblist());
			this.getCommand("rvbhelp").setExecutor(new rvbhelp());
			this.getCommand("rvbstandings").setExecutor(new rvbstandings());
			this.getCommand("rvbtasks").setExecutor(new rvbtasks());
			this.getCommand("rvbprizes").setExecutor(new rvbprizes());
			this.getCommand("rvbrules").setExecutor(new rvbrules());
			this.getCommand("rvbaddpoints").setExecutor(new rvbaddpoints());
			
			//reminder runnable
			sendPlayerReminder();
			
			//new day notificaiton
			//NewDayNotification();
			
			//event registers
			getServer().getPluginManager().registerEvents(new getPlayerStats(), this);
			getServer().getPluginManager().registerEvents(new rvbstandings(), this);
			getServer().getPluginManager().registerEvents(new rvbtasks(), this);
			getServer().getPluginManager().registerEvents(new rvbprizes(), this);
		}
		
		@Override
		public void onDisable() {
			System.out.println("(!) Red VS Blue Disabled");	
			
			//save configs
			saveParticipantsFile();
			savePlayerDataFile();
		}
		
		//Main instance
		public static Main getInstance() {
			return instance;
		}
		
		//PLAYER DATA FILE
		public void savePlayerDataFile() {
			try {
				playerData.save(playerDataFile);
			} catch (IOException e) {
				Bukkit.getConsoleSender().sendMessage("Couldn't save playerdata.yml");
			}
		}
		
		public void reloadPlayerDataFile() {
			playerData = YamlConfiguration.loadConfiguration(playerDataFile);
		}
		
		//get player data file
		public FileConfiguration getPlayerData() {
			return this.playerData;
		}
		
		//create player data file
		private void createPlayerDataFile() {
			playerDataFile = new File(getDataFolder(), "playerdata.yml");
			if(!playerDataFile.exists()) {
				playerDataFile.getParentFile().mkdirs();
				saveResource("playerdata.yml", false);
				System.out.println("(!) playerdata.yml created");
			}
			
			playerData = new YamlConfiguration();
			try {
				playerData.load(playerDataFile);
				System.out.println("(!) playerdata.yml loaded");
			} catch(IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			} 
		}
		
		//PARTICIPANTS FILE
		public FileConfiguration getParticipants() {
			return this.participants;
		}
		
		public void saveParticipantsFile() {
			try {
				participants.save(participantsFile);
			} catch (IOException e) {
				Bukkit.getConsoleSender().sendMessage("Couldn't save participants.yml");
			}
		}
		
		public void reloadParticipantsFile() {
			participants = YamlConfiguration.loadConfiguration(participantsFile);
		}
		
		//create player data file
		private void createParticipantFile() {
			participantsFile = new File(getDataFolder(), "participants.yml");
			if(!participantsFile.exists()) {
				participantsFile.getParentFile().mkdirs();
				saveResource("participants.yml", false);
				System.out.println("(!) participants.yml created");	
			}
			
			participants = new YamlConfiguration();
			try {
				participants.load(participantsFile);
				System.out.println("(!) participants.yml loaded");
			} catch(IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			} 	
		}
	
		/*
		 * RUNNABLES
		 */
		
		public void sendPlayerReminder() {
			new BukkitRunnable() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					for(Player p : getServer().getWorld("world").getPlayers()) {
						p.sendMessage(ChatColor.YELLOW + "Join the " + ChatColor.RED + ""
								+ ChatColor.BOLD + "RED " + ChatColor.YELLOW + " VS " + ChatColor.BLUE +
								ChatColor.BOLD + " BLUE" + ChatColor.YELLOW + " using /rvbjoin");
						p.sendMessage("");
						p.sendMessage(ChatColor.YELLOW + "The event lasts from September 1st to the 30th. It is free to join!");
						p.sendMessage("");
						p.sendMessage(ChatColor.YELLOW + "More information on our " + ChatColor.BLUE + "/discord!");
					}
				}
				//54000 ticks or 45 minutes
			}.runTaskTimerAsynchronously(this, 0, 150000);
		}
		
//		public void NewDayNotification() {
//			new BukkitRunnable() {
//				
//				@SuppressWarnings("deprecation")
//				@Override
//				public void run() {
//					
//					
//					Bukkit.broadcastMessage(ChatColor.RED + "RED " + ChatColor.YELLOW + "VS" + ChatColor.BLUE + " BLUE" + ChatColor.YELLOW + ": New day. New task!");
//				}
//				//54000 ticks or 45 minutes
//			}.runTaskTimerAsynchronously(this, 0, 20);
//		}
		
		//get the day
		public static int getDay(String date) {
			LocalDate currentDate = LocalDate.parse(date);
			
			int day = currentDate.getDayOfMonth();
			return day;
		}
		
		//get the month
		public static int getMonth(String date) {
			LocalDate currentDate = LocalDate.parse(date);
			
			int month = currentDate.getMonthValue();
			return month;
		}
	}