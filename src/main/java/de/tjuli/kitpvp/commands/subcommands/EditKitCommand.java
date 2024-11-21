package de.tjuli.kitpvp.commands.subcommands;


import de.tjuli.kitpvp.KitPVP;
import de.tjuli.kitpvp.commands.SubCommand;
import de.tjuli.kitpvp.enums.MsgType;
import de.tjuli.kitpvp.enums.Permissions;
import de.tjuli.kitpvp.kit.Kit;
import de.tjuli.kitpvp.kit.KitManager;
import org.bukkit.entity.Player;


public class EditKitCommand extends SubCommand {

    private final KitPVP plugin;

    public EditKitCommand(KitPVP plugin) {
        this.plugin = plugin;
    }

    @Override
    protected String getName() {
        return "editKit";
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
        return Permissions.EDIT_KIT.getPermission();
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
        kit.loadKit(sender);
        KitPVP.INSTANCE.getKitEditor().getEditor(sender).setKit(kit);
        sender.sendMessage(MsgType.EDIT_KIT.getMessage().replaceAll("%kit%", kitName));

    }


}