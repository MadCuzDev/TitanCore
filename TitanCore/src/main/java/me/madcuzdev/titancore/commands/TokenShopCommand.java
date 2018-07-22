package me.madcuzdev.titancore.commands;

import me.madcuzdev.titancore.ConfigHandler;
import me.madcuzdev.titancore.EnchantHandler;
import me.madcuzdev.titancore.PriceHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class TokenShopCommand implements Listener, CommandExecutor {
    private ArrayList<String> lore = new ArrayList<>();

    private HashMap<String, Integer> prices;
    {
        prices = new HashMap<>();
        prices.put("NoDrop", 5000);
        prices.put("Fortune", 2000);
        prices.put("Cubic", 8000);
        prices.put("Efficiency", 800);
        prices.put("Silk Touch", 5000);
        prices.put("Cutter", 10000000);
    }

    private HashMap<String, Integer> maxes;
    {
        maxes = new HashMap<>();
        maxes.put("NoDrop", 1);
        maxes.put("Fortune", 30000);
        maxes.put("Cubic", 5000);
        maxes.put("Efficiency", 30000);
        maxes.put("Silk Touch", 1);
        maxes.put("Cutter", 6);
    }

    private Inventory gui = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', "&6&lEnchantments"));
    {
        ItemStack grayGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta im = grayGlassPane.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4"));
        grayGlassPane.setItemMeta(im);

        for (int i = 0; 4 > i; i++) {
            gui.setItem(gui.firstEmpty(), grayGlassPane);
        }

        ItemStack obsidian = new ItemStack(Material.OBSIDIAN);
        im = obsidian.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lInfo"));
        lore.clear();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Middle-click for 1000x"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Drop for 100x"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Right-click for 10x"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Left-click for 1x"));
        im.setLore(lore);
        obsidian.setItemMeta(im);
        gui.addItem(obsidian);

        for (int i = 0; 7 > i; i++) {
            gui.setItem(gui.firstEmpty(), grayGlassPane);
        }

        ItemStack feather = new ItemStack(Material.FEATHER);
        im = feather.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lNoDrop"));
        lore.clear();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7COST: "
                + PriceHandler.formatNumber((double) prices.get("NoDrop"))));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7MAX: "
                + PriceHandler.formatNumber((double) maxes.get("NoDrop"))));
        im.setLore(lore);
        feather.setItemMeta(im);
        gui.addItem(feather);

        ItemStack diamond = new ItemStack(Material.DIAMOND);
        im = diamond.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lFortune"));
        lore.clear();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7COST: "
                + PriceHandler.formatNumber((double) prices.get("Fortune"))));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7MAX: "
                + PriceHandler.formatNumber((double) maxes.get("Fortune"))));
        im.setLore(lore);
        diamond.setItemMeta(im);
        gui.addItem(diamond);

        ItemStack tnt = new ItemStack(Material.TNT);
        im = tnt.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lCubic"));
        lore.clear();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7COST: "
                + PriceHandler.formatNumber((double) prices.get("Cubic"))));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7MAX: "
                + PriceHandler.formatNumber((double) maxes.get("Cubic"))));
        im.setLore(lore);
        tnt.setItemMeta(im);
        gui.addItem(tnt);

        for (int i = 0; 6 > i; i++) {
            gui.setItem(gui.firstEmpty(), grayGlassPane);
        }

        ItemStack beacon = new ItemStack(Material.BEACON);
        im = beacon.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lEfficiency"));
        lore.clear();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7COST: "
                + PriceHandler.formatNumber((double) prices.get("Efficiency"))));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7MAX: "
                + PriceHandler.formatNumber((double) maxes.get("Efficiency"))));
        im.setLore(lore);
        beacon.setItemMeta(im);
        gui.addItem(beacon);

        ItemStack web = new ItemStack(Material.WEB);
        im = web.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lSilk Touch"));
        lore.clear();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7COST: "
                + PriceHandler.formatNumber((double) prices.get("Silk Touch"))));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7MAX: "
                + PriceHandler.formatNumber((double) maxes.get("Silk Touch"))));
        im.setLore(lore);
        web.setItemMeta(im);
        gui.addItem(web);

        ItemStack cookie = new ItemStack(Material.COOKIE);
        im = cookie.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lCutter"));
        lore.clear();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7COST: "
                + PriceHandler.formatNumber((double) prices.get("Cutter"))));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7MAX: "
                + PriceHandler.formatNumber((double) maxes.get("Cutter"))));
        im.setLore(lore);
        cookie.setItemMeta(im);
        gui.addItem(cookie);

        for (int i = 0; 12 > i; i++) {
            gui.setItem(gui.firstEmpty(), grayGlassPane);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void inventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getInventory().getTitle().equals(gui.getTitle())) {
                event.setCancelled(true);
                if (player.getItemInHand().getType() != Material.AIR) {
                    if (event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getType() != Material.STAINED_GLASS_PANE) {
                        int multi = 1;
                        if (event.getClick() == ClickType.RIGHT) {
                            multi = 10;
                        } else if(event.getClick() == ClickType.DROP) {
                            multi = 100;
                        } else if(event.getClick() == ClickType.MIDDLE) {
                            multi = 1000;
                        }
                        double tokenBal = ConfigHandler.getTokenConfig().getDouble(player.getUniqueId().toString());
                        int lvl = multi;
                        boolean success = false;
                        Enchantment ench;
                        String displayName = event.getCurrentItem().getItemMeta().getDisplayName().replace("§6§l", "");
                        switch (displayName) {
                            case "Fortune":
                                ench = Enchantment.LOOT_BONUS_BLOCKS;
                                break;
                            case "Cubic":
                                ench = EnchantHandler.Cubic;
                                break;
                            case "Efficiency":
                                ench = Enchantment.DIG_SPEED;
                                break;
                            case "NoDrop":
                                ench = EnchantHandler.NoDrop;
                                break;
                            case "Silk Touch":
                                ench = Enchantment.SILK_TOUCH;
                                break;
                            case "Cutter":
                                ench = EnchantHandler.Cutter;
                                break;
                                default:
                                    return;
                        }
                        int enchLevel = multi;
                        if (player.getItemInHand().containsEnchantment(ench)) {
                            enchLevel += player.getItemInHand().getEnchantmentLevel(ench);
                        }
                        if (enchLevel <= maxes.get(displayName)) {
                            if (ConfigHandler.getTokenConfig().getDouble(player.getUniqueId().toString()) >= prices.get(displayName) * multi) {

                                success = true;
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You need " + PriceHandler.formatNumber((double) prices.get(displayName) * multi) + " tokens for this enchant"));
                            }
                        } else {
                            String number = String.valueOf(maxes.get(displayName));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Max level for this enchant is " + PriceHandler.formatNumber(Double.parseDouble(number))));
                        }
                        if (success) {
                            if (player.getItemInHand().containsEnchantment(ench)) {
                                lvl += player.getItemInHand().getEnchantmentLevel(ench);
                            }

                            player.getItemInHand().addUnsafeEnchantment(ench, lvl);

                            ArrayList<String> lore = new ArrayList<>();
                            if (player.getItemInHand().getItemMeta().getLore() != null) {
                                lore.addAll(player.getItemInHand().getItemMeta().getLore());
                            }

                            for (int i = 0; lore.size() > i; i++) {
                                if (lore.get(i).startsWith(ChatColor.translateAlternateColorCodes('&', "&7" + displayName))) {
                                    lore.remove(i);
                                }
                            }

                            ItemMeta im = player.getItemInHand().getItemMeta();

                            lore.add(ChatColor.translateAlternateColorCodes('&', "&7" + displayName + " " + lvl));
                            im.setLore(lore);
                            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            player.getItemInHand().setItemMeta(im);

                            ConfigHandler.getTokenConfig().set(player.getUniqueId().toString(), tokenBal - prices.get(displayName) * multi);
                            ConfigHandler.reloadtokenConfig();

                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSuccessfully applied " + displayName));
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.getItemInHand() != null) {
                player.openInventory(gui);
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Cannot enchant this item"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Must be executed as a Player"));
        }
        return true;
    }
}
