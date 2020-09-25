package pl.trollcraft.deathswap.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.deathswap.Deathswap;
import pl.trollcraft.deathswap.chatutils.ChatUtils;

public class EndCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Komenda jedynie dla graczy.");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("Deathswap.endGame")) {
            player.sendMessage(ChatUtils.color("&cBrak uprawnien!"));
        }
        if (args.length == 1) {
            String playerName = args[0];
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getDisplayName().equalsIgnoreCase(playerName)) {
                    player.sendMessage(ChatUtils.color("&6Wymuszono zakonczenie gry!"));
                    Deathswap.gameManager.end(p);
                    return true;
                }
            }
            player.sendMessage(ChatUtils.color("&4&lNie ma takiej osoby na serwerze."));
            return true;
        }
        player.sendMessage(ChatUtils.color("&aUzycie ./end <gracz>"));

        return true;
    }
}


