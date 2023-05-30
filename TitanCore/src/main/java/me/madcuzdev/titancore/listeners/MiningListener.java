package me.madcuzdev.titancore.listeners;

import java.util.*;

import com.sk89q.worldedit.BlockVector;
import me.madcuzdev.titancore.ConfigHandler;
import me.madcuzdev.titancore.commands.PickCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.manager.UserManager;
import me.madcuzdev.titancore.EnchantHandler;
import me.madcuzdev.titancore.PriceHandler;
import me.madcuzdev.titancore.VaultHandler;

public class MiningListener implements Listener {
    private PriceHandler priceHandler = new PriceHandler();
    private UserManager userManager = LuckPerms.getApi().getUserManager();
    private PickCommand pickCommand = new PickCommand();

    private Random random = new Random();
    private Timer timer = new Timer();

    private FileConfiguration tokenConfig = ConfigHandler.getTokenConfig();
    private FileConfiguration prestigeConfig = ConfigHandler.getPrestigesConfig();

    private HashMap<Player, Double> blocksMined = new HashMap<>();

    static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");

        if (!(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    static ArrayList<Block> validBlocks = new ArrayList<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!Bukkit.getOfflinePlayer(uuid).hasPlayedBefore()) {
            pickCommand.givePickaxe(player);
            ConfigHandler.getTokenConfig().set(uuid.toString(), 1000);
            ConfigHandler.getPrestigesConfig().set(uuid.toString(), 0);
            ConfigHandler.reloadTokenConfig();
            ConfigHandler.reloadPrestigesConfig();
        }
    }

    public void scheduleBlocks() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                HashMap<Player, Double> loopMap = new HashMap<>(blocksMined);
                for (Player player : loopMap.keySet()) {
                    String uuid = player.getUniqueId().toString();
                    double money = player.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS) ? 3 * player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) : 3;
                    money = loopMap.get(player) * money * priceHandler.getSellPrices().get(Objects.requireNonNull(userManager.getUser(player.getUniqueId())).getPrimaryGroup());
                    if (ConfigHandler.getMasteryConfig().contains(uuid) && ConfigHandler.getMasteryConfig().getInt(uuid) > 0) money = money * (ConfigHandler.getMasteryConfig().getInt(uuid) + 1);

                    VaultHandler.getEcon().depositPlayer(player, money * ((player.getItemInHand() != null && random.nextInt(500) < player.getItemInHand().getEnchantmentLevel(EnchantHandler.MoneyBags)) ? 2 : 1));

                    double tokens = (prestigeConfig.getDouble(uuid) / 10) + 1;
                    tokens = tokens * loopMap.get(player);

                    tokenConfig.set(uuid, tokenConfig.getDouble(uuid) + tokens * ((player.getItemInHand() != null && random.nextInt(500) < player.getItemInHand().getEnchantmentLevel(EnchantHandler.Casino)) ? 2 : 1));
                }

                if (blocksMined.size() > 0) ConfigHandler.reloadTokenConfig();
                blocksMined.clear();
            }
        }, 0, 2000);
    }

    public static BlockVector[] getRegionPoints(BlockBreakEvent event) {
        Block block = event.getBlock();
        for (ProtectedRegion protectedRegion : Objects.requireNonNull(getWorldGuard()).getRegionManager(block.getWorld()).getApplicableRegions(block.getLocation())) {
            if (protectedRegion.getId().toLowerCase().contains("mine")) {
                return new BlockVector[]{protectedRegion.getMinimumPoint(), protectedRegion.getMaximumPoint()};
            }
        }
        return null;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        for (ProtectedRegion protectedRegion : Objects.requireNonNull(getWorldGuard()).getRegionManager(block.getWorld()).getApplicableRegions(block.getLocation())) {
            if (protectedRegion.getId().toLowerCase().contains("mine")) {
                blocksMined.put(player, blocksMined.containsKey(player) ? blocksMined.get(player) + 1 : 1);
                block.setType(Material.AIR);
                if (player.getItemInHand().containsEnchantment(EnchantHandler.Scavenger)) {
                    int enchantLevel = player.getInventory().getItemInHand().getEnchantmentLevel(EnchantHandler.Scavenger);
                    if (enchantLevel >= random.nextInt(250000)) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crate give to " + player.getName() + " mining 1");
                }
            }
        }
    }

}
