package fr.maxlego08.veinminer.api;

import java.util.UUID;

/**
 * Record representing player data for vein mining.
 *
 * @param uuid               the player's UUID
 * @param veinMinerEnabled   whether vein mining is enabled for the player
 * @param totalBlocksMined   total number of blocks mined with vein miner
 * @param totalVeinsMined    total number of veins mined
 */
public record PlayerData(
        UUID uuid,
        boolean veinMinerEnabled,
        long totalBlocksMined,
        long totalVeinsMined
) {

    /**
     * Creates a new PlayerData with default values.
     *
     * @param uuid the player's UUID
     * @return a new PlayerData with default values
     */
    public static PlayerData defaultData(UUID uuid) {
        return new PlayerData(uuid, true, 0, 0);
    }

    /**
     * Creates a new PlayerData with vein miner toggled.
     *
     * @return a new PlayerData with vein miner toggled
     */
    public PlayerData withToggled() {
        return new PlayerData(uuid, !veinMinerEnabled, totalBlocksMined, totalVeinsMined);
    }

    /**
     * Creates a new PlayerData with vein miner enabled state changed.
     *
     * @param enabled the new enabled state
     * @return a new PlayerData with the new enabled state
     */
    public PlayerData withEnabled(boolean enabled) {
        return new PlayerData(uuid, enabled, totalBlocksMined, totalVeinsMined);
    }

    /**
     * Creates a new PlayerData with updated statistics.
     *
     * @param blocksMinedDelta the number of blocks to add to the total
     * @param veinsMined       whether to increment the veins mined counter
     * @return a new PlayerData with updated statistics
     */
    public PlayerData withStats(int blocksMinedDelta, boolean veinsMined) {
        return new PlayerData(
                uuid,
                veinMinerEnabled,
                totalBlocksMined + blocksMinedDelta,
                totalVeinsMined + (veinsMined ? 1 : 0)
        );
    }
}
