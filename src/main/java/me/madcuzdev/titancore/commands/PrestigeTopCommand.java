package me.madcuzdev.titancore.commands;

import me.madcuzdev.titancore.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public class PrestigeTopCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int page = 1;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
                if (page < 0) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "Error: Invalid page number"));
                    return true;
                }
            } catch (NumberFormatException exc) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "Error: Invalid page number"));
            }
        }
        Map<String, Integer> prestiges = new HashMap<>();
        //Adds all the player data
        Set<String> keys = ConfigHandler.getPrestigesConfig().getKeys(false);
        for (String key : keys) prestiges.put(key, (Integer) ConfigHandler.getPrestigesConfig().get(key));
        //TreeMaps can't be sorted by value (only key) and can't access themselves so it contains the data of prestiges and sorts using it
        Map<String, Integer> prestigesSorted = new TreeMap<>((a, b) -> prestiges.get(a) > prestiges.get(b) ? -1 : prestiges.get(b) > prestiges.get(a) ? 1 : a.compareTo(b));
        prestigesSorted.putAll(prestiges);

        //Not necessary just info
        int min = page * 10 - 10;
        int max = 10 * page;
        int rank = 0;
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l--PRESTIGE TOP " + page + "--"));
        for (String uuid : prestigesSorted.keySet()) {
            rank++;
            if (rank > min) {
                UUID actualUUID = UUID.fromString(uuid);
                String name = Bukkit.getServer().getOfflinePlayer(actualUUID).getName();
                int prestige = prestigesSorted.get(uuid);
                //For example
                if (rank > max) break;
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + "#" + rank + ": " + name + " - " + prestige));
            }
        }
        return true;
    }
}
