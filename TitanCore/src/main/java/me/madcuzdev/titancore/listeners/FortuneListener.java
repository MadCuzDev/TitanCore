package me.madcuzdev.titancore.listeners;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.manager.UserManager;
import me.madcuzdev.titancore.PriceHandler;
import me.madcuzdev.titancore.VaultHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class FortuneListener implements Listener {
    private PriceHandler priceHandler = new PriceHandler();
    private UserManager userManager = LuckPerms.getApi().getUserManager();

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
                int blocks = 3;
                if (player.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                    blocks = blocks * player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                }
                VaultHandler.getEcon().depositPlayer(player, blocks * priceHandler.getSellPrices().get(Objects.requireNonNull(userManager.getUser(player.getName())).getPrimaryGroup()));
            }
        }
    }

}
