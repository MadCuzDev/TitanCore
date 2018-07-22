package me.madcuzdev.titancore;

import me.madcuzdev.titancore.commands.*;
import me.madcuzdev.titancore.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TitanCore extends JavaPlugin {
    private VaultHandler vaultHandler = new VaultHandler();

    @Override
    public void onEnable() {
        if (!vaultHandler.setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Vault economy set up");

        ConfigHandler.setupPrestigesConfig();
        ConfigHandler.setupTokenConfig();
        ConfigHandler.setupCooldownConfig();
        getLogger().info("Configs loaded");

        EnchantHandler.setupEnchants();
        getLogger().info("Enchants loaded");

        // CommandExecutor & Listener classes
        NoDropListener noDropListener = new NoDropListener();
        TokenShopCommand tokenShopCommand = new TokenShopCommand();
        PwarpCommand pwarpCommand = new PwarpCommand();

        // Listener classes
        NoHungerListener noHungerListener = new NoHungerListener();
        PrestigeListener prestigeListener = new PrestigeListener();
        CubicListener cubicListener = new CubicListener();
        FortuneListener fortuneListener = new FortuneListener();
        TokenListener tokenListener = new TokenListener();
        CutterListener cutterListener = new CutterListener();

        // CommandExecutor classes
        NoHungerCommand noHungerCommand = new NoHungerCommand();
        RankupCommand rankupCommand = new RankupCommand();
        MaxRankupCommand maxRankupCommand = new MaxRankupCommand();
        PrestigeCommand prestigeCommand = new PrestigeCommand();
        CeCommand ceCommand = new CeCommand();
        CostCommand costCommand = new CostCommand();
        TokenCommand tokenCommand = new TokenCommand();
        PrestigeSetCommand prestigeSetCommand = new PrestigeSetCommand();
        DailyCommand dailyCommand = new DailyCommand();

        registerEvents(noHungerListener, prestigeListener, noDropListener, cubicListener, fortuneListener, tokenListener, tokenShopCommand, cutterListener, pwarpCommand);

        getCommand("rankup").setExecutor(rankupCommand);
        getCommand("nohunger").setExecutor(noHungerCommand);
        getCommand("maxrankup").setExecutor(maxRankupCommand);
        getCommand("prestige").setExecutor(prestigeCommand);
        getCommand("dropson").setExecutor(noDropListener);
        getCommand("ce").setExecutor(ceCommand);
        getCommand("token").setExecutor(tokenCommand);
        getCommand("tokenshop").setExecutor(tokenShopCommand);
        getCommand("cost").setExecutor(costCommand);
        getCommand("pwarp").setExecutor(pwarpCommand);
        getCommand("prestigeset").setExecutor(prestigeSetCommand);
        getCommand("daily").setExecutor(dailyCommand);

        getLogger().info("TitanCore has been successfully enabled");
    }



    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("TitanCore has been successfully disabled");
    }
}
