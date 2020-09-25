package pl.trollcraft.deathswap.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import pl.trollcraft.deathswap.Deathswap;

public class DeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Deathswap.gameManager.handleDeath(event.getEntity());
    }
}
