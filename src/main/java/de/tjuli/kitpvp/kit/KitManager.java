package de.tjuli.kitpvp.kit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class KitManager {

    private final JavaPlugin plugin;
    private File baseDirectory;
    private List<FileConfiguration> kitFiles;
    private Map<UUID, List<Kit>> kitsMap;

    public KitManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        baseDirectory = plugin.getDataFolder();
        kitFiles = new ArrayList<>();
        kitsMap = new HashMap<>();

        // Load kits for existing players
        loadKitsForExistingPlayers();

        // Register the listener
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), plugin);
    }

    public void shutdown() {
        saveKits();
        baseDirectory = null;
        kitFiles = null;
        kitsMap = null;
    }

    private void loadKitsForExistingPlayers() {
        File kitsFolder = new File(baseDirectory, "kits");
        if (!kitsFolder.exists()) {
            kitsFolder.mkdirs();
        }

        File[] kitFolders = kitsFolder.listFiles();
        if (kitFolders != null) {
            for (File kitFolder : kitFolders) {
                if (kitFolder.isDirectory()) {
                    UUID owner = UUID.fromString(kitFolder.getName());
                    loadKitsForPlayer(owner);
                }
            }
        }
    }

    private void loadKitsForPlayer(UUID owner) {
        File kitFolder = new File(baseDirectory + File.separator + "kits" + File.separator + owner.toString());
        if (!kitFolder.exists()) {
            kitFolder.mkdirs();
        }

        List<Kit> kits = new ArrayList<>();

        File[] kitFiles = kitFolder.listFiles();
        if (kitFiles != null) {
            for (File kitFile : kitFiles) {
                if (kitFile.isFile()) {
                    FileConfiguration kitConfig = YamlConfiguration.loadConfiguration(kitFile);
                    this.kitFiles.add(kitConfig);
                    Kit kit = new Kit(
                            owner,
                            kitConfig.getString("name"),
                            Objects.requireNonNull(kitConfig.getList("items")).toArray(new ItemStack[0]),
                            Objects.requireNonNull(kitConfig.getList("armor")).toArray(new ItemStack[0])
                    );
                    kits.add(kit);
                }
            }
        }

        kitsMap.put(owner, kits);
    }

    private void saveKits() {
        File kitsFolder = new File(baseDirectory, "kits");
        if (!kitsFolder.exists()) {
            kitsFolder.mkdirs();
        }

        for (Map.Entry<UUID, List<Kit>> entry : kitsMap.entrySet()) {
            UUID owner = entry.getKey();
            List<Kit> kits = entry.getValue();

            File kitFolder = new File(kitsFolder, owner.toString());
            if (!kitFolder.exists()) {
                kitFolder.mkdirs();
            }

            for (Kit kit : kits) {
                String kitName = kit.getName();
                File kitFile = new File(kitFolder, kitName + ".yml");
                FileConfiguration kitConfig = YamlConfiguration.loadConfiguration(kitFile);
                kitConfig.set("name", kitName);
                kitConfig.set("items", kit.getItems());
                kitConfig.set("armor", kit.getArmor());

                try {
                    kitConfig.save(kitFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public FileConfiguration getKitFileByName(String name) {
        for (FileConfiguration file : kitFiles) {
            if (file.getString("name").equals(name)) {
                return file;
            }
        }
        return null;
    }

    public List<FileConfiguration> getKitFiles() {
        return kitFiles;
    }

    public Kit getKitByName(String name, UUID ownerId) {
        List<Kit> kits = kitsMap.get(ownerId);
        if (kits != null) {
            for (Kit kit : kits) {
                if (kit.getName().equals(name)) {
                    return kit;
                }
            }
        }
        return null;
    }

    public List<Kit> getKits(UUID ownerId) {
        return kitsMap.getOrDefault(ownerId, new ArrayList<>());
    }

    public void createKit(UUID owner, String name, ItemStack[] items, ItemStack[] kitArmor) {
        List<Kit> kits = kitsMap.getOrDefault(owner, new ArrayList<>());

        File kitFolder = new File(baseDirectory + File.separator + "kits" + File.separator + owner.toString());
        if (!kitFolder.exists()) {
            kitFolder.mkdirs();
        }
        File kitFile = new File(kitFolder, name + ".yml");
        FileConfiguration kitConfig = YamlConfiguration.loadConfiguration(kitFile);
        kitConfig.set("name", name);
        kitConfig.set("items", items);
        kitConfig.set("armor", kitArmor);

        try {
            kitConfig.save(kitFile);

            Kit existingKit = getKitByName(name, owner);
            if (existingKit != null) {
                kits.remove(existingKit);
            }

            Kit newKit = new Kit(owner, name, items, kitArmor);
            kits.add(newKit);

            kitsMap.put(owner, kits);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteKit(String name, UUID ownerId) {
        List<Kit> kits = kitsMap.get(ownerId);
        if (kits != null) {
            Kit kitToRemove = getKitByName(name, ownerId);
            if (kitToRemove != null) {
                kits.remove(kitToRemove);

                FileConfiguration kitConfigToRemove = getKitFileByName(name);
                if (kitConfigToRemove != null) {
                    File kitFolder = new File(baseDirectory + File.separator + "kits" + File.separator + ownerId);
                    File kitFile = new File(kitFolder, name + ".yml");
                    if (kitFile.exists()) {
                        kitFile.delete();
                    }
                    kitFiles.remove(kitConfigToRemove);
                }
            }
        }
    }

    private class PlayerJoinListener implements Listener {

        @EventHandler
        public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
            UUID playerId = event.getPlayer().getUniqueId();
            loadKitsForPlayer(playerId);
        }
    }
}
