package fr.maxlego08.veinminer.api.hooks;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Interface for protection plugin hooks.
 * Implementations check if a player can break a block at a specific location.
 */
public interface ProtectionHook {

    /**
     * Gets the name of the protection plugin.
     *
     * @return the plugin name
     */
    String getName();

    /**
     * Checks if this hook is available (plugin loaded and enabled).
     *
     * @return true if the hook is available
     */
    boolean isAvailable();

    /**
     * Checks if the player can break the specified block.
     *
     * @param player the player attempting to break the block
     * @param block  the block to check
     * @return true if the player can break the block, false otherwise
     */
    boolean canBreak(Player player, Block block);
}
