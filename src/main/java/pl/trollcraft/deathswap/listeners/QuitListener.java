package pl.trollcraft.deathswap.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.trollcraft.deathswap.Deathswap;

public class QuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Deathswap.gameManager.handleQuit(event.getPlayer());
    }
}
