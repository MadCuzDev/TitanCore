package me.madcuzdev.titancore.listeners;

import me.madcuzdev.titancore.EnchantHandler;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class QuakeListener implements Listener {

    @EventHandler
    public void breakerEnchantBreak(BlockBreakEvent event) {
        World world = event.getPlayer().getWorld();
        Location loc = event.getBlock().getLocation();
        Block block = world.getBlockAt(loc);
        if (!MiningListener.validBlocks.contains(block)) {
            Player player = event.getPlayer();
            if (player.getItemInHand().containsEnchantment(EnchantHandler.Quake)) {
                int enchantLevel = player.getInventory().getItemInHand().getEnchantmentLevel(EnchantHandler.Quake);
                for (double y = block.getLocation().getY() - (double) enchantLevel; y <= block.getLocation().getY() + (double) enchantLevel; y++) {
                    for (double z = block.getLocation().getZ() - (double) enchantLevel; z <= block.getLocation().getZ() + (double) enchantLevel; z++) {
                        Location lc = new Location(world, loc.getX(), y, z);
                        destroyBlock(player, lc);
                    }
                }
                for (double y = block.getLocation().getY() - (double) enchantLevel; y <= block.getLocation().getY() + (double) enchantLevel; y++) {
                    for (double x = block.getLocation().getX() - (double) enchantLevel; x <= block.getLocation().getX() + (double) enchantLevel; x++) {
                        Location lc = new Location(world, x, y, loc.getZ());
                        destroyBlock(player, lc);
                    }
                }
                MiningListener.validBlocks.clear();
            }
        }
    }

    private void destroyBlock(Player player, Location lc) {
        SphericListener.breakBlock(player, lc);
    }
}
