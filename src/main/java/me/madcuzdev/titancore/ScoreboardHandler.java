package me.madcuzdev.titancore;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.manager.UserManager;
import me.madcuzdev.titancore.commands.RankupCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

class ScoreboardHandler {

	private TitanCore core;
	private UserManager userManager = LuckPerms.getApi().getUserManager();
	private PriceHandler priceHandler = new PriceHandler();

	private RankupCommand rankupCommand = new RankupCommand();


	ScoreboardHandler(TitanCore core) {
		this.core = core;
	}

	void startScoreboardLoop() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : core.getServer().getOnlinePlayers()) {
					showScoreboard(player);
				}
			}
		}.runTaskTimer(core, 200, 40);
	}

	private String getEntryFromScore(Objective objective, int score) {
		if(objective == null) return null;
		if(!hasScoreTaken(objective, score)) return null;
		for (String s : objective.getScoreboard().getEntries()) {
			if(objective.getScore(s).getScore() == score) return objective.getScore(s).getEntry();
		}
		return null;
	}

	private boolean hasScoreTaken(Objective objective, int score) {
		for (String s : objective.getScoreboard().getEntries()) {
			if(objective.getScore(s).getScore() == score) return true;
		}
		return false;
	}

	private void replaceScore(Objective objective, int score, String name) {
		if(hasScoreTaken(objective, score)) {
			if(getEntryFromScore(objective, score).equalsIgnoreCase(name)) return;
			if(!(getEntryFromScore(objective, score).equalsIgnoreCase(name))) objective.getScoreboard().resetScores(getEntryFromScore(objective, score));
		}
		objective.getScore(name).setScore(score);
	}

	private double getRankPercent(String playerRank, Player player) {
		long price;
		if (playerRank.equalsIgnoreCase("z")) {
			int nextPrestige = 1;
			if (ConfigHandler.getPrestigesConfig().contains(player.getUniqueId().toString())) {
				nextPrestige += ConfigHandler.getPrestigesConfig().getInt(player.getUniqueId().toString());
			}
			price = priceHandler.getPrestigePrice(nextPrestige);
		} else {
			String nextRank = rankupCommand.getNextRank(playerRank);
			price = priceHandler.getRankPrices().get(nextRank);
		}
		double fraction = VaultHandler.getEcon().getBalance(player) / ((double) price);
		return Math.min(Math.floor(fraction * 1000) / 10, 100);
	}

	private void showScoreboard(Player player) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = scoreboard.registerNewObjective("title", "dummy");
		objective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "TitanPrison");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		String playerRank = Objects.requireNonNull(userManager.getUser(player.getName())).getPrimaryGroup();

		replaceScore(objective, 7, ChatColor.GOLD + "Rank: " + ChatColor.GRAY + (playerRank.equalsIgnoreCase("default") ? "A" : playerRank.toUpperCase()));
		replaceScore(objective, 6, ChatColor.GOLD + "Prestige: " + ChatColor.GRAY + ConfigHandler.getPrestigesConfig().getInt(player.getUniqueId().toString()));
		replaceScore(objective, 5, ChatColor.GOLD + "Progress: " + ChatColor.GRAY + getRankPercent(playerRank, player) + "%");
		replaceScore(objective, 4, " ");
		replaceScore(objective, 3, ChatColor.GOLD + "Balance: " + ChatColor.GRAY + shorten(VaultHandler.getEcon().getBalance(player)));
		replaceScore(objective, 2, ChatColor.GOLD + "Tokens: " + ChatColor.GRAY + PriceHandler.formatNumber(ConfigHandler.getTokenConfig().getDouble(player.getUniqueId().toString())));
		replaceScore(objective, 1, " ");
		replaceScore(objective, 0, ChatColor.GRAY + (new SimpleDateFormat("MM/dd/yy")).format(new Date()));

		player.setScoreboard(scoreboard);
	}

	private String shorten(double num) {
		String[] nums = new String[]{"", "K", "M", "B", "T", "Qd", "Qt", "Sx", "Sp", "O", "N", "D"};
		int i = (int) (Math.log10(Math.max(num, 1.0)) / 3.0);
		return Math.floor(num / Math.pow(1000.0, (double) i) * 10) / 10 + nums[i];
	}
}
