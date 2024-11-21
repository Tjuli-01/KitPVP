package de.tjuli.kitpvp.enums;



import de.tjuli.kitpvp.KitPVP;

public enum MsgType {

    PREFIX(KitPVP.INSTANCE.getConfigManager().getConfig().getString("prefix")),
    NO_PERMISSION(PREFIX.getMessage() + KitPVP.INSTANCE.getConfigManager().getLanguage().getString("no_perm")),
    NO_CONSOLE(PREFIX.getMessage() + KitPVP.INSTANCE.getConfigManager().getLanguage().getString("no_console")),
    INVALID_SYNTAX(PREFIX.getMessage() + KitPVP.INSTANCE.getConfigManager().getLanguage().getString("invalid_syntax")),
    CREATE_KIT(PREFIX.getMessage() + KitPVP.INSTANCE.getConfigManager().getLanguage().getString("create_kit")),
    DELETE_KIT(PREFIX.getMessage() + KitPVP.INSTANCE.getConfigManager().getLanguage().getString("delete_kit")),
    EDIT_KIT(PREFIX.getMessage() + KitPVP.INSTANCE.getConfigManager().getLanguage().getString("edit_kit")),
    KIT_NOT_FOUND(PREFIX.getMessage() + KitPVP.INSTANCE.getConfigManager().getLanguage().getString("kit_not_found")),
    KIT_ALREADY_EXISTS(PREFIX.getMessage() + KitPVP.INSTANCE.getConfigManager().getLanguage().getString("kit_already_exists")),
    JOIN_QUEUE(PREFIX.getMessage() + KitPVP.INSTANCE.getConfigManager().getLanguage().getString("join_queue")),
    LEAVE_QUEUE(PREFIX.getMessage() + KitPVP.INSTANCE.getConfigManager().getLanguage().getString("leave_queue")),
    GAME_STARTING(PREFIX.getMessage() + KitPVP.INSTANCE.getConfigManager().getLanguage().getString("game_starting")),
    GAME_END(PREFIX.getMessage() + KitPVP.INSTANCE.getConfigManager().getLanguage().getString("game_end")),
    GAME_WIN(PREFIX.getMessage() + KitPVP.INSTANCE.getConfigManager().getLanguage().getString("game_win")),
    GAME_LOSE(PREFIX.getMessage() + KitPVP.INSTANCE.getConfigManager().getLanguage().getString("game_lose")),
    CAN_NOT_ATTACK(PREFIX.getMessage() + KitPVP.INSTANCE.getConfigManager().getLanguage().getString("can_not_attack")),
    NOT_IN_EDITOR_MODE(PREFIX.getMessage() + KitPVP.INSTANCE.getConfigManager().getLanguage().getString("not_in_editor_mode"));
    private final String message;

    MsgType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}