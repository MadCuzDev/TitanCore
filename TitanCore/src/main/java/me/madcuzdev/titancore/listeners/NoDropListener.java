package me.madcuzdev.titancore.listeners;

import me.madcuzdev.titancore.EnchantHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.*;

public class NoDropListener implements Listener, CommandExecutor {

    private List<UUID> dropTog = new ArrayList<>();

    private Timer timer = new Timer();

    @EventHandler
    public void itemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (event.getItemDrop().getItemStack().containsEnchantment(EnchantHandler.NoDrop) && !dropTog.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lYou cannot drop this item. Type /dropson to bypass NoDrop for 5 seconds"));
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (dropTog.contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lYou already have NoDrop temporarily disabled"));
            } else {
                dropTog.add(player.getUniqueId());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lNoDrop has been bypassed for 5 seconds"));

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        dropTog.remove(player.getUniqueId());
                    }
                }, 5000);
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Must be executed as a Player"));
        }
        return true;
    }
}
