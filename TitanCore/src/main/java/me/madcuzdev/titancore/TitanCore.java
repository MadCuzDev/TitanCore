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
        ConfigHandler.setupCustomizationConfig();
        ConfigHandler.setupMasteryConfig();
        getLogger().info("Configs loaded");

        EnchantHandler.setupEnchants();
        getLogger().info("Enchants loaded");

        // CommandExecutor & Listener classes
        TokenShopCommand tokenShopCommand = new TokenShopCommand();
        PwarpCommand pwarpCommand = new PwarpCommand();
        CustomizeCommand customizeCommand = new CustomizeCommand();
        BlocksCommand blocksCommand = new BlocksCommand();
        MineVoucherCommand mineVoucherCommand = new MineVoucherCommand();

        // Listener classes
        NoHungerListener noHungerListener = new NoHungerListener();
        PrestigeListener prestigeListener = new PrestigeListener();
        MiningListener miningListener = new MiningListener();
        CubicListener cubicListener = new CubicListener();
        CutterListener cutterListener = new CutterListener();
        QuakeListener quakeListener = new QuakeListener();
        SphericListener sphericListener = new SphericListener();
        ExplosiveListener explosiveListener = new ExplosiveListener();
        JackhammerListener jackhammerListener = new JackhammerListener();

        // CommandExecutor classes
        NoHungerCommand noHungerCommand = new NoHungerCommand();
        RankupCommand rankupCommand = new RankupCommand();
        MaxRankupCommand maxRankupCommand = new MaxRankupCommand();
        PrestigeCommand prestigeCommand = new PrestigeCommand();
        CeCommand ceCommand = new CeCommand();
        CostCommand costCommand = new CostCommand();
        TokenCommand tokenCommand = new TokenCommand();
        PrestigeSetCommand prestigeSetCommand = new PrestigeSetCommand();
        PickCommand pickCommand = new PickCommand();
        RanksCommand ranksCommand = new RanksCommand();
        PrestigeTopCommand prestigeTopCommand = new PrestigeTopCommand();
        MasteryCommand masteryCommand = new MasteryCommand();
        MasterySetCommand masterySetCommand = new MasterySetCommand();
        MultiplierCommand multiplierCommand = new MultiplierCommand();

        //Load presets
        CustomizeCommand.setupCustomizationOptions();
        ScoreboardHandler scoreboardHandler = new ScoreboardHandler(this);
        scoreboardHandler.startScoreboardLoop();

        
        registerEvents(noHungerListener, prestigeListener, cubicListener, miningListener,
                tokenShopCommand, cutterListener, pwarpCommand, customizeCommand,
                quakeListener, sphericListener, blocksCommand, mineVoucherCommand,
                explosiveListener, jackhammerListener);

        getCommand("rankup").setExecutor(rankupCommand);
        getCommand("nohunger").setExecutor(noHungerCommand);
        getCommand("maxrankup").setExecutor(maxRankupCommand);
        getCommand("prestige").setExecutor(prestigeCommand);
        getCommand("ce").setExecutor(ceCommand);
        getCommand("token").setExecutor(tokenCommand);
        getCommand("tokenshop").setExecutor(tokenShopCommand);
        getCommand("cost").setExecutor(costCommand);
        getCommand("pwarp").setExecutor(pwarpCommand);
        getCommand("prestigeset").setExecutor(prestigeSetCommand);
        getCommand("customize").setExecutor(customizeCommand);
        getCommand("pick").setExecutor(pickCommand);
        getCommand("ranks").setExecutor(ranksCommand);
        getCommand("blocks").setExecutor(blocksCommand);
        getCommand("minevoucher").setExecutor(mineVoucherCommand);
        getCommand("prestigetop").setExecutor(prestigeTopCommand);
        getCommand("mastery").setExecutor(masteryCommand);
        getCommand("masteryset").setExecutor(masterySetCommand);
        getCommand("multiplier").setExecutor(multiplierCommand);

        miningListener.scheduleBlocks();
        getLogger().info("Mining timer successfully started");

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
