package fr.maxlego08.veinminer.api.enums;

/**
 * Enum that contains all the permissions of the plugin.
 */
public enum Permission {

    ZVEINMINER_USE,
    ZVEINMINER_RELOAD,
    ZVEINMINER_APPLY,
    ZVEINMINER_SIZE,

    ;

    private final String permission;

    Permission() {
        this.permission = this.name().toLowerCase().replace("_", ".");
    }

    public String getPermission() {
        return permission;
    }

}
