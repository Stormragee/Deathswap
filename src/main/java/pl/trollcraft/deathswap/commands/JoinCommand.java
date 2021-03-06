package pl.trollcraft.deathswap.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.deathswap.Deathswap;
import pl.trollcraft.deathswap.chatutils.ChatUtils;

public class JoinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Komenda jedynie dla graczy.");
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 1) {
            String gameId = args[0];
            Deathswap.gameManager.join(player, gameId);
            return true;
        }
        player.sendMessage(ChatUtils.color("&aUzycie ./dolacz <kod>"));
        return true;
    }
}
