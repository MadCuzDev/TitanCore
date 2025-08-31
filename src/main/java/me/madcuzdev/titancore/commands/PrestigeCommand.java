package me.madcuzdev.titancore.commands;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.madcuzdev.titancore.ConfigHandler;
import me.madcuzdev.titancore.PriceHandler;
import me.madcuzdev.titancore.VaultHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static org.bukkit.Bukkit.getServer;


public class PrestigeCommand implements CommandExecutor {
    private PriceHandler priceHandler = new PriceHandler();
    private RankupCommand rankupCommand = new RankupCommand();

    private LuckPermsApi luckPermsApi = LuckPerms.getApi();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            User user = luckPermsApi.getUser(player.getName());
            assert user != null;
            if (user.getPrimaryGroup().equalsIgnoreCase("z")) {
                long prestigePrice;
                int nextPrestige = 1;
                if (ConfigHandler.getPrestigesConfig().contains(player.getUniqueId().toString())) {
                    nextPrestige += ConfigHandler.getPrestigesConfig().getInt(player.getUniqueId().toString());
                }
                prestigePrice = priceHandler.getPrestigePrice(nextPrestige);

                if (VaultHandler.getEcon().getBalance(player) > prestigePrice) {
                    ConfigHandler.getPrestigesConfig().set(player.getUniqueId().toString(), nextPrestige);
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You need " + PriceHandler.formatNumber((double) prestigePrice) + " to prestige"));
                    return true;
                }
                ConfigHandler.reloadPrestigesConfig();

                rankupCommand.setRankup(user, "z", "default");

                Bukkit.dispatchCommand(getServer().getConsoleSender(), "spawn " + player.getName());

                VaultHandler.getEcon().withdrawPlayer(player, prestigePrice);

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crate give to " + player.getName() + " prestige 1");

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSuccess! You've prestiged to " +
                        ConfigHandler.getPrestigesConfig().getInt(player.getUniqueId().toString()) + " for $" + PriceHandler.formatNumber((double) prestigePrice)));
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Must be Z rank to prestige"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Must be executed as a Player"));
        }
        return true;
    }
}
