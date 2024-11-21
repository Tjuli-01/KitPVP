package de.tjuli.kitpvp.queueSystem;

import de.tjuli.kitpvp.KitPVP;
import de.tjuli.kitpvp.enums.MsgType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class QueueSystem {
    private List<Player> queue;

    public QueueSystem() {
        queue = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        if (!queue.contains(player) && !KitPVP.INSTANCE.getGameManager().isInGame(player)) {
            queue.add(player);
            player.sendMessage(MsgType.JOIN_QUEUE.getMessage());
            checkStartGame();
        }
    }

    public void removePlayer(Player player) {
        if (queue.contains(player)) {
            queue.remove(player);
            player.sendMessage(MsgType.LEAVE_QUEUE.getMessage());
        }
    }

    private void checkStartGame() {
        if (queue.size() >= 2) {
            Player player1 = queue.get(0);
            Player player2 = queue.get(1);
            queue.remove(player1);
            queue.remove(player2);
            KitPVP.INSTANCE.getGameManager().startGame(player1, player2);

        }
    }

    public boolean isInQueue(Player player) {
        for (Player p : queue) {
            if (p == player) {
                return true;
            }
        }
        return false;
    }
}
