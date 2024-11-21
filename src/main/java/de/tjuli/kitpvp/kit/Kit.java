package de.tjuli.kitpvp.kit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;


public class Kit {
    private final UUID owner;
    private final String kitName;
    private final ItemStack[] kitItems;
    private final ItemStack[] kitArmor;


    public Kit(UUID owner, String kitName, ItemStack[] kitItems, ItemStack[] kitArmor) {
        this.owner = owner;
        this.kitName =  kitName;
        this.kitItems = kitItems;
        this.kitArmor = kitArmor;
    }

    public String getName() {
        return kitName;
    }
    public ItemStack[] getItems() {
        return kitItems;
    }
    public ItemStack[] getArmor() {
        return kitArmor;
    }


    public void loadKit(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.getInventory().setContents(kitItems);
        player.getInventory().setArmorContents(kitArmor);
    }

    public UUID getOwner() {
        return owner;
    }
}
