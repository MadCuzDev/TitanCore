package me.madcuzdev.titancore;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigHandler {

    private static File prestigesFile = new File("plugins/TitanCore/prestiges.yml");
    private static FileConfiguration prestigesConfig;

    public static FileConfiguration getPrestigesConfig() {
        return prestigesConfig;
    }

    static void setupPrestigesConfig() {
        prestigesConfig = YamlConfiguration.loadConfiguration(prestigesFile);
        getPrestigesConfig().options().copyDefaults(true);
        savePrestigesConfig();
    }

    private static void savePrestigesConfig() {
        try {
            prestigesConfig.save(prestigesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadPrestigesConfig() {
        try {
            prestigesConfig.load(prestigesFile);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadPrestigesConfig() {
        savePrestigesConfig();
        loadPrestigesConfig();
    }

    
    private static File tokenFile = new File("plugins/TitanCore/token.yml");
    private static FileConfiguration tokenConfig;

    public static FileConfiguration getTokenConfig() {
        return tokenConfig;
    }

    static void setupTokenConfig() {
        tokenConfig = YamlConfiguration.loadConfiguration(tokenFile);
        getTokenConfig().options().copyDefaults(true);
        saveTokenConfig();
    }

    private static void saveTokenConfig() {
        try {
            tokenConfig.save(tokenFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadTokenConfig() {
        try {
            tokenConfig.load(tokenFile);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadTokenConfig() {
        saveTokenConfig();
        loadTokenConfig();
    }

    private static File cooldownFile = new File("plugins/TitanCore/cooldown.yml");
    private static FileConfiguration cooldownConfig;

    public static FileConfiguration getCooldownConfig() {
        return cooldownConfig;
    }

    static void setupCooldownConfig() {
        cooldownConfig = YamlConfiguration.loadConfiguration(cooldownFile);
        getCooldownConfig().options().copyDefaults(true);
        saveCooldownConfig();
    }

    private static void saveCooldownConfig() {
        try {
            cooldownConfig.save(cooldownFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static File customizationFile = new File("plugins/TitanCore/customization.yml");
    private static FileConfiguration customizationConfig;

    public static FileConfiguration getCustomizationConfig() {
        return customizationConfig;
    }

    static void setupCustomizationConfig() {
        customizationConfig = YamlConfiguration.loadConfiguration(customizationFile);
        getCustomizationConfig().options().copyDefaults(true);
        saveCustomizationConfig();
    }

    private static void saveCustomizationConfig() {
        try {
            customizationConfig.save(customizationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadCustomizationConfig() {
        try {
            customizationConfig.load(customizationFile);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadCustomizationConfig() {
        saveCustomizationConfig();
        loadCustomizationConfig();
    }

    private static File masteryFile = new File("plugins/TitanCore/mastery.yml");
    private static FileConfiguration masteryConfig;

    public static FileConfiguration getMasteryConfig() {
        return masteryConfig;
    }

    static void setupMasteryConfig() {
        masteryConfig = YamlConfiguration.loadConfiguration(masteryFile);
        getMasteryConfig().options().copyDefaults(true);
        saveMasteryConfig();
    }

    private static void saveMasteryConfig() {
        try {
            masteryConfig.save(masteryFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadMasteryConfig() {
        try {
            masteryConfig.load(masteryFile);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadMasteryConfig() {
        saveMasteryConfig();
        loadMasteryConfig();
    }

}
