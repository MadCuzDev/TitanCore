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

public class ExplosiveListener implements Listener {
    private Random rand = new Random();

    @EventHandler
    public void breakerEnchantBreak(BlockBreakEvent event) {
        World world = event.getPlayer().getWorld();
        Location loc = event.getBlock().getLocation();
        Block block = world.getBlockAt(loc);
        if (!MiningListener.validBlocks.contains(block)) {
            Player player = event.getPlayer();
            if (player.getItemInHand().containsEnchantment(EnchantHandler.Explosive)) {
                int enchantLevel = player.getInventory().getItemInHand().getEnchantmentLevel(EnchantHandler.Explosive);
                int num = rand.nextInt(5250) + 1;
                if (enchantLevel + 250 >= num) {
                    double radius = enchantLevel / 3000 + 1;
                    for (double x = block.getLocation().getX() - radius; x <= block.getLocation().getX() + radius; x++) {
                        for (double y = block.getLocation().getY() - radius; y <= block.getLocation().getY() + radius; y++) {
                            for (double z = block.getLocation().getZ() - radius; z <= block.getLocation().getZ() + radius; z++) {
                                Location lc = new Location(world, x, y, z);
                                if (MiningListener.getWorldGuard() == null || MiningListener.getWorldGuard().canBuild(player, lc)) {
                                    MiningListener.validBlocks.add(lc.getBlock());
                                    if (rand.nextInt(2) > 0) {
                                        Bukkit.getPluginManager().callEvent(new BlockBreakEvent(lc.getBlock(), player));
                                    }
                                }
                            }
                        }
                    }
                    MiningListener.validBlocks.clear();
                }
            }
        }
    }
}
