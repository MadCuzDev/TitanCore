package me.madcuzdev.titancore.listeners;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.madcuzdev.titancore.ConfigHandler;

public class PrestigeListener implements Listener {
	
	public static final String DEFAULT_DESIGN = "%pri%%prestige%";
	public static final String DEFAULT_BORDER = "%sec%[%design%%sec%]";
	public static final String DEFAULT_PRIMARY = "d";
	public static final String DEFAULT_SECONDARY = "8";
	public static final String DEFAULT_TERTIARY = "d";
	public static final String DEFAULT_QUATERNARY = "d";
	
    @EventHandler
    public void addPrestigeToChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        System.out.println(getFullDisplay(player));
        event.setFormat(event.getFormat().replace("{PRESTIGE}", getFullDisplay(player)).replace('&', '\u00A7'));
    }
    
    public static String getCustomizationOption(String option, Player player){
    	ConfigurationSection section = ConfigHandler.getCustomizationConfig().getConfigurationSection("" + player.getUniqueId());
    	if(section == null){
    		section = ConfigHandler.getCustomizationConfig().createSection("" + player.getUniqueId());
    		section.set("Design", DEFAULT_DESIGN);
    		section.set("Border", DEFAULT_BORDER);
    		section.set("Primary", DEFAULT_PRIMARY);
    		section.set("Secondary", DEFAULT_SECONDARY);
    		section.set("Tertiary", DEFAULT_TERTIARY);
    		section.set("Quaternary", DEFAULT_QUATERNARY);
    	}
    	return section.getString(option);
    }
    
    public static String getFullDisplay(Player player){
    	return getPartialDisplay(player, " ", " ");
    }
    
    public static String getPartialDisplay(Player player, String option, String replacement){
    	String display = option.equals("Border")?replacement:getCustomizationOption("Border", player);
    	display=display.replace("%design%", option.equals("Design")?replacement:getCustomizationOption("Design", player));
    	display=display.replace("%prestige%", ConfigHandler.getPrestigesConfig().getInt(player.getUniqueId().toString())+"");
    	display=display.replace("%pri%", "&"+(option.equals("Primary")?replacement:getCustomizationOption("Primary", player)));
    	display=display.replace("%sec%", "&"+(option.equals("Secondary")?replacement:getCustomizationOption("Secondary", player)));
    	display=display.replace("%ter%", "&"+(option.equals("Tertiary")?replacement:getCustomizationOption("Tertiary", player)));
    	display=display.replace("%qua%", "&"+(option.equals("Quaternary")?replacement:getCustomizationOption("Quaternary", player)));
    	return display;
    }

}
