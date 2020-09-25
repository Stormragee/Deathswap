package pl.trollcraft.deathswap.obj;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import pl.trollcraft.deathswap.Deathswap;
import pl.trollcraft.deathswap.chatutils.ChatUtils;


import java.util.ArrayList;

public class GameManager {
    private final Deathswap plugin;
    private ArrayList<Game> games;

    public GameManager(Deathswap plugin) {
        this.games = new ArrayList<Game>();
        this.plugin = plugin;
    }

    public void create(Player player) {
        for (Game game : this.games) {
            if (game.findPlayer(player)) {
                player.sendMessage(ChatUtils.color("&3Jesteś już w gre aby zakonczyc gre uzyj ./wyjdz"));
                return;
            }
            if (game.owner.equals(player))
                player.sendMessage(ChatUtils.color("&3Jesteś hostem tej gry aby zakonczyc gre uzyj ./wyjdz"));
            return;
        }

        // We can create a game
        Game newGame = new Game(player, this);
        this.games.add(newGame);
        player.sendMessage(ChatUtils.color("&3Utworzono gre!"));
    }

    public void start(Player player) {
        for (Game game : this.games) {
            if (game.owner.equals(player) || game.findPlayer(player)) {
                game.start();
                return;
            }
        }

    }

    public void join(Player player, String gameId) {
        Game gameToJoin = null;

        for (Game game : this.games) {
            if (game.findPlayer(player)) {
                player.sendMessage(ChatUtils.color("&5&lJesteś już w grze! Aby wyjsc z areny uzyj ./wyjdz"));
                return;
            }
            if (game.id.equalsIgnoreCase(gameId)) {
                gameToJoin = game;
            }
        }

        if (gameToJoin != null) {
            gameToJoin.join(player);
            player.sendMessage(ChatUtils.color("&5&lDolaczyles do gry! o kodzie " + gameId));
            return;
        }

        player.sendMessage(ChatUtils.color("&5&lNie moge znalesc tej gry Sprawdz dokladnie kod :("));
    }

    public void addPlayer(Player owner, String playerName) {
        Game ownersGame = null;
        if (!owner.hasPermission("Deathswap.addPlayer")) {
            owner.sendMessage(ChatUtils.color("&cBrak uprawnien!"));
        }
        for (Game game : this.games) {
            if (game.owner.equals(owner)) {
                ownersGame = game;
                break;
            }
        }

        if (ownersGame == null) {
            owner.sendMessage(ChatUtils.color("&a&lStworz najpierw gre uzywajac ./stworz"));
        }

        Player player = null;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getDisplayName().equalsIgnoreCase(playerName)) {
                player = p;
                break;
            }
        }

        if (player == null) {
            owner.sendMessage(ChatUtils.color("&4&lNie ma takiej osoby na serwerze."));
            return;
        }

        ownersGame.join(player);
    }

    public void end(Player player) {
        for (Game game : this.games) {
            if (game.findPlayer(player)) {
                game.end();
                games.remove(game);
                return;
            }
        }

        player.sendMessage(ChatUtils.color("&2&lSkonczyles wlasnie gre! Aby utworzyc nowa gre wpisz ./stworz"));
    }

    public void end(Game game) {
        games.remove(game);
    }

    public void handleDeath(Player player) {
        for (Game game : this.games) {
            if (game.findPlayer(player)) {
                game.deadPlayer(player);
                return;
            }
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void handleQuit(Player player) {
        for (Game game : this.games) {
            if (game.findPlayer(player)) {
                game.deadPlayer(player);
                return;
            }
        }
    }

}

