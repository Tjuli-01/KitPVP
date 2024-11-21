package de.tjuli.kitpvp.editor;

import de.tjuli.kitpvp.KitPVP;
import de.tjuli.kitpvp.kit.Kit;
import de.tjuli.kitpvp.kit.KitManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class KitEditor implements Listener {
    private JavaPlugin plugin;
    private World regionWorld;
    private int minX, minZ, maxX, maxZ;
    private Map<Player, Editor> editorMap;
    private KitManager kitManager;

    public KitEditor(KitPVP plugin) {
        this.plugin = plugin;
        loadRegionCoordinates();
        editorMap = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void loadRegionCoordinates() {
        regionWorld = Bukkit.getWorld(KitPVP.INSTANCE.getConfigManager().getConfig().getString("kit_editor.world"));
        minX = KitPVP.INSTANCE.getConfigManager().getConfig().getInt("kit_editor.min_x");
        minZ = KitPVP.INSTANCE.getConfigManager().getConfig().getInt("kit_editor.min_z");
        maxX = KitPVP.INSTANCE.getConfigManager().getConfig().getInt("kit_editor.max_x");
        maxZ = KitPVP.INSTANCE.getConfigManager().getConfig().getInt("kit_editor.max_z");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        kitManager = KitPVP.INSTANCE.getKitManager();

        if (location.getWorld() != regionWorld) {
            setEditorMode(player, null);
            return;
        }

        if (location.getBlockX() <= minX || location.getBlockX() >= maxX || location.getBlockZ() <= minZ || location.getBlockZ() >= maxZ) {
            if (isEditorModeEnabled(player)) {
                setEditorMode(player, null);
            }
        } else {
            if (!isEditorModeEnabled(player)) {
                player.getInventory().clear();
                Kit kit = kitManager.getKitByName(player.getName(), player.getUniqueId());
                if(kit == null) {
                    kit = new Kit(player.getUniqueId(), player.getName(), null, null);
                } else {
                kit.loadKit(player);}
                setEditorMode(player, new Editor(kit));
            }
        }
    }

    private void setEditorMode(Player player, Editor editor) {
        if (editor == null) {
            Editor editor1 = editorMap.get(player);
            kitManager.createKit(editor1.getKit().getOwner(), editor1.getKit().getName(), player.getInventory().getContents(), player.getInventory().getArmorContents());
            editorMap.remove(player);
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.setGameMode(GameMode.ADVENTURE);
        } else {
            editorMap.put(player, editor);
            player.setGameMode(GameMode.CREATIVE);


        }
    }

    public boolean isEditorModeEnabled(Player player) {
        return editorMap.containsKey(player);
    }

    public Editor getEditor(Player player) {
        return editorMap.get(player);
    }
}
