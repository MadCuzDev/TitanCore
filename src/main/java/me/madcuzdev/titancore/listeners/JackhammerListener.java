package me.madcuzdev.titancore.listeners;

import com.sk89q.worldedit.BlockVector;
import me.madcuzdev.titancore.EnchantHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JackhammerListener implements Listener {
    private Random random = new Random();

    @EventHandler
    public void breakerEnchantBreak(BlockBreakEvent event) {
        World world = event.getPlayer().getWorld();
        Location loc = event.getBlock().getLocation();
        Block block = world.getBlockAt(loc);
        if (!MiningListener.validBlocks.contains(block)) {
            Player player = event.getPlayer();
            if (player.getItemInHand().containsEnchantment(EnchantHandler.Jackhammer)) {
                int enchantLevel = player.getInventory().getItemInHand().getEnchantmentLevel(EnchantHandler.Jackhammer);
                double chance = (double) enchantLevel /250000;
                boolean activation = chance >= random.nextDouble();
                if (activation) {
                    BlockVector[] blockVectors = MiningListener.getRegionPoints(event);

                    int squareSize = (int) Math.min(Math.abs(blockVectors[0].getX() - blockVectors[1].getX()), Math.abs(blockVectors[0].getZ() - blockVectors[1].getZ()));

                    int startX = (int) Math.min(blockVectors[0].getX(), blockVectors[1].getX());
                    int startZ = (int) Math.min(blockVectors[0].getZ(), blockVectors[1].getZ());
                    int maxX = (int) Math.max(blockVectors[0].getX(), blockVectors[1].getX());
                    int maxZ = (int) Math.max(blockVectors[0].getZ(), blockVectors[1].getZ());

                    List<Location> points = new ArrayList<>();

                    for (int i = startX; i <= maxX; i++) {
                        for (int j = startZ; j <= maxZ; j++) {
                            points.add(new Location(event.getBlock().getWorld(), i, event.getBlock().getY(), j));
                        }
                    }

                    for (Location location : points) {
                        breakBlock(player, location);
                    }
                    MiningListener.validBlocks.clear();
                }
            }
        }
    }

    static void breakBlock(Player player, Location lc) {
        if (MiningListener.getWorldGuard() == null || MiningListener.getWorldGuard().canBuild(player, lc)) {
            MiningListener.validBlocks.add(lc.getBlock());
            Bukkit.getPluginManager().callEvent(new BlockBreakEvent(lc.getBlock(), player));
        }
    }
}
