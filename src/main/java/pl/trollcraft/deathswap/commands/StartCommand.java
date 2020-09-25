package pl.trollcraft.deathswap.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.deathswap.Deathswap;
import pl.trollcraft.deathswap.chatutils.ChatUtils;

public class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Komenda jedynie dla graczy.");
            return true;
        }
        Player player = (Player) sender;
        player.sendMessage(ChatUtils.color("&3&lWystartowales gre!"));
        Deathswap.gameManager.start(player);
        return true;
    }
}


