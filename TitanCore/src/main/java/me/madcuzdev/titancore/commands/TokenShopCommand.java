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
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;


public class TokenShopCommand implements Listener, CommandExecutor {
    private ArrayList<String> lore = new ArrayList<>();

    private HashMap<String, Integer> prices;
    {
        prices = new HashMap<>();
        prices.put("Fortune", 2000);
        prices.put("Cubic", 6000);
        prices.put("Efficiency", 800);
        prices.put("Silk Touch", 5000);
        prices.put("Cutter", 10000000);
        prices.put("MoneyBags", 100000);
        prices.put("Casino", 100000);
        prices.put("Quake", 20000000);
        prices.put("Spheric", 10000);
    }

    private HashMap<String, Integer> maxes;
    {
        maxes = new HashMap<>();
        maxes.put("Fortune", 30000);
        maxes.put("Cubic", 5000);
        maxes.put("Efficiency", 30000);
        maxes.put("Silk Touch", 1);
        maxes.put("Cutter", 6);
        maxes.put("MoneyBags", 500);
        maxes.put("Casino", 500);
        maxes.put("Quake", 6);
        maxes.put("Spheric", 5000);
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
        obsidian.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        gui.addItem(obsidian);

        for (int i = 0; 7 > i; i++) {
            gui.setItem(gui.firstEmpty(), grayGlassPane);
        }

        addShopItem(Material.IRON_INGOT, "Casino");

        addShopItem(Material.DIAMOND, "Fortune");

        addShopItem(Material.TNT, "Cubic");

        for (int i = 0; 6 > i; i++) {
            gui.setItem(gui.firstEmpty(), grayGlassPane);
        }

        addShopItem(Material.BEACON, "Efficiency");

        addShopItem(Material.WEB, "Silk Touch");

        addShopItem(Material.COOKIE, "Cutter");

        for (int i = 0; 6 > i; i++) {
            gui.setItem(gui.firstEmpty(), grayGlassPane);
        }

        addShopItem(Material.DEAD_BUSH, "Quake");
        
        addShopItem(Material.GOLD_NUGGET, "MoneyBags");

        addShopItem(Material.SLIME_BALL, "Spheric");
        
        for (int i = 0; 3 > i; i++) {
            gui.setItem(gui.firstEmpty(), grayGlassPane);
        }
    }
    
    private void addShopItem(Material material, String enchant) {
        ItemStack shopItem = new ItemStack(material);
        ItemMeta im = shopItem.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&l" + enchant));
        lore.clear();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7COST: "
                + PriceHandler.formatNumber((double) prices.get(enchant))));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7MAX: "
                + PriceHandler.formatNumber((double) maxes.get(enchant))));
        im.setLore(lore);
        shopItem.setItemMeta(im);
        gui.addItem(shopItem);
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
                            case "Silk Touch":
                                ench = Enchantment.SILK_TOUCH;
                                break;
                            case "MoneyBags":
                            	ench = EnchantHandler.MoneyBags;
                            	break;
                            case "Casino":
                                ench = EnchantHandler.Casino;
                                break;
                            case "Cutter":
                                ench = EnchantHandler.Cutter;
                                break;
                            case "Quake":
                                ench = EnchantHandler.Quake;
                                break;
                            case "Spheric":
                                ench = EnchantHandler.Spheric;
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
