package de.tjuli.kitpvp.kit;

import de.tjuli.kitpvp.KitPVP;
import de.tjuli.kitpvp.enums.MsgType;
import de.tjuli.kitpvp.enums.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KitGUI implements Listener {
    private final Plugin plugin;
    private final KitManager kitManager;

    public KitGUI(Plugin plugin, KitManager kitManager) {
        this.plugin = plugin;
        this.kitManager = kitManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void openGUI(Player player) {
        List<Kit> kits = kitManager.getKits(player.getUniqueId());
        int rows = (int) Math.ceil(kits.size() / 9.0);

        Inventory gui = Bukkit.createInventory(null, rows * 9, ChatColor.BLUE + "Kits");


        fillGUI(gui, player.getUniqueId());
        player.openInventory(gui);
    }

    private void fillGUI(Inventory gui, UUID owner) {
        List<Kit> kits = kitManager.getKits(owner);

        for (int i = 0; i < kits.size(); i++) {
            Kit kit = kits.get(i);
            if(kit.getOwner() == owner) {
            ItemStack kitItem = createKitItem(kit);
            gui.setItem(i, kitItem);
            }
        }
    }

    private ItemStack createKitItem(Kit kit) {
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(ChatColor.GREEN + kit.getName());

        List<String> lore = KitPVP.INSTANCE.getConfigManager().getLanguage().getStringList("kit_gui_item_lore");
        itemMeta.setLore(lore);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.BLUE + "Kits") && event.getWhoClicked().hasPermission(Permissions.OPEN_GUI.getPermission())) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                Kit kit = getKitFromItem(clickedItem, player.getUniqueId());
                if (kit != null) {
                    if (event.isLeftClick()) {
                        kit.loadKit(player);
                        KitPVP.INSTANCE.getKitEditor().getEditor(player).setKit(kit);
                        player.sendMessage(MsgType.EDIT_KIT.getMessage().replaceAll("%kit%", kit.getName()));
                        player.closeInventory();
                    }
                    else if (event.isShiftClick()) {
                        kitManager.deleteKit(kit.getName(), player.getUniqueId());
                        player.sendMessage(MsgType.DELETE_KIT.getMessage().replaceAll("%kit%", kit.getName()));
                        openGUI(player);
                    }
                }
            }
        }
    }


    private Kit getKitFromItem(ItemStack itemStack, UUID uuid) {
        String kitName = ChatColor.stripColor(itemStack.getItemMeta().getDisplayName());
        return kitManager.getKitByName(kitName, uuid);
    }

}
