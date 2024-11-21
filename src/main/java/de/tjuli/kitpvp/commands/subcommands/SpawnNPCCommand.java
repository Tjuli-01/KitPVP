package de.tjuli.kitpvp.commands.subcommands;


import de.tjuli.kitpvp.KitPVP;
import de.tjuli.kitpvp.commands.SubCommand;
import de.tjuli.kitpvp.enums.Permissions;
import de.tjuli.kitpvp.queueSystem.NPC;
import org.bukkit.entity.Player;


public class SpawnNPCCommand extends SubCommand {

    private final KitPVP plugin;

    public SpawnNPCCommand(KitPVP plugin) {
        this.plugin = plugin;
    }

    @Override
    protected String getName() {
        return "spawnNPC";
    }

    @Override
    protected int minArgs() {
        return 1;
    }

    @Override
    protected boolean tabKits() {
        return false;
    }

    @Override
    protected boolean needsEditorMode() {
        return false;
    }


    @Override
    protected String getPermission() {
        return Permissions.SPAWN_NPC.getPermission();
    }

    @Override
    protected void execute(Player sender, String[] args) {
        String npcName = args[1];

        KitPVP.INSTANCE.getNpc().spawnNPC(npcName,sender.getWorld(), sender.getLocation().getBlockX() + .5, sender.getLocation().getBlockY(), sender.getLocation().getBlockZ() +.5);
    }



}