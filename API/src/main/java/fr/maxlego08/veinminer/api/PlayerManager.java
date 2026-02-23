package fr.maxlego08.veinminer.api;

import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

/**
 * Interface for managing player data and settings.
 */
public interface PlayerManager {

    /**
     * Gets the player data for the specified player.
     *
     * @param player the player
     * @return the player data
     */
    PlayerData getPlayerData(Player player);

    /**
     * Gets the player data for the specified UUID.
     *
     * @param uuid the player's UUID
     * @return an Optional containing the player data if loaded, empty otherwise
     */
    Optional<PlayerData> getPlayerData(UUID uuid);

    /**
     * Toggles vein mining for the specified player.
     *
     * @param player the player
     * @return the new enabled state
     */
    boolean toggle(Player player);

    /**
     * Sets the vein mining enabled state for the specified player.
     *
     * @param player  the player
     * @param enabled the new enabled state
     */
    void setEnabled(Player player, boolean enabled);

    /**
     * Checks if vein mining is enabled for the specified player.
     *
     * @param player the player
     * @return true if vein mining is enabled
     */
    boolean isEnabled(Player player);

    /**
     * Updates the statistics for the specified player.
     *
     * @param player      the player
     * @param blocksMined the number of blocks mined
     */
    void updateStats(Player player, int blocksMined);

    /**
     * Saves the player data to storage.
     *
     * @param uuid the player's UUID
     */
    void savePlayerData(UUID uuid);

    /**
     * Loads the player data from storage.
     *
     * @param uuid the player's UUID
     */
    void loadPlayerData(UUID uuid);

    /**
     * Unloads the player data from memory.
     *
     * @param uuid the player's UUID
     */
    void unloadPlayerData(UUID uuid);
}
