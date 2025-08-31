package me.madcuzdev.titancore.listeners;

import me.madcuzdev.titancore.EnchantHandler;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Random;

public class SphericListener implements Listener {
    private Random rand = new Random();

    @EventHandler
    public void breakerEnchantBreak(BlockBreakEvent event) {
        World world = event.getPlayer().getWorld();
        Location loc = event.getBlock().getLocation();
        Block block = world.getBlockAt(loc);
        if (!MiningListener.validBlocks.contains(block)) {
            Player player = event.getPlayer();
            if (player.getItemInHand().containsEnchantment(EnchantHandler.Spheric)) {
                int enchantLevel = player.getInventory().getItemInHand().getEnchantmentLevel(EnchantHandler.Spheric);
                int num = rand.nextInt(5250) + 1;
                if (enchantLevel + 250 >= num) {
                    double radius = 2 + enchantLevel / 1500;
                    for (double x = block.getLocation().getX() - radius; x <= block.getLocation().getX() + radius; x++) {
                        for (double y = block.getLocation().getY() - radius; y <= block.getLocation().getY() + radius; y++) {
                            for (double z = block.getLocation().getZ() - radius; z <= block.getLocation().getZ() + radius; z++) {
                                Location lc = new Location(world, x, y, z);
                                if (lc.distance(loc) < 2 + enchantLevel / 1500) {
                                    breakBlock(player, lc);
                                }
                            }
                        }
                    }
                    MiningListener.validBlocks.clear();
                }
            }
        }
    }

    static void breakBlock(Player player, Location lc) {
        CutterListener.breakBlock(player, lc);
    }
}
