package fr.maxlego08.veinminer;

import fr.maxlego08.veinminer.api.Config;
import fr.maxlego08.veinminer.api.Taggable;
import fr.maxlego08.veinminer.api.VeinManager;
import fr.maxlego08.veinminer.command.CommandManager;
import fr.maxlego08.veinminer.command.commands.CommandVein;
import fr.maxlego08.veinminer.tags.MaterialTaggable;
import fr.maxlego08.veinminer.tags.TagTaggable;
import fr.maxlego08.veinminer.utils.TagRegistry;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The main class of the zVeinMiner plugin. This class is responsible for registering
 * commands and event listeners, and for loading the configuration file.
 *
 * @author Maxlego08
 */
public class VeinMinerPlugin extends JavaPlugin {

    private final CommandManager commandManager = new CommandManager(this);
    private VeinManager veinManager;

    @Override
    public void onEnable() {
        super.onEnable();

        this.saveDefaultConfig();

        this.commandManager.registerCommand(this, "zveinminer", new CommandVein(this), List.of("veinminer", "zvm"));

        this.veinManager = new ZVeinManager(this);

        var server = this.getServer();
        server.getPluginManager().registerEvents(this.veinManager, this);
        server.getServicesManager().register(VeinManager.class, this.veinManager, this, ServicePriority.Highest);

        this.loadConfiguration(this.getConfig());
    }

    /**
     * Returns the CommandManager object that handles all command-related logic.
     *
     * @return the CommandManager object
     */
    public CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Reloads the configuration from the default config file and loads the
     * configuration data into the plugin. This will clear any existing presets
     * and populate them with new ones defined in the configuration file.
     */
    public void reloadFiles() {
        this.reloadConfig();
        this.loadConfiguration(this.getConfig());
        this.getLogger().info("Config reloaded");
    }

    /**
     * Gets the VeinManager object that handles all vein-related logic,
     * including managing the list of vein presets and handling block break events.
     *
     * @return The VeinManager object.
     */
    public VeinManager getVeinManager() {
        return veinManager;
    }

    /**
     * Loads the configuration for vein presets from the provided FileConfiguration.
     * Clears existing presets and populates them with new ones defined in the
     * configuration file. Each preset consists of a name, size, and a list of tags.
     * The tags are converted to Taggable objects and stored in the Config.veinPresets map.
     *
     * @param configuration The FileConfiguration object containing the preset data.
     */
    private void loadConfiguration(FileConfiguration configuration) {

        Config.enableDebug = configuration.getBoolean("enable-debug");
        Config.enableDebugTime = configuration.getBoolean("enable-debug-time");

        Config.veinPresets.clear();

        var presets = configuration.getMapList("presets");
        for (Map<?, ?> preset : presets) {
            String name = (String) preset.get("name");
            int size = (int) preset.get("size");
            var tags = (List<String>) preset.get("tags");
            List<Taggable> tagsList = tags.stream().map(String::toUpperCase).map(this::toTag).filter(Objects::nonNull).toList();
            Config.veinPresets.put(name, new ZVeinPreset(name, size, tagsList));
        }

        System.out.println(Config.veinPresets);
    }

    /**
     * Convert a string to a Taggable, either by looking it up in the tag registry,
     * or by converting it to a MaterialTaggable if it's a valid material name.
     *
     * @param tag the string to convert
     * @return the Taggable, or null if an error occurs
     */
    public Taggable toTag(String tag) {
        var bukkitTag = TagRegistry.getTag(tag);
        if (bukkitTag != null) return new TagTaggable(tag, bukkitTag);

        try {
            return new MaterialTaggable(Material.valueOf(tag));
        } catch (Exception exception) {
            exception.printStackTrace();
            this.getLogger().severe("Error while loading tag " + tag);
        }

        return null;
    }
}