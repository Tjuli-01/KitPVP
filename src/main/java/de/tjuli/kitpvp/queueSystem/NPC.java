package de.tjuli.kitpvp.queueSystem;

import de.tjuli.kitpvp.KitPVP;
import de.tjuli.kitpvp.files.ConfigManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class NPC implements Listener {
    private KitPVP plugin;
    private Villager npc;
    private Location npcLocation;
    private boolean spawned = false;

    public NPC(KitPVP kitPVP) {
        this.plugin = kitPVP;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        freezeNPC();
        ConfigManager configManager = KitPVP.INSTANCE.getConfigManager();
        if(configManager.getConfig().getBoolean("NPC.spawned")) {
            String name = configManager.getConfig().getString("NPC.name");
            Location location = (Location) configManager.getConfig().get("NPC.location");

            spawnNPC(name, location.getWorld(), location.getX(), location.getY(), location.getZ());
        }

    }

    public void spawnNPC(String npcName, World world, double x, double y, double z) {
        if (npc != null) {
            killNPC();
        }
        npcLocation = new Location(world, x, y, z);
        npc = (Villager) world.spawnEntity(npcLocation, EntityType.VILLAGER);
        npc.setCustomName(npcName);
        npc.setCustomNameVisible(true);
        spawned = true;

        saveToConfig(npcName, npcLocation, true);

    }

    public void killNPC() {

        if (npc != null) {
            npc.setHealth(0.0D);
            npc.remove();
            npc = null;
        }
        spawned = false;

    }

    //Yes, I know, this Runnable is going to run pretty often
    private void freezeNPC() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (spawned && npc.getLocation() != npcLocation) {
                    npc.teleport(npcLocation);
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private void saveToConfig(String name, Location location, boolean spawned ) {
        ConfigManager configManager = KitPVP.INSTANCE.getConfigManager();
        configManager.getConfig().set("NPC.spawned", spawned);
        configManager.getConfig().set("NPC.name", name);
        configManager.getConfig().set("NPC.location", location);

    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!spawned) {
            return;
        }

        QueueSystem queueSystem = KitPVP.INSTANCE.getQueueSystem();
        if (event.getRightClicked() instanceof Villager) {
            Villager villager = (Villager) event.getRightClicked();
            Player player = event.getPlayer();
            if (villager == npc) {
                if (queueSystem.isInQueue(player)) {
                    queueSystem.removePlayer(player);
                } else {
                    queueSystem.addPlayer(player);
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!spawned) {
            return;
        }

        QueueSystem queueSystem = KitPVP.INSTANCE.getQueueSystem();
        if (event.getEntity() instanceof Villager && event.getDamager() instanceof Player) {
            Villager villager = (Villager) event.getEntity();
            Player player = (Player) event.getDamager();
            if (villager == npc) {
                if (queueSystem.isInQueue(player)) {
                    queueSystem.removePlayer(player);
                } else {
                    queueSystem.addPlayer(player);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageEvent event) {
        if (!spawned) {
            return;
        }
        if (event.getEntity() instanceof Villager) {
            Villager villager = (Villager) event.getEntity();
            if (villager
                    == npc) {
                event.setCancelled(true);
            }
        }
    }

}

