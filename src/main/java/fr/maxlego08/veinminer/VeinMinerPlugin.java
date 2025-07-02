package fr.maxlego08.veinminer;

import fr.maxlego08.veinminer.api.VeinManager;
import fr.maxlego08.veinminer.command.CommandManager;
import fr.maxlego08.veinminer.command.commands.CommandTemplate;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class VeinMinerPlugin extends JavaPlugin {

    private final CommandManager commandManager = new CommandManager(this);
    private VeinManager veinManager;

    @Override
    public void onEnable() {
        super.onEnable();

        this.saveDefaultConfig();

        this.commandManager.registerCommand(this, "zveinminer", new CommandTemplate(this), List.of("veinminer", "zvm"));

        this.veinManager = new ZVeinManager(this);

        var server = this.getServer();
        server.getPluginManager().registerEvents(this.veinManager, this);
        server.getServicesManager().register(VeinManager.class, this.veinManager, this, ServicePriority.Highest);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public void reloadFiles() {
        this.reloadConfig();
        this.getLogger().info("Config reloaded");
    }

    public VeinManager getVeinManager() {
        return veinManager;
    }
}