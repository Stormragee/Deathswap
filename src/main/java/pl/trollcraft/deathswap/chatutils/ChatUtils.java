package pl.trollcraft.deathswap.chatutils;

import org.bukkit.ChatColor;

public class ChatUtils {
    public static String color(String toColor) {
        return toColor.replace("&", ChatColor.COLOR_CHAR + "");
    }
}
