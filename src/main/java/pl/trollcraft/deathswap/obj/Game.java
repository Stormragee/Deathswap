package pl.trollcraft.deathswap.obj;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pl.trollcraft.deathswap.chatutils.ChatUtils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
    private BukkitTask swapTimer = null;
    private final ArrayList<Player> players;
    private final ArrayList<Player> deadPlayers;
    private final GameManager gameManager;
    //Settings
    private int minSwapTime = 30;
    private int maxSwapTime = 120;
    private int swapRadius = 400;
    private Location startLocation;
    public String id;
    public Player owner;

    public Game(Player owner, GameManager gameManager) {
        this.owner = owner;
        this.gameManager = gameManager;
        this.players = new ArrayList<Player>();
        this.deadPlayers = new ArrayList<Player>();
        this.swapRadius = getRandomRadius(400, 1200);
        this.players.add(owner);
        this.id = IDGenerator.random(3);
        owner.sendMessage(ChatUtils.color("&4Gra utworzona! Zapros znajomych lub daj znac na chacie ze szukasz graczy ;) Kod gry:") + this.id);
    }


    public void start() {
        if (this.players.size() == 1) {
            this.owner.sendMessage(ChatUtils.color("&3Nie mozesz zaczac gry samemu :("));
            return;
        }
        this.startLocation = this.owner.getLocation();

        resetPlayers();
        randomizeLocations();
        sendCountdown();
    }

    public void join(Player player) {
        this.players.add(player);
    }

    public void end() {
        for (Player player : this.players) {
            player.setGameMode(GameMode.SURVIVAL);
        }
        this.gameManager.end(this);
    }

    public boolean findPlayer(Player player) {
        for (Player p : this.players) {
            if (p.equals(player)) {
                return true;
            }
        }
        return false;
    }

    public void deadPlayer(Player player) {
        this.deadPlayers.add(player);
        player.teleport(this.startLocation);
        if (this.deadPlayers.size() >= this.players.size() - 1) {
            Player winner = null;
            for (Player p : this.players) {
                if (!this.deadPlayers.contains(p)) {
                    winner = p;
                    p.teleport(this.startLocation);
                    broadcastTitle(ChatUtils.color("&l&1" + winner.getName() + " wygrał deathswapa!"), "", 10, 70, 20);
                    end();
                }
            }
        } else {
            player.sendTitle(ChatUtils.color("&3&lPrzegrales :("), "", 10, 70, 20);
        }
    }

    private void resetPlayers() {
        for (Player player : this.players) {
            player.setHealth(20);
            player.setExhaustion(0);
            player.setFoodLevel(20);
            Collection<PotionEffect> potions = player.getActivePotionEffects();
            for (PotionEffect potion : potions) {
                player.removePotionEffect(potion.getType());
            }

            PlayerInventory inventory = player.getInventory();
            inventory.clear();
        }
    }

    private String startingMessage(int timeLeft) {
        return ChatUtils.color("&3Gra zaczyna się za &2" + timeLeft);
    }

    private void sendCountdown() {
        String title = ChatUtils.color("Gotowi?");

        new BukkitRunnable() {
            int timeLeft = 5;

            @Override
            public void run() {
                if (timeLeft < 1) {
                    broadcastTitle(ChatUtils.color(" &c&l Niech rozpocznie sie gra!"), "", 0, 70, 20);
                    this.cancel();
                    startSwapTimer();
                    return;
                }
                broadcastTitle(title, startingMessage(timeLeft), 0, 25, 0);

                timeLeft--;
            }
        }.runTaskTimer(this.gameManager.getPlugin(), 0L, 20L);
    }

    private void randomizeLocations() {
        Location[] locations = getStartingPoints(this.players.size());
        for (int i = 0; i < locations.length; i++) {
            Player player = this.players.get(i);
            player.teleport(locations[i]);
        }
    }

    /**
     * Gets a list of random Locations for players to start.
     *
     * @param playerCount requires the count of the players.
     * @return locations an array of location types randomized.
     */
    private Location[] getStartingPoints(int playerCount) {
        Point[] points = new Point[playerCount];
        World world = this.owner.getWorld();

        Location origin = getRandomOrigin(swapRadius);
        int x = origin.getBlockX();
        int z = origin.getBlockZ();
        int radius = this.swapRadius;

        double slice = Math.toRadians((360.0 / (float) playerCount) + getRandomAngle());
        for (int i = 0; i < playerCount; i++) {
            double angle = slice * i;

            int newX = x + (int) (radius * Math.cos(angle));
            int newZ = z + (int) (radius * Math.sin(angle));
            //Debug
            this.owner.sendMessage(newX + ", " + newZ);
            points[i] = new Point(newX, newZ);
        }

        // Resolve to block locations
        Location[] locations = new Location[playerCount];
        for (int i = 0; i < points.length; i++) {
            Point point = points[i];
            locations[i] = new Location(world, point.getX(), 80, point.getY());
            locations[i] = world.getHighestBlockAt(locations[i]).getLocation();
        }

        return locations;
    }

    private void startSwapTimer() {
        swapTimer = new BukkitRunnable() {
            int timeLeft = getRandomTime();

            @Override
            public void run() {
                if (timeLeft < 1) {
                    performSwap();
                    this.cancel();
                    startSwapTimer();
                    return;
                }

                timeLeft--;
            }
        }.runTaskTimer(this.gameManager.getPlugin(), 0L, 20L);
    }

    private void performSwap() {
        ArrayList<Player> swappable = new ArrayList<Player>();

        for (Player player : this.players) {
            if (!this.deadPlayers.contains(player)) {
                player.sendTitle(ChatUtils.color(" &b&l Zamiana!"), "", 10, 40, 20);
                swappable.add(player);
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                Player[] players = swappable.toArray(new Player[swappable.size()]);
                Location[] points = new Location[players.length];
                for (int i = 0; i < players.length; i++) {
                    points[i] = players[i].getLocation();
                }

                for (int i = 0; i < players.length; i++) {
                    Player player = players[i];
                    int index = i + 1;
                    if (index == players.length) {
                        index = 0;
                    }

                    player.teleport(points[index]);
                }
            }
        }.runTaskLater(this.gameManager.getPlugin(), 20L);

    }

    private int getRandomRadius(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private int getRandomTime() {
        return ThreadLocalRandom.current().nextInt(minSwapTime, maxSwapTime + 1);
    }

    private double getRandomAngle() {
        int angle = ThreadLocalRandom.current().nextInt(0, 360);
        return Math.toRadians((double) angle);
    }


    private Location getRandomOrigin(int maxDelta) {
        int deltaA = ThreadLocalRandom.current().nextInt(-maxDelta, maxDelta + 1);
        int deltaB = ThreadLocalRandom.current().nextInt(-maxDelta, maxDelta + 1);

        World world = this.owner.getWorld();

        Location origin = this.startLocation;
        Location newOrigin = new Location(world, origin.getBlockX() + deltaA, 0, origin.getBlockZ() + deltaB);

        return world.getHighestBlockAt(newOrigin).getLocation();
    }

    private void broadcastTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        for (Player player : this.players) {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }

}
