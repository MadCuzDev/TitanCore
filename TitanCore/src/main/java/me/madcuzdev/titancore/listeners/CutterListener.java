package me.madcuzdev.titancore.listeners;

import me.madcuzdev.titancore.EnchantHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Random;
import java.util.Timer;

public class CutterListener implements Listener {

    @EventHandler
    public void breakerEnchantBreak(BlockBreakEvent event) {
        World world = event.getPlayer().getWorld();
        Location loc = event.getBlock().getLocation();
        Block block = world.getBlockAt(loc);
        if (!FortuneListener.validBlocks.contains(block)) {
            Player player = event.getPlayer();
            if (player.getItemInHand().containsEnchantment(EnchantHandler.Cutter)) {
                int enchantLevel = player.getInventory().getItemInHand().getEnchantmentLevel(EnchantHandler.Cutter);
                for (double x = block.getLocation().getX() - (double) enchantLevel; x <= block.getLocation().getX() + (double) enchantLevel; x++) {
                    for (double z = block.getLocation().getZ() - (double) enchantLevel; z <= block.getLocation().getZ() + (double) enchantLevel; z++) {
                        Location lc = new Location(world, x, loc.getY(), z);
                        if (FortuneListener.getWorldGuard() == null || FortuneListener.getWorldGuard().canBuild(player, lc)) {
                            FortuneListener.validBlocks.add(lc.getBlock());
                            Bukkit.getPluginManager().callEvent(new BlockBreakEvent(lc.getBlock(), player));
                        }
                    }
                }
                FortuneListener.validBlocks.clear();
            }
        }
    }
}
