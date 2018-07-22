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
					if(false&&event.getCurrentItem().getItemMeta().getLore().get(0).contains("required.")){
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
	
	public static void addCustomizationColors(String color, int primaryLevel){
		String[] types = new String[]{"Primary", "Secondary", "Tertiary", "Quaternary"};
		for(int i = 0; i < types.length; i++){
			addCustomizationOption(types[i], new CustomizationOption(color, primaryLevel*(i+1)));
		}
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
		
		//Colors
		
		addCustomizationColors("8", 1);
		addCustomizationColors("7", 2);
		addCustomizationColors("4", 3);
		addCustomizationColors("1", 4);
		addCustomizationColors("2", 5);
		addCustomizationColors("5", 6);
		addCustomizationColors("9", 7);
		addCustomizationColors("a", 8);
		addCustomizationColors("b", 9);
		addCustomizationColors("d", 10);
		addCustomizationColors("9", 15);
		addCustomizationColors("e", 25);
		addCustomizationColors("6", 35);
		addCustomizationColors("f", 50);
		addCustomizationColors("0", 75);
		
		//Borders
		
		addCustomizationOption("Border", new CustomizationOption("%sec%(%design%%sec%)",1));
		addCustomizationOption("Border", new CustomizationOption("%sec%<%design%%sec%>",2));
		addCustomizationOption("Border", new CustomizationOption("%sec%{%design%%sec%}",3));
		
		addCustomizationOption("Border", new CustomizationOption("%sec%&l[%design%%sec%&l]",20));
		addCustomizationOption("Border", new CustomizationOption("%sec%&l(%design%%sec%&l)",22));
		addCustomizationOption("Border", new CustomizationOption("%sec%&l<%design%%sec%&l>",24));
		addCustomizationOption("Border", new CustomizationOption("%sec%&l{%design%%sec%&l}",26));
		
		addCustomizationOption("Border", new CustomizationOption("%ter%&ki%sec%&l[%design%%sec%&l]%ter%&ki",50));
		addCustomizationOption("Border", new CustomizationOption("%ter%&ki%sec%&l(%design%%sec%&l)%ter%&ki",53));
		addCustomizationOption("Border", new CustomizationOption("%ter%&ki%sec%&l<%design%%sec%&l>%ter%&ki",56));
		addCustomizationOption("Border", new CustomizationOption("%ter%&ki%sec%&l{%design%%sec%&l}%ter%&ki",59));
		
		addCustomizationOption("Border", new CustomizationOption("%ter%&ki%sec%&l--[%design%%sec%&l]--%ter%&ki",100));
		addCustomizationOption("Border", new CustomizationOption("%ter%&ki%sec%&l--(%design%%sec%&l)--%ter%&ki",104));
		addCustomizationOption("Border", new CustomizationOption("%ter%&ki%sec%&l--<%design%%sec%&l>--%ter%&ki",108));
		addCustomizationOption("Border", new CustomizationOption("%ter%&ki%sec%&l--{%design%%sec%&l}--%ter%&ki",112));
		
		addCustomizationOption("Border", new CustomizationOption("%ter%&ki%sec%&l&m--[%design%%sec%&l&m]--%ter%&ki",200));
		addCustomizationOption("Border", new CustomizationOption("%ter%&ki%sec%&l&m--(%design%%sec%&l&m)--%ter%&ki",205));
		addCustomizationOption("Border", new CustomizationOption("%ter%&ki%sec%&l&m--<%design%%sec%&l&m>--%ter%&ki",210));
		addCustomizationOption("Border", new CustomizationOption("%ter%&ki%sec%&l&m--{%design%%sec%&l&m}--%ter%&ki",215));
		
		addCustomizationOption("Border", new CustomizationOption("%ter%&k&li%sec%&l&m--<[%design%%qua%&l&m%sec%&l&m]>--%ter%&k&li", 350));
		addCustomizationOption("Border", new CustomizationOption("%ter%&k&li%sec%&l&m--[(%design%%qua%&l&m%sec%&l&m)]--%ter%&k&li", 356));
		addCustomizationOption("Border", new CustomizationOption("%ter%&k&li%sec%&l&m--{<%design%%qua%&l&m%sec%&l&m>}--%ter%&k&li", 362));
		addCustomizationOption("Border", new CustomizationOption("%ter%&k&li%sec%&l&m--({%design%%qua%&l&m%sec%&l&m})--%ter%&k&li", 368));
		
		//Designs
		
		addCustomizationOption("Design", new CustomizationOption("%pri%&l%prestige%", 25));
		addCustomizationOption("Design", new CustomizationOption("%ter%&ki%pri%&l%prestige%%ter%&ki", 75));
		addCustomizationOption("Design", new CustomizationOption("%qua%&ki%ter%&ki%pri%&l%prestige%%ter%&ki%qua%&ki", 150));
		addCustomizationOption("Design", new CustomizationOption("%qua%&ki%ter%&k&li%pri%&l%prestige%%ter%&k&li%qua%&ki", 225));
		addCustomizationOption("Design", new CustomizationOption("%qua%&ki%ter%&k&li%qua%&ki%pri%&l%prestige%%qua%&ki%ter%&k&li%qua%&ki", 325));
		addCustomizationOption("Design", new CustomizationOption("%qua%&ki%ter%&k&li%qua%&k&li%pri%&l%prestige%%qua%&k&li%ter%&k&li%qua%&ki", 500));
		addCustomizationOption("Design", new CustomizationOption("%qua%&k&li%ter%&k&li%qua%&k&li%pri%&l%prestige%%qua%&k&li%ter%&k&li%qua%&k&li", 1000));
		
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
