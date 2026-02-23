package fr.maxlego08.veinminer.hooks;

import fr.maxlego08.veinminer.VeinMinerPlugin;
import fr.maxlego08.veinminer.api.hooks.ProtectionHook;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Manages all protection hooks for the plugin.
 * Provides a unified way to check if a player can break a block.
 */
public class HookManager {

    private final VeinMinerPlugin plugin;
    private final List<ProtectionHook> hooks = new ArrayList<>();
    private final Logger logger;

    public HookManager(VeinMinerPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.registerHooks();
    }

    /**
     * Registers all available protection hooks.
     */
    private void registerHooks() {
        // WorldGuard
        registerHook("WorldGuard", () -> new WorldGuardHook(plugin));

        // GriefPrevention
        registerHook("GriefPrevention", () -> new GriefPreventionHook(plugin));
    }

    /**
     * Attempts to register a hook if its plugin is available.
     *
     * @param pluginName   the name of the plugin
     * @param hookSupplier supplier for creating the hook instance
     */
    private void registerHook(String pluginName, HookSupplier hookSupplier) {
        if (plugin.getServer().getPluginManager().getPlugin(pluginName) != null) {
            try {
                ProtectionHook hook = hookSupplier.get();
                if (hook.isAvailable()) {
                    hooks.add(hook);
                    logger.info("Hooked into " + pluginName);
                }
            } catch (Exception e) {
                logger.warning("Failed to hook into " + pluginName + ": " + e.getMessage());
            }
        }
    }

    /**
     * Checks if a player can break the specified block according to all registered hooks.
     *
     * @param player the player attempting to break the block
     * @param block  the block to check
     * @return true if the player can break the block (all hooks allow it), false otherwise
     */
    public boolean canBreak(Player player, Block block) {
        for (ProtectionHook hook : hooks) {
            if (!hook.canBreak(player, block)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets a list of all registered hook names.
     *
     * @return list of hook names
     */
    public List<String> getHookNames() {
        return hooks.stream().map(ProtectionHook::getName).toList();
    }

    /**
     * Gets the number of registered hooks.
     *
     * @return the number of hooks
     */
    public int getHookCount() {
        return hooks.size();
    }

    @FunctionalInterface
    private interface HookSupplier {
        ProtectionHook get() throws Exception;
    }
}
