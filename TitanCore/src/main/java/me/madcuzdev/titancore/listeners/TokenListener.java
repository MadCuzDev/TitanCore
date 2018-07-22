package me.madcuzdev.titancore.listeners;

import me.madcuzdev.titancore.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class TokenListener implements Listener {

    private HashMap<Player, Double> tokenGain = new HashMap<>();
    private Timer timer = new Timer();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!Bukkit.getOfflinePlayer(uuid).hasPlayedBefore()) {
            ConfigHandler.getTokenConfig().set(uuid.toString(), 1000);
            ConfigHandler.getPrestigesConfig().set(uuid.toString(), 0);
            ConfigHandler.reloadtokenConfig();
            ConfigHandler.reloadPrestigesConfig();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        double tokens = (ConfigHandler.getPrestigesConfig().getDouble(uuid.toString()) / 5) + 1;
        if (!tokenGain.containsKey(player)) {
            tokenGain.put(player, tokens);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ConfigHandler.getTokenConfig().set(uuid.toString(), ConfigHandler.getTokenConfig().getDouble(uuid.toString()) + tokenGain.get(player));
                    ConfigHandler.reloadtokenConfig();
                    tokenGain.remove(player);
                    if (player.getItemInHand().getType() == Material.DIAMOND_PICKAXE) player.getItemInHand().setDurability((short) 0);
                }
            }, 1000);
        } else {
            tokenGain.put(player, tokenGain.get(player) + tokens);
        }
    }

}
