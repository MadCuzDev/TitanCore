package me.madcuzdev.titancore.listeners;

import me.madcuzdev.titancore.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PrestigeListener implements Listener {

    @EventHandler
    public void addPrestigeToChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        event.setFormat(ChatColor.translateAlternateColorCodes('&', "&8[&c&l" + ConfigHandler.getPrestigesConfig().getInt(player.getUniqueId().toString()) + "&8]&r " + event.getFormat()));
    }

}
