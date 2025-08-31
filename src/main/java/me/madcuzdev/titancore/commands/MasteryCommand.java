package me.madcuzdev.titancore.commands;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.madcuzdev.titancore.ConfigHandler;
import me.madcuzdev.titancore.PriceHandler;
import me.madcuzdev.titancore.VaultHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getServer;


public class MasteryCommand implements CommandExecutor {
    private RankupCommand rankupCommand = new RankupCommand();

    private LuckPermsApi luckPermsApi = LuckPerms.getApi();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();
            User user = luckPermsApi.getUser(player.getName());
            assert user != null;
            boolean containsUser = ConfigHandler.getMasteryConfig().contains(uuid);
            int presRequired = containsUser ? 200 + 100 * ConfigHandler.getMasteryConfig().getInt(uuid) : 200;
            if (ConfigHandler.getPrestigesConfig().getInt(uuid) >= presRequired) {
                ConfigHandler.getPrestigesConfig().set(uuid, 0);
                ConfigHandler.reloadPrestigesConfig();

                ConfigHandler.getMasteryConfig().set(uuid, containsUser ? ConfigHandler.getMasteryConfig().getInt(uuid) + 1 : 1);
                ConfigHandler.reloadMasteryConfig();

                rankupCommand.setRankup(user, "z", "default");
                Bukkit.dispatchCommand(getServer().getConsoleSender(), "crate give to " + player.getName() + " mastery 1");

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSuccess! You've masteried to " + ConfigHandler.getMasteryConfig().get(uuid) + " for " + presRequired + " prestiges"));
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You need " + presRequired + " prestiges to mastery"));
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Must be executed as a Player"));
        }
        return true;
    }
}
