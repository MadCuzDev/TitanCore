package me.madcuzdev.titancore.listeners;

import me.madcuzdev.titancore.commands.NoHungerCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class NoHungerListener implements Listener {
    private NoHungerCommand noHungerCommand = new NoHungerCommand();

    @EventHandler
    public void onHungerLoss(FoodLevelChangeEvent event) {
        if (noHungerCommand.getNoHungerStatus() && event.getEntity() instanceof Player) {
            event.setCancelled(true);
            Player player = (Player)event.getEntity();
            if (player.getFoodLevel() < 19.5D) {
                player.setFoodLevel(20);
            }
        }
    }
}
