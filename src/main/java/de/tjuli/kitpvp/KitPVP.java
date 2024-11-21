package de.tjuli.kitpvp;

import de.tjuli.kitpvp.commands.CommandManager;
import de.tjuli.kitpvp.editor.KitEditor;
import de.tjuli.kitpvp.files.ConfigManager;
import de.tjuli.kitpvp.game.GameManager;
import de.tjuli.kitpvp.kit.KitManager;
import de.tjuli.kitpvp.queueSystem.NPC;
import de.tjuli.kitpvp.queueSystem.QueueSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

public final class KitPVP extends JavaPlugin {
    public static KitPVP INSTANCE;

    private ConfigManager configManager;
    private KitManager kitManager;
    private QueueSystem queueSystem;
    private GameManager gameManager;
    private KitEditor kitEditor;
    private NPC npc;

    @Override
    public void onEnable() {
        INSTANCE = this;
        configManager = new ConfigManager(this);
        configManager.setup();
        kitManager = new KitManager(this);
        kitManager.setup();
        queueSystem = new QueueSystem();
        gameManager = new GameManager(this);
        kitEditor = new KitEditor(this);
        npc = new NPC(this);

        getCommand("kitpvp").setExecutor(new CommandManager(this));

    }

    @Override
    public void onDisable() {
        configManager.shutdown();
        kitManager.shutdown();
        npc.killNPC();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
    public KitManager getKitManager() {return kitManager;}

    public QueueSystem getQueueSystem() {
        return queueSystem;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public NPC getNpc() {
        return npc;
    }

    public KitEditor getKitEditor() {
        return kitEditor;
    }
}

