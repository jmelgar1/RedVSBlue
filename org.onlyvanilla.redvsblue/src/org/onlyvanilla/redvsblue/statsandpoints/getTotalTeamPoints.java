package org.onlyvanilla.redvsblue.statsandpoints;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.onlyvanilla.redvsblue.Main;

public class getTotalTeamPoints implements Listener {
	
	private Main mainClass = Main.getInstance();
	
	public void getTeamPoints(FileConfiguration playerDataConfig) {
    	
		int redTeamPoints = 0;
		int blueTeamPoints = 0;
		
		//get teamdata configuration section from playerdata.yml
		ConfigurationSection teamData = playerDataConfig.getConfigurationSection("TeamPoints");	

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
			
			playerPoints = statsSection.getInt("total-points");
			
			if(playerData.getString("team").equals("red")) {
				//on red team
				redTeamPoints += playerPoints;
			} else if(playerData.getString("team").equals("blue")) {
				//on blue team
				blueTeamPoints += playerPoints;
			} else {
				//not on a team
				playerPoints = 0;
			}					
			
			teamData.set("red-team", redTeamPoints);
			teamData.set("blue-team", blueTeamPoints);
			mainClass.savePlayerDataFile();
		}
	}
}
