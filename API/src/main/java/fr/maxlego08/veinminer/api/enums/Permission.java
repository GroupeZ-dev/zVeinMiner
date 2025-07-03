package fr.maxlego08.veinminer.api.enums;

public enum Permission {

    ZVEINMINER_USE,
    ZVEINMINER_RELOAD,
    ZVEINMINER_APPLY,

    ;

    private final String permission;

    Permission() {
        this.permission = this.name().toLowerCase().replace("_", ".");
    }

    public String getPermission() {
        return permission;
    }

}
