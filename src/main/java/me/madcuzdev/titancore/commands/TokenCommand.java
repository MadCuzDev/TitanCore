package me.madcuzdev.titancore.commands;

import me.madcuzdev.titancore.ConfigHandler;
import me.madcuzdev.titancore.PriceHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class TokenCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (ConfigHandler.getTokenConfig().contains(player.getUniqueId().toString())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lYou have " + PriceHandler.formatNumber((Double) ConfigHandler.getTokenConfig().get(player.getUniqueId().toString())) + " tokens"));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lYou have 0 tokens"));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Must be executed as a Player"));
            }
        } else {

            switch (args[0]) {
                case "add":
                    if (sender.hasPermission("titancore.token")) {
                        if (args.length > 2) {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                            if (offlinePlayer != null) {
                                double tokens = Double.parseDouble(args[2]);
                                String uuid = offlinePlayer.getUniqueId().toString();
                                ConfigHandler.getTokenConfig().set(uuid, ConfigHandler.getTokenConfig().getDouble(uuid) + tokens);
                                ConfigHandler.reloadTokenConfig();

                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l" + offlinePlayer.getName() + " has been given " + PriceHandler.formatNumber(tokens) + " tokens"));
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Could not find '" + args[1] + "'"));
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lFormat: /te add (player) (amount)"));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You do not have permission to execute this command"));
                    }
                    break;
                case "set":
                    if (sender.hasPermission("titancore.token")) {
                        if (args.length > 2) {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                            if (offlinePlayer != null) {
                                double tokens = Double.parseDouble(args[2]);
                                ConfigHandler.getTokenConfig().set(offlinePlayer.getUniqueId().toString(), tokens);
                                ConfigHandler.reloadTokenConfig();

                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l" + offlinePlayer.getName() + "'s token balance has been set to " + PriceHandler.formatNumber(tokens)));
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Could not find '" + args[1] + "'"));
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lFormat: /te set (player) (amount)"));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You do not have permission to execute this command"));
                    }
                    break;
                case "pay":
                    if (args.length > 2) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            try {
                                if (sender instanceof Player) {
                                    Player player = (Player) sender;
                                    double tokens = Double.parseDouble(args[2]);
                                    if (tokens > 0) {
                                        if (ConfigHandler.getTokenConfig().getDouble(player.getUniqueId().toString()) >= tokens) {
                                            ConfigHandler.getTokenConfig().set(target.getUniqueId().toString(), tokens + ConfigHandler.getTokenConfig().getDouble(target.getUniqueId().toString()));
                                            ConfigHandler.getTokenConfig().set(player.getUniqueId().toString(), ConfigHandler.getTokenConfig().getDouble(player.getUniqueId().toString()) - tokens);
                                            ConfigHandler.reloadTokenConfig();

                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lYou paid " + target.getName() + " " + PriceHandler.formatNumber(tokens) + " tokens"));
                                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lYou received " + PriceHandler.formatNumber(tokens) + " tokens from " + player.getName()));
                                        } else {
                                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You do not have enough tokens"));
                                        }
                                    } else {
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You must send at least 1 token"));
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You must be a player to run this command"));
                                }
                            } catch (NumberFormatException exc) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: '" + args [2]) + "' is not a valid number");
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Could not find '" + args[1] + "'"));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lFormat: /te set (player) (amount)"));
                    }
                    break;
                case "help":
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lValid commands:\n/te set (player) (amount)\n/te add (player) (amount)\n/te (player)\n/te pay (username) (amount)"));
                    break;
                default:
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    String uuid = offlinePlayer.getUniqueId().toString();
                    if (ConfigHandler.getTokenConfig().contains(uuid)) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l" + offlinePlayer.getName() + "'s token balance is " + PriceHandler.formatNumber(ConfigHandler.getTokenConfig().getDouble(uuid))));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Could not find '" + args[0] + "' in storage"));
                    }
                    break;
            }
        }
        return true;
    }
}
