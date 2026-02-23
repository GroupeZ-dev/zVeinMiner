package fr.maxlego08.veinminer.api.enums;

/**
 * Enum that contains all the permissions of the plugin.
 */
public enum Permission {

    ZVEINMINER_USE,
    ZVEINMINER_RELOAD,
    ZVEINMINER_APPLY,
    ZVEINMINER_SET,
    ZVEINMINER_ADD,
    ZVEINMINER_REMOVE,
    ZVEINMINER_SIZE,
    ZVEINMINER_TOGGLE,
    ZVEINMINER_LIST,
    ZVEINMINER_INFO,
    ZVEINMINER_GIVE,
    ZVEINMINER_STATS,

    ;

    private final String permission;

    Permission() {
        this.permission = this.name().toLowerCase().replace("_", ".");
    }

    public String getPermission() {
        return permission;
    }

}
