package me.madcuzdev.titancore.commands;

import me.madcuzdev.titancore.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MasterySetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("titancore.masteryset")) {
            if (args.length > 1) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                String uuid = offlinePlayer.getUniqueId().toString();
                if (ConfigHandler.getMasteryConfig().contains(uuid)) {
                    try {
                        int mastery = Integer.parseInt(args[1]);
                        ConfigHandler.getMasteryConfig().set(uuid, mastery);
                        ConfigHandler.reloadMasteryConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l" + offlinePlayer.getName() + "'s mastery set to " + mastery));
                    } catch (NumberFormatException exc) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Please specify a number between 1 - 2,147,483,647"));
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Could not find " + offlinePlayer.getName() + " in storage"));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lFormat: /masteryset (player) (mastery)"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You do not have permission to execute this command"));
        }
        return true;
    }
}
