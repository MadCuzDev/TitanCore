package me.madcuzdev.titancore.commands;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.madcuzdev.titancore.ConfigHandler;
import me.madcuzdev.titancore.listeners.PrestigeListener;
import net.md_5.bungee.api.ChatColor;

public class CustomizeCommand implements CommandExecutor, Listener {
	
	private static HashMap<String, ArrayList<CustomizationOption>> customizationOptions;
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        openCustomizationGUI((Player)sender);
        return true;
    }
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event){
		if(event.getInventory() != null && event.getInventory().getName().contains("Customization GUI")){
			event.setCancelled(true);
			if(event.getInventory().getName().split(" ")[0].equals("Main")){
				HashMap<Integer, String> positions = new HashMap<>();
				positions.put(4, "Border");
				positions.put(10, "Primary");
				positions.put(12, "Secondary");
				positions.put(14, "Tertiary");
				positions.put(16, "Quaternary");
				positions.put(22, "Design");
				if(positions.containsKey(event.getRawSlot()))
					openSpecializedCustomizationGUI((Player)event.getWhoClicked(), positions.get(event.getRawSlot()));
			}else{
				if(event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null && event.getCurrentItem().getItemMeta().getLore() != null){
					if(event.getCurrentItem().getItemMeta().getLore().get(0).contains("required.")){
						event.getWhoClicked().sendMessage(ChatColor.RED + "You can't use this customization option!");
					}else{
						String type = event.getInventory().getName().split(" ")[0];
						ConfigHandler.getCustomizationConfig().set(event.getWhoClicked().getUniqueId() + "." + type, customizationOptions.get(type).get(event.getRawSlot()).data);
						event.getWhoClicked().sendMessage(ChatColor.GREEN + "Customization option changed!");
						openCustomizationGUI((Player)event.getWhoClicked());
						ConfigHandler.reloadCustomizationConfig();
					}
				}
			}
		}
	}

	private void openCustomizationGUI(Player player) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "Main Customization GUI");
		int[] pos = new int[]{4,10,12,14,16,22};
		String[] names = new String[]{"Border", "Primary", "Secondary", "Tertiary", "Quaternary", "Design"};
		for(int i = 0; i < names.length; i++){
			ItemStack is = new ItemStack(Material.WOOL);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Customize " + names[i]);
			is.setItemMeta(im);
			inv.setItem(pos[i], is);
		}
		player.openInventory(inv);
		
	}
	
	private void openSpecializedCustomizationGUI(Player player, String type){
		
		Inventory inv = Bukkit.createInventory(null, 54, type + " Customization GUI");
		ArrayList<CustomizationOption> options = customizationOptions.get(type);
		for(int i = 0; i < options.size(); i++){
			CustomizationOption option = options.get(i);
			ItemStack is = new ItemStack(Material.WOOL);
			ItemMeta im = is.getItemMeta();
			ArrayList<String> lore = new ArrayList<>();
			if(ConfigHandler.getPrestigesConfig().getInt(player.getUniqueId().toString())<option.prestigeRequirement)
				lore.add(ChatColor.RED+""+ChatColor.BOLD+"Prestige " + option.prestigeRequirement + " required.");
			if(option.donatorRequirement != null && !player.hasPermission("titancore.customization." + option.donatorRequirement))
				lore.add(ChatColor.RED+""+ChatColor.BOLD+option.donatorRequirement+" rank required.");
			if(lore.size()==0)
				lore.add(ChatColor.GREEN+""+ChatColor.BOLD+"Unlocked.");
			im.setLore(lore);
			im.setDisplayName(PrestigeListener.getPartialDisplay(player, type, option.data).replace('&', '\u00A7'));
			is.setItemMeta(im);
			inv.setItem(i, is);
		}
		player.openInventory(inv);
		
		
	}
	
	public static void setupCustomizationOptions(){
		customizationOptions = new HashMap<>();
		String[] types = new String[]{"Border", "Design", "Primary", "Secondary", "Tertiary", "Quaternary"};
		for(String type : types){
			customizationOptions.put(type, new ArrayList<>());
		}
		addCustomizationOption("Border", new CustomizationOption(PrestigeListener.DEFAULT_BORDER, 0));
		addCustomizationOption("Design", new CustomizationOption(PrestigeListener.DEFAULT_DESIGN, 0));
		addCustomizationOption("Primary", new CustomizationOption(PrestigeListener.DEFAULT_PRIMARY, 0));
		addCustomizationOption("Secondary", new CustomizationOption(PrestigeListener.DEFAULT_SECONDARY, 0));
		addCustomizationOption("Tertiary", new CustomizationOption(PrestigeListener.DEFAULT_TERTIARY, 0));
		addCustomizationOption("Quaternary", new CustomizationOption(PrestigeListener.DEFAULT_QUATERNARY, 0));
		
		addCustomizationOption("Border", new CustomizationOption("%sec%&l[%design%%sec%&l]", 1));
		addCustomizationOption("Design", new CustomizationOption("%pri%&l%prestige%&l%pri%", 2));
		addCustomizationOption("Primary", new CustomizationOption("7", 3));
		addCustomizationOption("Secondary", new CustomizationOption("4", 4));
		addCustomizationOption("Tertiary", new CustomizationOption("3", 5));
		addCustomizationOption("Quaternary", new CustomizationOption("2", 6));
		
		addCustomizationOption("Border", new CustomizationOption("%qua%&ki%sec%&l[%ter%&ki%design%%ter%&ki%sec%&l]%qua%&ki", 20, "Donor"));
	}
	
	private static void addCustomizationOption(String type, CustomizationOption option){
		customizationOptions.get(type).add(option);
	}
	
}

class CustomizationOption {
	
	String data;
	int prestigeRequirement;
	String donatorRequirement;
	
	CustomizationOption(String data, int prestigeRequirement, String donatorRequirement){
		this.data = data;
		this.prestigeRequirement = prestigeRequirement;
		this.donatorRequirement = donatorRequirement;
	}
	

	CustomizationOption(String data, int prestigeRequirement){
		this.data = data;
		this.prestigeRequirement = prestigeRequirement;
		this.donatorRequirement = null;
	}
	
}
