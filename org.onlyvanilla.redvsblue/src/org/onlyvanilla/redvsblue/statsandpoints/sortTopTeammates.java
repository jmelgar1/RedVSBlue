package org.onlyvanilla.redvsblue.statsandpoints;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

public class sortTopTeammates implements Listener {
	
	//top player hashmap
	HashMap<String, Integer> topPlayers = new HashMap<String, Integer>();
	
	public Map<String, Integer> getPlayers(FileConfiguration playerDataConfig) {
		//get teamdata configuration section from playerdata.yml
		for(String IGN : playerDataConfig.getConfigurationSection("").getKeys(false)) {
			
			//ignore the teampoints section as that is not a player
			if(IGN.equals("TeamPoints")) {
				continue;
			}
			
			int playerPoints = 0;
			
			//get playerdata configuration section from playerdata.yml
			ConfigurationSection playerData = playerDataConfig.getConfigurationSection(IGN);
			
			//get stats configuration section from playerdata.yml
			ConfigurationSection statsSection = playerData.getConfigurationSection("stats");
			
			//retrieve point count from playerdata.yml
			playerPoints = statsSection.getInt("total-points");
			
			//put player ign and point count in hashmap
			topPlayers.put(IGN, playerPoints);
		}
		return topPlayers;
	}
}


