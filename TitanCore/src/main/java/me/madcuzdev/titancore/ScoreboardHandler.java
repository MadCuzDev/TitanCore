package me.madcuzdev.titancore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.manager.UserManager;
import net.md_5.bungee.api.ChatColor;

public class ScoreboardHandler {

	private TitanCore core;
	private UserManager userManager = LuckPerms.getApi().getUserManager();
    private PriceHandler priceHandler = new PriceHandler();

	
	public ScoreboardHandler(TitanCore core){
		this.core=core;
	}
	
	public void startScoreboardLoop(){
		new BukkitRunnable(){
			@Override
			public void run(){
				for(Player player : core.getServer().getOnlinePlayers()){
					showScoreboard(player);
				}
			}
		}.runTaskTimer(core, 400, 40);
	}
	
	public void showScoreboard(Player player){
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = scoreboard.registerNewObjective("title", "dummy");
		objective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Titan" + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Prison");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		String playerRank = userManager.getUser(player.getName()).getPrimaryGroup();
		
		Score rank = objective.getScore(ChatColor.GOLD + "Rank: " + ChatColor.GRAY + (playerRank.equalsIgnoreCase("default")?"A":playerRank));
		rank.setScore(7);
		Score prestige = objective.getScore(ChatColor.GOLD + "Prestige: " + ChatColor.GRAY + ConfigHandler.getPrestigesConfig().getInt(player.getUniqueId().toString()));
		prestige.setScore(6);
		
		long price = 1;
		if(playerRank.equalsIgnoreCase("z")){
	        int nextPrestige = 1;
	        if (ConfigHandler.getPrestigesConfig().contains(player.getUniqueId().toString())) {
	            nextPrestige += ConfigHandler.getPrestigesConfig().getInt(player.getUniqueId().toString());
	        }
	        price = priceHandler.getPrestigePrice(nextPrestige);
		}else{
			String nextRank = getNextRank(playerRank);
			price = priceHandler.getRankPrices().get(nextRank);
		}
		double fraction = VaultHandler.getEcon().getBalance(player)/((double)price);
		fraction = Math.min(Math.floor(fraction*1000)/10,100);
		
		Score progress = objective.getScore(ChatColor.GOLD + "Progress: " + ChatColor.GRAY + fraction + "%");
		progress.setScore(5);
		
		Score blank1 = objective.getScore(" ");
		blank1.setScore(4);
		
		Score balance = objective.getScore(ChatColor.GOLD + "Balance: " + ChatColor.GRAY + shorten(VaultHandler.getEcon().getBalance(player)));
		balance.setScore(3);
		
		Score tokens = objective.getScore(ChatColor.GOLD + "Tokens: " + ChatColor.GRAY + ConfigHandler.getTokenConfig().getDouble(player.getUniqueId().toString()));
		tokens.setScore(2);
		
		Score blank2 = objective.getScore("  ");
		blank2.setScore(1);
		
		Score date = objective.getScore(ChatColor.GRAY + (new SimpleDateFormat("mm/dd/yy")).format(new Date()));
		date.setScore(0);
				
		player.setScoreboard(scoreboard);
	}
	
	public String shorten(double num){
		String[] nums = new String[]{"", "K", "M", "B", "T", "Qd", "Qt", "Sx", "Sp", "O", "N", "D"};
		int i = (int)(Math.log10(Math.max(num,1.0))/3.0);
		return Math.floor(num/Math.pow(1000.0, (double)i)*10)/10 + nums[i];
	}
	
	String getNextRank(String primaryGroup) {
        String nextRank;
        if (primaryGroup.equalsIgnoreCase("default")) {
            nextRank = "b";
        } else {
            char character = primaryGroup.charAt(0);
            character++;
            nextRank = String.valueOf(character);
        }
        return nextRank;
    }
	
}
