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
import org.bukkit.event.player.PlayerInteractEvent;
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
        prices.put("Spheric", 7500);
        prices.put("Scavenger", 10000);
        prices.put("Explosive", 500);
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
        maxes.put("Scavenger", 100);
        maxes.put("Explosive", 5000);
    }

    private Inventory buyGUI = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', "&6&lEnchantments - Enchant"));

    {
        ItemStack grayGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta im = grayGlassPane.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4"));
        grayGlassPane.setItemMeta(im);

        ItemStack paper = new ItemStack(Material.PAPER);
        im = paper.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lDisenchant"));
        lore.clear();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to access the disenchanting menu"));
        im.setLore(lore);
        paper.setItemMeta(im);
        paper.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        buyGUI.addItem(paper);

        for (int i = 0; 3 > i; i++) {
            buyGUI.setItem(buyGUI.firstEmpty(), grayGlassPane);
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
        buyGUI.addItem(obsidian);

        for (int i = 0; 6 > i; i++) {
            buyGUI.setItem(buyGUI.firstEmpty(), grayGlassPane);
        }

        addShopItem(Material.GOLD_NUGGET, "MoneyBags", false);

        addShopItem(Material.IRON_INGOT, "Casino", false);

        addShopItem(Material.SLIME_BALL, "Spheric", false);

        addShopItem(Material.BRICK, "Cubic", false);

        addShopItem(Material.TRIPWIRE_HOOK, "Scavenger", false);

        for (int i = 0; 5 > i; i++) {
            buyGUI.setItem(buyGUI.firstEmpty(), grayGlassPane);
        }

        addShopItem(Material.DEAD_BUSH, "Quake", false);

        addShopItem(Material.WEB, "Silk Touch", false);

        addShopItem(Material.COOKIE, "Cutter", false);

        for (int i = 0; 6 > i; i++) {
            buyGUI.setItem(buyGUI.firstEmpty(), grayGlassPane);
        }

        addShopItem(Material.TNT, "Explosive", false);

        addShopItem(Material.BEACON, "Efficiency", false);

        addShopItem(Material.DIAMOND, "Fortune", false);

        for (int i = 0; 3 > i; i++) {
            buyGUI.setItem(buyGUI.firstEmpty(), grayGlassPane);
        }
    }

    private Inventory sellGUI = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', "&6&lEnchantments - Disenchant"));

    {
        ItemStack grayGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta im = grayGlassPane.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4"));
        grayGlassPane.setItemMeta(im);

        ItemStack paper = new ItemStack(Material.PAPER);
        im = paper.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lEnchant"));
        lore.clear();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to access the enchanting menu"));
        im.setLore(lore);
        paper.setItemMeta(im);
        paper.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        sellGUI.addItem(paper);

        for (int i = 0; 3 > i; i++) {
            sellGUI.setItem(sellGUI.firstEmpty(), grayGlassPane);
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
        sellGUI.addItem(obsidian);

        for (int i = 0; 6 > i; i++) {
            sellGUI.setItem(sellGUI.firstEmpty(), grayGlassPane);
        }

        addShopItem(Material.GOLD_NUGGET, "MoneyBags", true);

        addShopItem(Material.IRON_INGOT, "Casino", true);

        addShopItem(Material.SLIME_BALL, "Spheric", true);

        addShopItem(Material.BRICK, "Cubic", true);

        addShopItem(Material.TRIPWIRE_HOOK, "Scavenger", true);

        for (int i = 0; 5 > i; i++) {
            sellGUI.setItem(sellGUI.firstEmpty(), grayGlassPane);
        }

        addShopItem(Material.DEAD_BUSH, "Quake", true);

        addShopItem(Material.WEB, "Silk Touch", true);

        addShopItem(Material.COOKIE, "Cutter", true);

        for (int i = 0; 6 > i; i++) {
            sellGUI.setItem(sellGUI.firstEmpty(), grayGlassPane);
        }

        addShopItem(Material.TNT, "Explosive", true);

        addShopItem(Material.BEACON, "Efficiency", true);

        addShopItem(Material.DIAMOND, "Fortune", true);

        for (int i = 0; 3 > i; i++) {
            sellGUI.setItem(sellGUI.firstEmpty(), grayGlassPane);
        }
    }

    private void addShopItem(Material material, String enchant, boolean sell) {
        ItemStack shopItem = new ItemStack(material);
        ItemMeta im = shopItem.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&l" + enchant));
        double multi = 1;
        if (sell) multi = 0.75;

        lore.clear();
        String price = PriceHandler.formatNumber((double) prices.get(enchant) * multi);
        lore.add(ChatColor.translateAlternateColorCodes('&', sell ? "&7REFUND: " + price : "&7COST: "
                + price));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7MAX: "
                + PriceHandler.formatNumber((double) maxes.get(enchant))));
        im.setLore(lore);
        shopItem.setItemMeta(im);

        if (!sell) buyGUI.addItem(shopItem);
        else sellGUI.addItem(shopItem);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void inventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getInventory().getTitle().equals(buyGUI.getTitle())) {
                openBuyGUI(event, player);
            } else if (event.getInventory().getTitle().equals(sellGUI.getTitle())) {
                openSellGUI(event, player);
            }
        }
    }

    @EventHandler
    public void rightClickPick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                player.openInventory(buyGUI);
            }
        }
    }

    private void openSellGUI(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        if (player.getItemInHand().getType() != Material.AIR) {
            if (event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getType() != Material.STAINED_GLASS_PANE) {
                int multi = 1;
                if (event.getClick() == ClickType.RIGHT) {
                    multi = 10;
                } else if (event.getClick() == ClickType.DROP) {
                    multi = 100;
                } else if (event.getClick() == ClickType.MIDDLE) {
                    multi = 1000;
                }
                boolean success = false;
                Enchantment ench;
                String displayName = event.getCurrentItem().getItemMeta().getDisplayName().replace("§6§l", "");
                switch (displayName) {
                    case "Enchant":
                        player.closeInventory();
                        player.openInventory(buyGUI);
                        return;
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
                    case "Scavenger":
                        ench = EnchantHandler.Scavenger;
                        break;
                    case "Explosive":
                        ench = EnchantHandler.Explosive;
                        break;
                    default:
                        return;
                }
                if (player.getItemInHand().getAmount() == 1) {
                    if (player.getItemInHand().containsEnchantment(ench) && player.getItemInHand().getEnchantmentLevel(ench) - multi >= 0) {
                        success = true;
                        if (ench == Enchantment.LOOT_BONUS_BLOCKS) {
                            success = player.getItemInHand().getEnchantmentLevel(ench) - multi >= 5;
                            if (!success)
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You cannot disenchant fortune below level 5"));
                        }
                        if (ench == Enchantment.DIG_SPEED) {
                            success = player.getItemInHand().getEnchantmentLevel(ench) - multi >= 10;
                            if (!success)
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You cannot disenchant efficiency below level 10"));
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You cannot disenchant something to below level 0"));
                    }
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Cannot disenchant multiple items"));
                }
                if (success) {
                    int lvl = player.getItemInHand().getEnchantmentLevel(ench) - multi;

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

                    if (lvl != 0) lore.add(ChatColor.translateAlternateColorCodes('&', "&7" + displayName + " " + lvl));
                    im.setLore(lore);
                    im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    player.getItemInHand().setItemMeta(im);

                    ConfigHandler.getTokenConfig().set(player.getUniqueId().toString(), ConfigHandler.getTokenConfig().getDouble(player.getUniqueId().toString()) + ((prices.get(displayName) * 0.75) * multi));
                    ConfigHandler.reloadTokenConfig();

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSuccessfully disenchanted " + multi + " levels of " + displayName));
                }
            }
        }
    }

    private void openBuyGUI(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        if (player.getItemInHand().getType() != Material.AIR) {
            if (event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getType() != Material.STAINED_GLASS_PANE) {
                int multi = 1;
                if (event.getClick() == ClickType.RIGHT) {
                    multi = 10;
                } else if (event.getClick() == ClickType.DROP) {
                    multi = 100;
                } else if (event.getClick() == ClickType.MIDDLE) {
                    multi = 1000;
                }
                double tokenBal = ConfigHandler.getTokenConfig().getDouble(player.getUniqueId().toString());
                int lvl = multi;
                boolean success = false;
                Enchantment ench;
                String displayName = event.getCurrentItem().getItemMeta().getDisplayName().replace("§6§l", "");
                switch (displayName) {
                    case "Disenchant":
                        player.closeInventory();
                        player.openInventory(sellGUI);
                        return;
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
                    case "Scavenger":
                        ench = EnchantHandler.Scavenger;
                        break;
                    case "Explosive":
                        ench = EnchantHandler.Explosive;
                        break;
                    default:
                        return;
                }
                int enchLevel = multi;
                if (player.getItemInHand().containsEnchantment(ench)) {
                    enchLevel += player.getItemInHand().getEnchantmentLevel(ench);
                }
                if (player.getItemInHand().getAmount() == 1) {
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
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Cannot enchant multiple items"));
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
                    ConfigHandler.reloadTokenConfig();

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSuccessfully applied " + displayName));
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.getItemInHand() != null) {
                player.openInventory(buyGUI);
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Cannot enchant this item"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Must be executed as a Player"));
        }
        return true;
    }
}