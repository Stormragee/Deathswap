package pl.trollcraft.deathswap;

import org.bukkit.plugin.java.JavaPlugin;
import pl.trollcraft.deathswap.commands.*;
import pl.trollcraft.deathswap.listeners.DeathListener;
import pl.trollcraft.deathswap.listeners.QuitListener;
import pl.trollcraft.deathswap.obj.GameManager;

public class Deathswap extends JavaPlugin {
    public static GameManager gameManager;

    @Override
    public void onEnable() {
        gameManager = new GameManager(this);
        getCommand("start").setExecutor(new StartCommand());
        getCommand("create").setExecutor(new CreateCommand());
        getCommand("end").setExecutor(new EndCommand());
        getCommand("join").setExecutor(new JoinCommand());
        getCommand("add").setExecutor(new AddPlayerCommand());
        getServer().getPluginManager().registerEvents(new DeathListener(),this);
        getServer().getPluginManager().registerEvents(new QuitListener(),this);

    }
}
