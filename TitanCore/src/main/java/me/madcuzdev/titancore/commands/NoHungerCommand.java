package me.madcuzdev.titancore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class NoHungerCommand implements CommandExecutor {
    private boolean noHungerToggle = true;

    public boolean getNoHungerStatus() {
        return noHungerToggle;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("titancore.nohunger")) {
            noHungerToggle = !noHungerToggle;
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lNoHunger has been set to &b&l" + noHungerToggle));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You do not have permission to execute this command!"));
        }
        return true;
    }
}
