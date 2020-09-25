package pl.trollcraft.deathswap.chatutils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtils {
    public static String color(String toColor) {
        return toColor.replace("&", ChatColor.COLOR_CHAR + "");
    }
    public static void broadcastID(String id,String playername){
        for(Player p1: Bukkit.getOnlinePlayers()){
            p1.sendMessage(color("&lGracz " + playername + " utworzyl poczekalnie o kodzie &c&l  " + id ));
        }
    }

}
