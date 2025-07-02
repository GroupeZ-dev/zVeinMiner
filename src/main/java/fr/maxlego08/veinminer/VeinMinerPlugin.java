package fr.maxlego08.veinminer;

import org.bukkit.plugin.java.JavaPlugin;

public class VeinMinerPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        super.onEnable();

        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}