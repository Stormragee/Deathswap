package pl.trollcraft.deathswap.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.deathswap.Deathswap;

public class CreateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Komenda jedynie dla graczy.");
            return true;
        }
        Player player = (Player) sender;
        Boolean broad = false;
        if(args.length>0){
            broad = true;
        }
        Deathswap.gameManager.create(player,broad);
        return true;
    }
}
