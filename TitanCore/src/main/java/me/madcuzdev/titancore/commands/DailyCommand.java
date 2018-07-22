package me.madcuzdev.titancore.commands;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LocalizedNode;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.manager.UserManager;
import me.madcuzdev.titancore.ConfigHandler;
import me.madcuzdev.titancore.PriceHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class DailyCommand implements CommandExecutor {
    private FileConfiguration cooldownConfig = ConfigHandler.getCooldownConfig();
    private FileConfiguration tokenConfig = ConfigHandler.getTokenConfig();

    private LuckPermsApi luckPermsApi = LuckPerms.getApi();
    private UserManager userManager = luckPermsApi.getUserManager();

    private HashMap<String, Integer> tokens;

    {
        tokens = new HashMap<>();
        tokens.put("donor", 10000);
        tokens.put("lord", 50000);
        tokens.put("king", 100000);
        tokens.put("god", 200000);
        tokens.put("titan", 400000);
    }

    private HashMap<String, Integer> renames;

    {
        renames = new HashMap<>();
        renames.put("donor", 1);
        renames.put("lord", 2);
        renames.put("king", 3);
        renames.put("god", 4);
        renames.put("titan", 5);
    }

    private void giveAllToken(String rank) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String uuid = player.getUniqueId().toString();
            tokenConfig.set(uuid, tokenConfig.getDouble(uuid) + tokens.get(rank));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            Set<String> groups = userManager.getUser(uuid).getAllNodes().stream()
                    .filter(Node::isGroupNode)
                    .map(Node::getGroupName)
                    .collect(Collectors.toSet());
            String groupName = "donor";
            for (String forGroupName : groups) {
                if (tokens.containsKey(forGroupName)) {
                    if (renames.get(forGroupName) > renames.get(groupName)) {
                        groupName = forGroupName.toLowerCase();
                    }
                }
            }
            if (groupName.equalsIgnoreCase("")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You need a donor rank to execute this command"));
                return true;
            }
            System.out.print(groupName);
            if (args.length > 0) {
                long currentTime = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());
                switch (args[0]) {
                    case "token":
                        if (cooldownConfig.contains("token." + uuid) || player.hasPermission("titancore.dailybypass")) {
                            long resetTime = TimeUnit.MILLISECONDS.toHours(cooldownConfig.getLong("token." + uuid)) + 24;
                            if (resetTime <= currentTime || player.hasPermission("titancore.dailybypass")) {
                                giveAllToken(groupName);
                                cooldownConfig.set("token." + uuid, System.currentTimeMillis());
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6&l" + player.getName() + " has given everyone " +
                                        PriceHandler.formatNumber((double) tokens.get(groupName)) + " tokens"));
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Must wait another " + Math.subtractExact(resetTime, currentTime) + " hours"));
                            }
                        } else {
                            giveAllToken(groupName);
                            cooldownConfig.set("token." + uuid, System.currentTimeMillis());
                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6&l" + player.getName() + " has given everyone " +
                                    PriceHandler.formatNumber((double) tokens.get(groupName)) + " tokens"));
                        }
                        break;
                    case "rename":
                        if (cooldownConfig.contains("rename." + uuid) || player.hasPermission("titancore.dailybypass")) {
                            long resetTime = TimeUnit.MILLISECONDS.toHours(cooldownConfig.getLong("rename." + uuid)) + 24;
                            if (resetTime <= currentTime || player.hasPermission("titancore.dailybypass")) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rename give " + player.getName() + " " + renames.get(groupName));
                                cooldownConfig.set("rename." + uuid, System.currentTimeMillis());
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Must wait another " + Math.subtractExact(resetTime, currentTime) + " hours"));
                            }
                        } else {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rename give " + player.getName() + " " + renames.get(groupName));
                            cooldownConfig.set("rename." + uuid, System.currentTimeMillis());
                        }
                        break;
                    default:
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lValid rewards: token, rename"));
                        return true;
                }
                ConfigHandler.reloadCooldownConfig();
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lFormat: /daily (reward). Valid rewards: token, rename"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Must be executed as a Player"));
        }
        return true;
    }
}
