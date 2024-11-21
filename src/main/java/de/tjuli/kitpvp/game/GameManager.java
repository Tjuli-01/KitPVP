package de.tjuli.kitpvp.game;

import de.tjuli.kitpvp.KitPVP;
import de.tjuli.kitpvp.enums.MsgType;
import de.tjuli.kitpvp.files.ConfigManager;
import de.tjuli.kitpvp.kit.Kit;
import de.tjuli.kitpvp.kit.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameManager implements Listener {

    private final JavaPlugin plugin;
    private List<Game> activeGames;

    public GameManager(KitPVP plugin) {
        this.plugin = plugin;
        this.activeGames = new ArrayList<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //TODO add a method to generate a world for the game
    public void startGame(Player player1, Player player2) {
        KitManager kitManager = KitPVP.INSTANCE.getKitManager();
        List<Kit> kits = kitManager.getKits(player1.getUniqueId());
        kits.addAll(kitManager.getKits(player2.getUniqueId()));
        if (!kits.isEmpty()) {
            Random random = new Random();
            Kit kit = kits.get(random.nextInt(kits.size()));


            //Player 1
            kit.loadKit(player1);
            player1.setGameMode(GameMode.SURVIVAL);
            player1.setHealth(20.0D);
            player1.setFoodLevel(20);
            player1.setSaturation(20.0F);

            //Player 2
            kit.loadKit(player2);
            player2.setGameMode(GameMode.SURVIVAL);
            player2.setHealth(20.0D);
            player2.setFoodLevel(20);
            player2.setSaturation(20.0F);

            Game game = new Game(kit, player1, player2);
            activeGames.add(game);

            player1.sendMessage(MsgType.GAME_STARTING.getMessage().replaceAll("%opponent%", player2.getDisplayName()).replaceAll("%kit%", kit.getName()));
            player2.sendMessage(MsgType.GAME_STARTING.getMessage().replaceAll("%opponent%", player1.getDisplayName()).replaceAll("%kit%", kit.getName()));
        }
    }

    public void endGame(Player winner) {
        Game game = getGameByPlayer(winner);
        if (game != null) {
            Player loser = getOpponent(winner);
            activeGames.remove(game);
            winner.getInventory().clear();
            loser.getInventory().clear();
            teleportToSpawn(winner);
            teleportToSpawn(loser);
            ConfigManager configManager = KitPVP.INSTANCE.getConfigManager();
            if(configManager.getConfig().getBoolean("broadcast_wins")) {
            Bukkit.broadcastMessage(MsgType.GAME_END.getMessage().replaceAll("%winner%", winner.getDisplayName()).replaceAll("%loser%", loser.getDisplayName()).replaceAll("%kit%", game.getKit().getName()));
            }
            winner.sendMessage(MsgType.GAME_WIN.getMessage().replaceAll("%opponent%", loser.getDisplayName()).replaceAll("%kit%", game.getKit().getName()));
            loser.sendMessage(MsgType.GAME_LOSE.getMessage().replaceAll("%opponent%", winner.getDisplayName()).replaceAll("%kit%", game.getKit().getName()));
        }
    }

    public boolean isInGame(Player player) {
        return getGameByPlayer(player) != null;
    }

    public Game getGameByPlayer(Player player) {
        for (Game game : activeGames) {
            if (game.getPlayer1().equals(player) || game.getPlayer2().equals(player)) {
                return game;
            }
        }
        return null;
    }

    public Player getOpponent(Player player) {
        Game game = getGameByPlayer(player);
        if(game == null) {
            return null;
        }

        if (game.getPlayer1().equals(player)) {
            return game.getPlayer2();
        } else if (game.getPlayer2().equals(player)) {
            return game.getPlayer1();
        }
        return null;
    }

    private void teleportToSpawn(Player player) {
        // Get the spawn location from the config
        Location spawnLocation = (Location) plugin.getConfig().get("spawn_location");
        if (spawnLocation != null) {
            player.teleport(spawnLocation);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (isInGame(player)) {
            endGame(getOpponent(player));
        }
        event.setQuitMessage("");
        //Remove player from the queue
        KitPVP.INSTANCE.getQueueSystem().removePlayer(player);
    }
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (isInGame(player)) {
                double remainingHealth = player.getHealth() - event.getFinalDamage();
                if (remainingHealth <= 0.0) {
                    event.setCancelled(true);
                    player.setHealth(20.0);
                    endGame(getOpponent(player));
                }
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player target = (Player) event.getEntity();
            Player attacker = (Player) event.getDamager();

            if(target != getOpponent(attacker)) {
                event.setCancelled(true);
                attacker.sendMessage(MsgType.CAN_NOT_ATTACK.getMessage());
            }
        }
    }
}
