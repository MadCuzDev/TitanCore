package me.madcuzdev.titancore.commands;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import me.madcuzdev.titancore.PriceHandler;
import me.madcuzdev.titancore.VaultHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class RankupCommand implements CommandExecutor {
    private LuckPermsApi luckPermsApi = LuckPerms.getApi();
    private PriceHandler priceHandler = new PriceHandler();

    private ArrayList<Player> cooldownList = new ArrayList<>();

    private Timer timer = new Timer();

    void setRankup(User user, String primaryGroup, String nextRank) {
        Node prevGroupNode = luckPermsApi.getNodeFactory().makeGroupNode(primaryGroup).build();
        Node nextGroupNode = luckPermsApi.getNodeFactory().makeGroupNode(nextRank.toLowerCase()).build();
        user.setPermission(nextGroupNode);
        if (!primaryGroup.equalsIgnoreCase("default")) {
            user.unsetPermission(prevGroupNode);
        }
        user.setPrimaryGroup(nextRank.toLowerCase());
        luckPermsApi.getUserManager().saveUser(user);
    }

    public String getNextRank(String primaryGroup) {
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

    String getPreviousRank(String primaryGroup) {
        String nextRank;
        if (primaryGroup.equalsIgnoreCase("default")) {
            nextRank = "default";
        } else {
            char character = primaryGroup.charAt(0);
            character--;
            nextRank = String.valueOf(character);
        }
        return nextRank;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!cooldownList.contains(player)) {
                User user = luckPermsApi.getUserManager().getUser(sender.getName());
                assert user != null;
                String primaryGroup = user.getPrimaryGroup();
                String nextRank;
                 if (primaryGroup.equalsIgnoreCase("z")) {
                     sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lSorry, there is no rank above you. Please do /prestige to be reset to A rank and increase your prestige"));
                     return true;
                 } else {
                     nextRank = getNextRank(primaryGroup);
                 }

                if (VaultHandler.getEcon().getBalance(player) > priceHandler.getRankPrices().get(nextRank)) {
                    setRankup(user, primaryGroup, nextRank);

                    VaultHandler.getEcon().withdrawPlayer(player, priceHandler.getRankPrices().get(nextRank));

                    cooldownList.add(player);
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            cooldownList.remove(player);
                        }
                    }, 2000);

                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSuccess! You've now been ranked up to " +
                            nextRank.toUpperCase() + " for $" + PriceHandler.formatNumber((double) priceHandler.getRankPrices().get(nextRank))));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You must have at least $"
                            + PriceHandler.formatNumber((double) priceHandler.getRankPrices().get(nextRank))
                            + " to rank up to " + nextRank.toUpperCase()));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You must wait 2 seconds between executing this command. Type /maxrankup to rankup faster"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Must be executed as a Player"));
        }
        return true;
    }
}
