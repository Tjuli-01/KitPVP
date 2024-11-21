package de.tjuli.kitpvp.files;

import de.tjuli.kitpvp.KitPVP;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {


    private final JavaPlugin plugin;
    private File baseDirectory;
    private FileConfiguration config;
    private FileConfiguration language;
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        baseDirectory = plugin.getDataFolder();
        loadConfig();
        setDefaults();

    }

    public void shutdown() {
        saveFile(config,  "config");
        saveFile(language,  "language");
        baseDirectory = null;
        config = null;
        language = null;
    }

    private void loadConfig() {
        config = getFile( "config");
        language = getFile("language");
    }


    private FileConfiguration getFile(String fileName) {
        try {
            File configFile = new File(baseDirectory,fileName + ".yml");
            if (!configFile.exists()) {
                try {
                    configFile.createNewFile();
                    plugin.saveResource(fileName + ".yml", false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return YamlConfiguration.loadConfiguration(configFile);
        } catch (IllegalArgumentException e) {
            //Too long that I didn't work with that code, and it there must be a reason for that so i wont touch it
            return getFile(fileName);
        }

    }


    private void saveFile(FileConfiguration fileConfiguration, String fileName) {


        File configFile = new File(baseDirectory,fileName + ".yml");
        try {
            fileConfiguration.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDefaults() {
        //Config

        if(config.get("prefix") == null) {config.set("prefix", "§b§l[KitPVP] §r");}
        if(config.get("broadcast_wins") == null) {config.set("broadcast_wins", true);}
        if(config.get("spawn_location") == null) {config.set("spawn_location", Bukkit.getWorld("world").getSpawnLocation());}
        if(config.get("NPC.spawned") == null) {config.set("NPC.spawned", false);}
        if(config.get("NPC.name") == null) {config.set("NPC.name", "Join-Queue");}
        if(config.get("NPC.location") == null) {config.set("NPC.location", new Location(Bukkit.getWorld("world"), 0, 0, 0));}
        if(config.get("kit_editor.world") == null) {config.set("kit_editor.world", "world");}
        if(config.get("kit_editor.min_x") == null) {config.set("kit_editor.min_x", 0);}
        if(config.get("kit_editor.min_z") == null) {config.set("kit_editor.min_z", 0);}
        if(config.get("kit_editor.max_x") == null) {config.set("kit_editor.max_x", 0);}
        if(config.get("kit_editor.max_z") == null) {config.set("kit_editor.max_z", 0);}
        //Language
        if(language.get("no_perm") == null) {language.set("no_perm", "§cYou don't have the Permission to do that!");}
        if(language.get("no_console") == null) {language.set("no_console", "You can't execute this command in the console!");}
        if(language.get("invalid_syntax") == null) {language.set("invalid_syntax", "§cInvalid syntax");}
        if(language.get("create_kit") == null) {language.set("create_kit", "§aCreated the kit §6%kit%");}
        if(language.get("delete_kit") == null) {language.set("delete_kit", "§cDeleted the kit §6%kit%");}
        if(language.get("edit_kit") == null) {language.set("edit_kit", "§aEditing the kit §6%kit%");}
        if(language.get("kit_not_found") == null) {language.set("kit_not_found", "§cThe kit §6%kit% §cdoes not exist");}
        if(language.get("kit_already_exists") == null) {language.set("kit_already_exists", "§cThe kit §6%kit% §calready exists");}
        if(language.get("join_queue") == null) {language.set("join_queue", "§aYou joined the queue");}
        if(language.get("leave_queue") == null) {language.set("leave_queue", "§cYou left the queue");}
        if(language.get("game_starting") == null) {language.set("game_starting", "§7You are fighting against §a%opponent% §7with the kit §a%kit%");}
        if(language.get("game_end") == null) {language.set("game_end", "§a%winner% §7won against §a%loser% §7with the Kit §a%kit%");}
        if(language.get("game_win") == null) {language.set("game_win", "§7You §awon §7against §a%opponent% §7with the Kit §a%kit%");}
        if(language.get("game_lose") == null) {language.set("game_lose", "§7You §clost §7against §c%opponent% §7with the Kit §a%kit%");}
        if(language.get("can_not_attack") == null) {language.set("can_not_attack", "§cYou can't attack this player");}
        if(language.get("not_in_editor_mode") == null) {language.set("not_in_editor_mode", "§cYou must be in editor mode to execute this command");}


        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Left-Click to edit");
        lore.add(ChatColor.GRAY + "Shift-Right-Click to delete");
        if(language.get("kit_gui_item_lore") == null) {language.set("kit_gui_item_lore", lore);}

        saveFile(config,  "config");
        saveFile(language,  "language");



    }



    public FileConfiguration getConfig() {
        return config;
    }
    public FileConfiguration getLanguage() {
        return language;
    }

}

