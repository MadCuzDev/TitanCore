package me.madcuzdev.titancore.listeners;

import me.madcuzdev.titancore.ConfigHandler;
import me.madcuzdev.titancore.EnchantHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class TokenListener implements Listener {

    private Random random = new Random();
    private HashMap<Player, Double> tokenGain = new HashMap<>();
    private Timer timer = new Timer();

    private FileConfiguration tokenConfig = ConfigHandler.getTokenConfig();
    private FileConfiguration prestigeConfig = ConfigHandler.getPrestigesConfig();

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
        double tokens = (prestigeConfig.getDouble(uuid.toString()) / 10) + 1;
        if (!tokenGain.containsKey(player)) {
            tokenGain.put(player, tokens);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    String uuid = player.getUniqueId().toString();
                    tokenConfig.set(uuid, tokenConfig.getDouble(uuid) + tokenGain.get(player) * ((player.getItemInHand() != null && random.nextInt(500) < player.getItemInHand().getEnchantmentLevel(EnchantHandler.Casino)) ? 2 : 1));
                    tokenGain.remove(player);
                    ConfigHandler.reloadtokenConfig();
                    if (player.getItemInHand().getType() == Material.DIAMOND_PICKAXE)
                        player.getItemInHand().setDurability((short) 0);
                }
            }, 2000);
        } else {
            tokenGain.put(player, tokenGain.get(player) + tokens);
        }
    }

}
