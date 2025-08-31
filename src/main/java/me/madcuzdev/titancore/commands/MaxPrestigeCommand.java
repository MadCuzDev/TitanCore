package me.madcuzdev.titancore.commands;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.madcuzdev.titancore.PriceHandler;
import me.madcuzdev.titancore.VaultHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MaxPrestigeCommand implements CommandExecutor {
    private LuckPermsApi luckPermsApi = LuckPerms.getApi();
    private PriceHandler priceHandler = new PriceHandler();
    private RankupCommand rankupCommand = new RankupCommand();
    private DecimalFormat formatter = new DecimalFormat("###,###,###,###,###,###.##");

    private ArrayList<Player> cooldownList = new ArrayList<>();

    private Timer timer = new Timer();

    private void addToCooldown(Player player) {
        cooldownList.add(player);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                cooldownList.remove(player);
            }
        }, 2000);
    }

    private void rankupPlayer(User user, String primaryGroup, String nextRank, Player player, Long cost) {
        rankupCommand.setRankup(user, primaryGroup, nextRank);

        addToCooldown(player);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSuccess! You've been ranked up to " + nextRank.toUpperCase() + " for $" + formatter.format(cost)));
    }

    /*
    THIS COMMAND IS NOT FUNCTIONAL AND APPEARS TO SIMPLY BE A COPY-PASTE OF MAXRANKUP COMMAND
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            String nextRank;
            Player player = (Player) sender;
            if (!cooldownList.contains(player)) {
                User user = luckPermsApi.getUserManager().getUser(player.getName());
                assert user != null;
                String primaryGroup = user.getPrimaryGroup();

                if (primaryGroup.equalsIgnoreCase("z")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lSorry, there is no rank above you. Please do /prestige to be reset to A rank and increase your prestige"));
                    return true;
                } else {
                    nextRank = rankupCommand.getNextRank(primaryGroup);
                }

                if (priceHandler.getRankPrices().containsKey(nextRank)) {
                    long previousCost = 0;
                    if (VaultHandler.getEcon().getBalance(player) >= priceHandler.getRankPrices().get(nextRank)) {
                        while (priceHandler.getRankPrices().containsKey(nextRank)) {
                            long currentCost = priceHandler.getRankPrices().get(nextRank);
                            if (VaultHandler.getEcon().getBalance(player) - previousCost >= currentCost) {
                                previousCost += currentCost;
                                if (nextRank.equalsIgnoreCase("z")) {
                                    rankupPlayer(user, primaryGroup, nextRank, player, previousCost);

                                    VaultHandler.getEcon().withdrawPlayer(player, previousCost);

                                    return true;
                                }
                                nextRank = rankupCommand.getNextRank(nextRank);
                            } else {
                                String previousRank = rankupCommand.getPreviousRank(nextRank);

                                rankupPlayer(user, primaryGroup, previousRank, player, previousCost);

                                VaultHandler.getEcon().withdrawPlayer(player, previousCost);

                                return true;
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You must have at least $"
                                + formatter.format(priceHandler.getRankPrices().get(nextRank))
                                + " to rank up to " + nextRank.toUpperCase()));
                    }
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You must wait 2 seconds between executing this command"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Must be executed as a Player"));
        }
        return true;
    }
}
