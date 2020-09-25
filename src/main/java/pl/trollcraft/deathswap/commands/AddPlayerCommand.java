package pl.trollcraft.deathswap.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.deathswap.Deathswap;
import pl.trollcraft.deathswap.chatutils.ChatUtils;

public class AddPlayerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Komenda jedynie dla graczy.");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("Deathswap.addPlayer")) {
            player.sendMessage(ChatUtils.color("&cBrak uprawnien!"));
            return true;
        }
        if (args.length == 1) {
            String playerName = args[0];
            Deathswap.gameManager.addPlayer(player, playerName);
            return true;
        }
        player.sendMessage(ChatUtils.color("&aUzycie ./addplayer <gracz>"));
        return true;
    }
}
