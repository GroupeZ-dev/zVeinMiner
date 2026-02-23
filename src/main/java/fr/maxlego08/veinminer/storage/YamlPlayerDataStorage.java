package fr.maxlego08.veinminer.storage;

import fr.maxlego08.veinminer.VeinMinerPlugin;
import fr.maxlego08.veinminer.api.PlayerData;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * YAML-based storage for player data.
 * Stores player data in individual YAML files under plugins/zVeinMiner/playerdata/
 */
public class YamlPlayerDataStorage {

    private final VeinMinerPlugin plugin;
    private final File playerDataFolder;

    public YamlPlayerDataStorage(VeinMinerPlugin plugin) {
        this.plugin = plugin;
        this.playerDataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }
    }

    /**
     * Gets the file for the specified player.
     *
     * @param uuid the player's UUID
     * @return the file
     */
    private File getPlayerFile(UUID uuid) {
        return new File(playerDataFolder, uuid.toString() + ".yml");
    }

    /**
     * Loads player data from storage.
     *
     * @param uuid the player's UUID
     * @return the player data, or default data if not found
     */
    public PlayerData load(UUID uuid) {
        File file = getPlayerFile(uuid);

        if (!file.exists()) {
            return PlayerData.defaultData(uuid);
        }

        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            boolean veinMinerEnabled = config.getBoolean("vein-miner-enabled", true);
            long totalBlocksMined = config.getLong("statistics.total-blocks-mined", 0);
            long totalVeinsMined = config.getLong("statistics.total-veins-mined", 0);

            return new PlayerData(uuid, veinMinerEnabled, totalBlocksMined, totalVeinsMined);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load player data for " + uuid + ": " + e.getMessage());
            return PlayerData.defaultData(uuid);
        }
    }

    /**
     * Saves player data to storage.
     *
     * @param data the player data to save
     */
    public void save(PlayerData data) {
        File file = getPlayerFile(data.uuid());

        try {
            YamlConfiguration config = new YamlConfiguration();

            config.set("vein-miner-enabled", data.veinMinerEnabled());
            config.set("statistics.total-blocks-mined", data.totalBlocksMined());
            config.set("statistics.total-veins-mined", data.totalVeinsMined());

            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save player data for " + data.uuid() + ": " + e.getMessage());
        }
    }

    /**
     * Deletes player data from storage.
     *
     * @param uuid the player's UUID
     * @return true if the file was deleted
     */
    public boolean delete(UUID uuid) {
        File file = getPlayerFile(uuid);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * Checks if player data exists in storage.
     *
     * @param uuid the player's UUID
     * @return true if the data exists
     */
    public boolean exists(UUID uuid) {
        return getPlayerFile(uuid).exists();
    }
}
