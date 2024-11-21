package de.tjuli.kitpvp.game;

import de.tjuli.kitpvp.kit.Kit;
import org.bukkit.entity.Player;

public class Game {
    Kit kit;
    Player player1;
    Player player2;
    public Game(Kit kit, Player player1, Player player2) {
        this.kit = kit;
        this.player1 = player1;
        this.player2 = player2;
    }

    public Kit getKit() {
        return kit;
    }
    public Player getPlayer1() {
        return player1;
    }
    public Player getPlayer2() {
        return player2;
    }
}
