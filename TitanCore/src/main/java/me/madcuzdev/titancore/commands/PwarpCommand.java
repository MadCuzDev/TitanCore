package me.madcuzdev.titancore.commands;

import me.madcuzdev.titancore.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;


public class PwarpCommand implements Listener, CommandExecutor {
    private FileConfiguration prestigeConfig = ConfigHandler.getPrestigesConfig();

    private Inventory gui = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&6&lPrestige Mines"));

    {
        ItemStack grayGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta im = grayGlassPane.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4"));
        grayGlassPane.setItemMeta(im);

        for (int i = 0; 2 > i; i++) {
            gui.setItem(gui.firstEmpty(), grayGlassPane);
        }

        ItemStack p1 = new ItemStack(Material.DIAMOND_PICKAXE);
        im = p1.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lWarp to prestige 1"));
        p1.setItemMeta(im);
        gui.addItem(p1);

        for (int i = 20; 81 > i; i += 20) {
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lWarp to prestige " + i));
            p1.setItemMeta(im);
            gui.addItem(p1);
        }

        for (int i = 0; 4 > i; i++) {
            gui.setItem(gui.firstEmpty(), grayGlassPane);
        }

        for (int i = 100; 181 > i; i += 20) {
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lWarp to prestige " + i));
            p1.setItemMeta(im);
            gui.addItem(p1);
        }

        for (int i = 0; 6 > i; i++) {
            gui.setItem(gui.firstEmpty(), grayGlassPane);
        }

        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lWarp to prestige 200"));
        p1.setItemMeta(im);
        gui.addItem(p1);

        for (int i = 0; 4 > i; i++) {
            gui.setItem(gui.firstEmpty(), grayGlassPane);
        }

    }

    private ArrayList<Integer> pwarps;
    {
        pwarps = new ArrayList<>();
        pwarps.add(1);
        for (int i = 20; 201 > i; i += 20) {
            pwarps.add(i);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void inventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getInventory().getTitle().equals(gui.getTitle())) {
                event.setCancelled(true);
                if (event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getType() != Material.STAINED_GLASS_PANE) {
                    int prestige = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.translateAlternateColorCodes('&', "&6&lWarp to prestige "), ""));
                    if (prestigeConfig.contains(player.getUniqueId().toString()) && prestigeConfig.getInt(player.getUniqueId().toString()) >= prestige) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "warp p" + prestige + " " + player.getName());
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You must be prestige " + prestige + " or greater to warp to this mine"));
                    }
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                try {
                    int prestige = Integer.parseInt(args[0]);
                    if (pwarps.contains(prestige)) {
                        if (prestigeConfig.contains(player.getUniqueId().toString()) && prestigeConfig.getInt(player.getUniqueId().toString()) >= prestige) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "warp p" + prestige + " " + player.getName());
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You must be prestige " + prestige + " or greater to warp to this mine"));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Invalid pwarp. Type /pwarp to see a list of valid pwarps"));
                    }
                } catch (NumberFormatException exc) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Please type /pwarp for a list of valid pwarps"));
                }

            } else {
                player.openInventory(gui);
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Must be executed as a Player"));
        }
        return true;
    }
}
