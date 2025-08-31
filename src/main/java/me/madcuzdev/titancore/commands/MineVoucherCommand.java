package me.madcuzdev.titancore.commands;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.manager.UserManager;
import me.madcuzdev.titancore.PriceHandler;
import me.madcuzdev.titancore.VaultHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Objects;

public class MineVoucherCommand implements CommandExecutor, Listener {

    private PriceHandler priceHandler = new PriceHandler();
    private UserManager userManager = LuckPerms.getApi().getUserManager();

    private ItemStack voucher = new ItemStack(Material.PAPER);
    {
        voucher.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 400);
        ItemMeta itemMeta = voucher.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&lMine Blocks Voucher"));
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        voucher.setItemMeta(itemMeta);
    }

    @EventHandler
    public void onClickVoucher(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getType() == Material.PAPER && player.getItemInHand().containsEnchantment(Enchantment.ARROW_INFINITE)) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                boolean success = false;

                for (ItemStack itemStack : player.getInventory().getContents()) {
                    if (itemStack != null && itemStack.getType() == Material.DIAMOND_PICKAXE) {
                        double money = itemStack.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS) ? 3 * itemStack.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) : 3;
                        money = Integer.parseInt(event.getItem().getItemMeta().getLore().toString()
                                .replace("§6Right-click to redeem ", "").replace(" blocks worth", "").replace("[", "").replace("]", "").replace(", §7Factors in current rank and fortune", "")) * money * priceHandler.getSellPrices().get(Objects.requireNonNull(userManager.getUser(player.getUniqueId())).getPrimaryGroup());

                        VaultHandler.getEcon().depositPlayer(player, money);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lYou have redeemed $" + PriceHandler.formatNumber(money)));

                        int amount = player.getItemInHand().getAmount() - 1;
                        if (amount != 0) player.getItemInHand().setAmount(amount);
                        else player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));

                        success = true;
                        break;
                    }
                }

                if (!success) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You must have a pickaxe in your inventory to redeem this"));
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("titancore.minevoucher")) {
            if (args.length > 1) {
                Player player = Bukkit.getPlayer(args[0]);
               if (player != null) {
                   if (player.getInventory().firstEmpty() != -1) {
                       ItemMeta itemMeta = voucher.getItemMeta();
                       itemMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&6Right-click to redeem " + args[1] + " blocks worth"), ChatColor.translateAlternateColorCodes('&', "&7Factors in current rank and fortune")));
                       voucher.setItemMeta(itemMeta);
                       player.getInventory().addItem(voucher);
                   } else {
                       sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Target does not have an empty inventory slot"));
                   }
               } else {
                   sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Could not find " + args[0]));
               }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lFormat: /minevoucher (player) (blocks)"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You do not have permission to execute this command"));
        }
        return true;
    }
}
