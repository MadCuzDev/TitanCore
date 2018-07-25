package me.madcuzdev.titancore.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.manager.UserManager;
import me.madcuzdev.titancore.EnchantHandler;
import me.madcuzdev.titancore.PriceHandler;
import me.madcuzdev.titancore.VaultHandler;

public class FortuneListener implements Listener {
    private PriceHandler priceHandler = new PriceHandler();
    private UserManager userManager = LuckPerms.getApi().getUserManager();
    private Random random = new Random();

    private HashMap<Player, Double> moneyGain = new HashMap<>();
    private Timer timer = new Timer();

    static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");

        if (!(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    static ArrayList<Block> validBlocks = new ArrayList<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        for (ProtectedRegion protectedRegion : Objects.requireNonNull(getWorldGuard()).getRegionManager(block.getWorld()).getApplicableRegions(block.getLocation())) {
            if (protectedRegion.getId().toLowerCase().contains("mine")) {
                block.setType(Material.AIR);
                double blocks = player.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS) ? 3 * player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) : 3;
                blocks = blocks * priceHandler.getSellPrices().get(Objects.requireNonNull(userManager.getUser(player.getUniqueId())).getPrimaryGroup());
                if (!moneyGain.containsKey(player)) {
                    moneyGain.put(player, blocks);
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            VaultHandler.getEcon().depositPlayer(player, moneyGain.get(player) * ((player.getItemInHand()!=null&&random.nextInt(500)<player.getItemInHand().getEnchantmentLevel(EnchantHandler.Yes))?2:1));
                            moneyGain.remove(player);
                            if (player.getItemInHand().getType() == Material.DIAMOND_PICKAXE) player.getItemInHand().setDurability((short) 0);
                        }
                    }, 2000);
                } else {
                    moneyGain.put(player, moneyGain.get(player) + blocks);
                }
            }
        }
    }

}
