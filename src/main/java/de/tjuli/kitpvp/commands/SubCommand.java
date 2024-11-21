package de.tjuli.kitpvp.commands;


import org.bukkit.entity.Player;

public abstract class SubCommand {


    protected abstract String getName();
    protected abstract int minArgs();
    protected abstract boolean tabKits();
    protected abstract boolean needsEditorMode();

    protected abstract String getPermission();
    protected abstract void execute(Player sender, String[] args);
}