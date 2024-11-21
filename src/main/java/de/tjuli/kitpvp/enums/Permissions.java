package de.tjuli.kitpvp.enums;

public enum Permissions {
    ADMIN("kitpvp.admin"),
    CREATE_KIT("kitpvp.create_kit"),
    DELETE_KIT("kitpvp.delete_kit"),
    EDIT_KIT("kitpvp.edit_kit"),
    SPAWN_NPC("kitpvp.spawn_npc"),
    OPEN_GUI("kitpvp.open_gui");

    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}