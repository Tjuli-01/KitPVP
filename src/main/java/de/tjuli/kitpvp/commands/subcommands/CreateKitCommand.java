package de.tjuli.kitpvp.commands.subcommands;


import de.tjuli.kitpvp.KitPVP;
import de.tjuli.kitpvp.commands.SubCommand;
import de.tjuli.kitpvp.editor.KitEditor;
import de.tjuli.kitpvp.enums.MsgType;
import de.tjuli.kitpvp.enums.Permissions;
import de.tjuli.kitpvp.kit.Kit;
import de.tjuli.kitpvp.kit.KitManager;
import org.bukkit.entity.Player;


public class CreateKitCommand extends SubCommand {

    private final KitPVP plugin;

    public CreateKitCommand(KitPVP plugin) {
        this.plugin = plugin;
    }

    @Override
    protected String getName() {
        return "createKit";
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
        return true;
    }


    @Override
    protected String getPermission() {
        return Permissions.CREATE_KIT.getPermission();
    }

    @Override
    protected void execute(Player sender, String[] args) {
        KitManager kitManager = KitPVP.INSTANCE.getKitManager();
        KitEditor kitEditor = KitPVP.INSTANCE.getKitEditor();
        String kitName = args[1];
        Kit kit = kitManager.getKitByName(kitName, sender.getUniqueId());
        if(kit != null) {
            sender.sendMessage(MsgType.KIT_ALREADY_EXISTS.getMessage().replaceAll("%kit%", kitName));
            return;
        }


        kitManager.createKit(sender.getUniqueId(), kitName, sender.getInventory().getContents(), sender.getInventory().getArmorContents());
        Kit kit1 = new Kit(sender.getUniqueId(), kitName, sender.getInventory().getContents(), sender.getInventory().getArmorContents());
        kitEditor.getEditor(sender).setKit(kit1);
        sender.sendMessage(MsgType.CREATE_KIT.getMessage().replaceAll("%kit%", kitName));
    }



}