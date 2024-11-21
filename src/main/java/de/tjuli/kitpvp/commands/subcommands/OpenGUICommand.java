package de.tjuli.kitpvp.commands.subcommands;


import de.tjuli.kitpvp.KitPVP;
import de.tjuli.kitpvp.commands.SubCommand;
import de.tjuli.kitpvp.enums.Permissions;
import de.tjuli.kitpvp.kit.KitGUI;
import org.bukkit.entity.Player;


public class OpenGUICommand extends SubCommand {

    private final KitPVP plugin;

    public OpenGUICommand(KitPVP plugin) {
        this.plugin = plugin;
    }

    @Override
    protected String getName() {
        return "openGUI";
    }

    @Override
    protected int minArgs() {
        return 0;
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
        return Permissions.OPEN_GUI.getPermission();
    }

    @Override
    protected void execute(Player sender, String[] args) {
        KitGUI kitGUI = new KitGUI(plugin, plugin.getKitManager());
        kitGUI.openGUI(sender);
    }



}