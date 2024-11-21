package de.tjuli.kitpvp.commands.subcommands;


import de.tjuli.kitpvp.KitPVP;
import de.tjuli.kitpvp.commands.SubCommand;
import de.tjuli.kitpvp.enums.MsgType;
import de.tjuli.kitpvp.enums.Permissions;
import de.tjuli.kitpvp.kit.Kit;
import de.tjuli.kitpvp.kit.KitManager;
import org.bukkit.entity.Player;


public class DeleteKitCommand extends SubCommand {

    private final KitPVP plugin;

    public DeleteKitCommand(KitPVP plugin) {
        this.plugin = plugin;
    }

    @Override
    protected String getName() {
        return "deleteKit";
    }

    @Override
    protected int minArgs() {
        return 1;
    }

    @Override
    protected boolean tabKits() {
        return true;
    }

    @Override
    protected boolean needsEditorMode() {
        return true;
    }


    @Override
    protected String getPermission() {
        return Permissions.DELETE_KIT.getPermission();
    }

    @Override
    protected void execute(Player sender, String[] args) {
        KitManager kitManager = KitPVP.INSTANCE.getKitManager();
        String kitName = args[1];
        Kit kit = kitManager.getKitByName(kitName, sender.getUniqueId());
        if(kit == null) {
            sender.sendMessage(MsgType.KIT_NOT_FOUND.getMessage().replaceAll("%kit%", kitName));
            return;
        }

        kitManager.deleteKit(kitName, sender.getUniqueId());

        sender.sendMessage(MsgType.DELETE_KIT.getMessage().replaceAll("%kit%", kitName));
    }



}