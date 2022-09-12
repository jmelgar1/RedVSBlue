package org.onlyvanilla.redvsblue.statsandpoints;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.onlyvanilla.redvsblue.Main;
import org.bukkit.event.player.PlayerJoinEvent;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.ChatColor;
import net.luckperms.api.LuckPermsProvider;

public class getPlayerStats implements Listener {
	
	//Main instance
	private Main mainClass = Main.getInstance();
	
	//GetTotalTeamPoints instance
	getTotalTeamPoints gttp1 = new getTotalTeamPoints();
	
	//Get playerdataconfig
	FileConfiguration playerDataConfig = mainClass.getPlayerData();
	
	//Luckperms api
	LuckPerms api = LuckPermsProvider.get();
	
	//date
	LocalDateTime now = LocalDateTime.now();
	int currentMonth = now.getMonthValue();
	int currentDay = now.getDayOfMonth();
	//adjust for server time (4+ EST)
	int hour = now.getHour();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		Player p = event.getPlayer();
		
		if(playerDataConfig.get(p.getName()) == null) {
			ConfigurationSection playerIGN = playerDataConfig.createSection(p.getName());
			playerIGN.createSection("stats");
			mainClass.savePlayerDataFile();
			setPlayerTeam(p);			
		} else {
			setPlayerTeam(p);		
		}
	}
	
	//sets the players team in config from finding group with luckperms api
	public void setPlayerTeam(Player p) {
		ConfigurationSection playerIGN = playerDataConfig.getConfigurationSection(p.getName());
		
		//Find user team
		String username = p.getName();
		Group redTeam = api.getGroupManager().getGroup("red");
		Group blueTeam = api.getGroupManager().getGroup("blue");
		
		//get user groups
		Collection<Group> inheritedGroups = api.getUserManager().getUser(username).getInheritedGroups(QueryOptions.nonContextual());
		
		for(Group g : inheritedGroups) {
			if(g.equals(redTeam)) {
				playerIGN.set("team", "red");
				break;
			} else if(g.equals(blueTeam)){
				playerIGN.set("team", "blue");
				break;
			} else {
				playerIGN.set("team", "none");
			}
		}
		mainClass.savePlayerDataFile();
	}
	
	//returns player team from finding group with luckperms api
	public String findPlayerTeam(String username) {		
		//Find user team
		Group redTeam = api.getGroupManager().getGroup("red");
		Group blueTeam = api.getGroupManager().getGroup("blue");
		
		UUID userUUID = Bukkit.getOfflinePlayer(username).getUniqueId();
		User user = api.getUserManager().loadUser(userUUID).join();
		
		//get user groups
		Collection<Group> inheritedGroups = user.getInheritedGroups(QueryOptions.defaultContextualOptions());
		
		for(Group g : inheritedGroups) {
			if(g.equals(redTeam)) {
				return ChatColor.RED + "RED";
			} else if(g.equals(blueTeam)){
				return ChatColor.BLUE + "BLUE";
			}
		}
		mainClass.savePlayerDataFile();
		
		//if player is not on red or blue return none
		return "NONE";
	}
	
	public void notInConfig(Player p, Main mainClass, FileConfiguration playerDataConfig, String configKey, int points) {		
		//creates new user in config
		ConfigurationSection playerIGN = playerDataConfig.getConfigurationSection(p.getName());
		ConfigurationSection statsSection = playerIGN.getConfigurationSection("stats");
		statsSection.set(configKey, points);
		statsSection.set("total-points", points);
		
		//Find user team
		String username = p.getName();
		Group redTeam = api.getGroupManager().getGroup("red");
		Group blueTeam = api.getGroupManager().getGroup("blue");
		
		//get user groups
		Collection<Group> inheritedGroups = api.getUserManager().getUser(username).getInheritedGroups(QueryOptions.nonContextual());
		
		for(Group g : inheritedGroups) {
			if(g.equals(redTeam)) {
				playerIGN.set("team", "red");
				break;
			} else if(g.equals(blueTeam)){
				playerIGN.set("team", "blue");
				break;
			} else {
				playerIGN.set("team", "none");
			}
		}
		mainClass.savePlayerDataFile();
	}
	
	public FileConfiguration getDataConfig(){
		return playerDataConfig;
	}
	
	/*
	 * MOB KILL COUNTERS
	 */
	
	//Counts glow squid kills on 9/1/22
	@EventHandler
	public void killGlowSquid(EntityDeathEvent event) {
		killMobCounter(event, currentDay, currentMonth, 1, 
				9, EntityType.GLOW_SQUID, playerDataConfig, "kill-glow-squid", mainClass, 3);
	}
	
	//Counts creeper kills on 9/4/22
	@EventHandler
	public void killCreeper(EntityDeathEvent event) {
		killMobCounter(event, currentDay, currentMonth, 4, 
				9, EntityType.CREEPER, playerDataConfig, "kill-creeper", mainClass, 2);
	}
	
	//counts enderdragon kills on 9/7/22
	@EventHandler
	public void killEnderdragon(EntityDeathEvent event) {
		killMobCounter(event, currentDay, currentMonth, 7, 
				9, EntityType.ENDER_DRAGON, playerDataConfig, "kill-enderdragon", mainClass, 500);
	}
	
	//counts elder guardians kills on 9/11/22
	@EventHandler
	public void killElderGuardian(EntityDeathEvent event) {
		killMobCounter(event, currentDay, currentMonth, 11, 
				9, EntityType.ELDER_GUARDIAN, playerDataConfig, "kill-elder-guardian", mainClass, 100);
	}
	
	//counts piglin kills on 9/14/22
	@EventHandler
	public void killPiglin(EntityDeathEvent event) {
		killMobCounter(event, currentDay, currentMonth, 14, 
				9, EntityType.PIGLIN, playerDataConfig, "kill-piglin", mainClass, 3);
	}
	
	//counts witch kills on 9/17/22
	@EventHandler
	public void killWitch(EntityDeathEvent event) {
		killMobCounter(event, currentDay, currentMonth, 17, 
				9, EntityType.WITCH, playerDataConfig, "kill-witch", mainClass, 5);
	}
	
	//counts warden kills on 9/21/22
	@EventHandler
	public void killWarden(EntityDeathEvent event) {
		killMobCounter(event, currentDay, currentMonth, 21, 
				9, EntityType.WARDEN, playerDataConfig, "kill-warden", mainClass, 3000);
	}
	
	//counts slimes kills on 9/24/22
	@EventHandler
	public void killSlime(EntityDeathEvent event) {
		killMobCounter(event, currentDay, currentMonth, 24, 
				9, EntityType.SLIME, playerDataConfig, "kill-slime", mainClass, 3);
	}
	
	//counts parrot kills on 9/27/22
	@EventHandler
	public void killParrot(EntityDeathEvent event) {
		killMobCounter(event, currentDay, currentMonth, 27, 
				9, EntityType.PARROT, playerDataConfig, "kill-parrot", mainClass, 10);
	}
	
	/*
	 * BLOCK COUNTERS
	 */
	
	@EventHandler
	public void breakDiamondOre(BlockBreakEvent event) {
		blockBreakCounter(event, currentDay, currentMonth, 2, 
				9, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE, playerDataConfig, "break-diamond-ore", mainClass, 20);
	}
	
	//subtracts a point if player places diamond ore. Realistically could use metadata or coreprotect api but this isnt a big deal.
	
	@EventHandler
	public void placeDiamondOre(BlockPlaceEvent event) {
		blockPlaced(event, currentDay, currentMonth, 2, 
				9, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE, playerDataConfig, "break-diamond-ore", mainClass, 20);
	}
	
	@EventHandler
	public void breakEmeraldOre(BlockBreakEvent event) {
		blockBreakCounter(event, currentDay, currentMonth, 12, 
				9, Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE, playerDataConfig, "break-emerald-ore", mainClass, 30);
	}
	
	//subtracts a point if player places diamond emerald ore
	
	@EventHandler
	public void placeEmeraldOre(BlockPlaceEvent event) {
		blockPlaced(event, currentDay, currentMonth, 12, 
				9, Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE, playerDataConfig, "break-emerald-ore", mainClass, 30);
	}
	
	@EventHandler
	public void breakAncientDebrisBlock(BlockBreakEvent event) {
		blockBreakCounter(event, currentDay, currentMonth, 22, 
				9, Material.ANCIENT_DEBRIS, Material.ANCIENT_DEBRIS, playerDataConfig, "break-ancient-debris", mainClass, 50);
	}
	
	//subtracts a point if player places ancient debris ore
	
	@EventHandler
	public void placeAncientDebrisBlock(BlockPlaceEvent event) {
		blockPlaced(event, currentDay, currentMonth, 22, 
				9, Material.ANCIENT_DEBRIS, Material.ANCIENT_DEBRIS, playerDataConfig, "break-ancient-debris", mainClass, 50);
	}
	
	//fish counter
	@EventHandler
	public void fishCounter(PlayerFishEvent event) {
		fishCaughtCounter(event, currentDay, currentMonth, 13,
				9, playerDataConfig, "fish-caught", mainClass, 5);
	}
	
	//mob kill counter template
	public void killMobCounter(EntityDeathEvent event, int currentDay, int currentMonth, 
			int requiredDay, int requiredMonth, EntityType entityType, FileConfiguration playerDataConfig, 
			String configKey, Main mainClass, int points) {
		
		//if hour is between 12am-3:59am keep same day
		if(hour <= 3) {
			currentDay =- 1;
		}
		
		LivingEntity entity = event.getEntity();
		Player p = entity.getKiller();
		
		//check for current date and ensure tasks are done in a certain time frame
		if(currentDay == requiredDay && currentMonth == requiredMonth) {
			
			//ensure the mob is killed by a player
			if(!(entity.getKiller() == null)) {
		
				//check for entity type (aka. creeper, glowsquid, etc)
				if(entity.getType() == entityType) {
					
					//get player ign
					String IGN = entity.getKiller().getName();
					
					//get playerdata from playerdata.yml
					ConfigurationSection playerData = playerDataConfig.getConfigurationSection(IGN);
					
					if(playerData != null) {				
						//Get stats section
						ConfigurationSection statsSection = playerData.getConfigurationSection("stats");
						
						//Get kill count for respective mob
						int counter = statsSection.getInt(configKey);	
						int counterTotalPoints = statsSection.getInt("total-points");
						
						//add one to count
						counter += points;
						counterTotalPoints += points;
						
						//set the value in config
						statsSection.set(configKey, counter);
						statsSection.set("total-points", counterTotalPoints);
						
						//might need to reload/save less
						gttp1.getTeamPoints(playerDataConfig);
						mainClass.savePlayerDataFile();			
					} else {
						notInConfig(p, mainClass, playerDataConfig, configKey, points);
					}
				}
			}
		}
	}
	
	/*
	 * BLOCK LISTENER (CHECK IF BLOCK IS NATURALLY OCCURING)
	 */
	
	//block place template
	public void blockPlaced(BlockPlaceEvent event, int currentDay, int currentMonth, 
			int requiredDay, int requiredMonth, Material material, Material material2, FileConfiguration playerDataConfig, 
			String configKey,  Main mainClass, int points) {
		
		Block b = (Block) event.getBlock();
		Player p = event.getPlayer();
		
		//if hour is between 12am-3:59am keep same day
		if(hour <= 3) {
			currentDay =- 1;
		}
		
		if(currentDay == requiredDay && currentMonth == requiredMonth) {
			
			//check for block type (aka. emerald_ore, diamond ore, etc)
			if(b.getType() == material || b.getType() == material2) {
				
				//get player ign
				String IGN = event.getPlayer().getName();
				
				//get playerdata from playerdata.yml
				ConfigurationSection playerData = playerDataConfig.getConfigurationSection(IGN);
				
				if(playerData != null) {				
					//Get stats section
					ConfigurationSection statsSection = playerData.getConfigurationSection("stats");
					
					//Get kill count for respective mob
					int counter = statsSection.getInt(configKey);	
					int counterTotalPoints = statsSection.getInt("total-points");
					
					//add one to count
					counter -= points;
					counterTotalPoints -= points;
					
					//set the value in config
					statsSection.set(configKey, counter);
					statsSection.set("total-points", counterTotalPoints);
					
					//might need to reload/save less
					gttp1.getTeamPoints(playerDataConfig);
					mainClass.savePlayerDataFile();			
				} else {
					notInConfig(p, mainClass, playerDataConfig, configKey, points);
				}
			}
		}
	}
	
	//block break template
	public void blockBreakCounter(BlockBreakEvent event, int currentDay, int currentMonth, 
			int requiredDay, int requiredMonth, Material material, Material material2, FileConfiguration playerDataConfig, 
			String configKey, Main mainClass, int points) {
		Block b = (Block) event.getBlock();
		Player p = event.getPlayer();
		
		//if hour is between 12am-3:59am keep same day
		if(hour <= 3) {
			currentDay =- 1;
		}
		
		if(currentDay == requiredDay && currentMonth == requiredMonth) {
			
			//check for block type (aka. emerald_ore, diamond ore, etc)
			if(b.getType() == material || b.getType() == material2) {
				
				//get player ign
				String IGN = event.getPlayer().getName();
				
				//get playerdata from playerdata.yml
				ConfigurationSection playerData = playerDataConfig.getConfigurationSection(IGN);
				
				if(playerData != null) {				
					//Get stats section
					ConfigurationSection statsSection = playerData.getConfigurationSection("stats");
					
					//Get kill count for respective mob
					int counter = statsSection.getInt(configKey);	
					int counterTotalPoints = statsSection.getInt("total-points");
					
					//add one to count
					counter += points;
					counterTotalPoints += points;
					
					//set the value in config
					statsSection.set(configKey, counter);
					statsSection.set("total-points", counterTotalPoints);
					
					//might need to reload/save less
					gttp1.getTeamPoints(playerDataConfig);
					mainClass.savePlayerDataFile();			
				} else {
					notInConfig(p, mainClass, playerDataConfig, configKey, points);
				}
			}
		}
	}
	
	//fish caught counter template
	public void fishCaughtCounter(PlayerFishEvent event, int currentDay, int currentMonth, 
			int requiredDay, int requiredMonth, FileConfiguration playerDataConfig, 
			String configKey, Main mainClass, int points) {
		Player p = event.getPlayer();
		
		//if hour is between 12am-3:59am keep same day
		if(hour <= 3) {
			currentDay =- 1;
		}
		
		if(currentDay == requiredDay && currentMonth == requiredMonth) {
			
			//check for block type (aka. emerald_ore, diamond ore, etc)
			if(event.getState() == State.CAUGHT_FISH) {
				
				//get player ign
				String IGN = event.getPlayer().getName();
				
				//get playerdata from playerdata.yml
				ConfigurationSection playerData = playerDataConfig.getConfigurationSection(IGN);
				
				if(playerData != null) {				
					//Get stats section
					ConfigurationSection statsSection = playerData.getConfigurationSection("stats");
					
					//Get kill count for respective mob
					int counter = statsSection.getInt(configKey);		
					
					//add one to count
					counter += 1;
					
					//set the value in config
					statsSection.set(configKey, counter);

					gttp1.getTeamPoints(playerDataConfig);
					mainClass.savePlayerDataFile();			
				} else {
					notInConfig(p, mainClass, playerDataConfig, configKey, points);
				}
			}
		}
	}
}