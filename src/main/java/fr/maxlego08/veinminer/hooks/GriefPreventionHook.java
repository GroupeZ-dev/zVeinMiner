package fr.maxlego08.veinminer.hooks;

import fr.maxlego08.veinminer.VeinMinerPlugin;
import fr.maxlego08.veinminer.api.hooks.ProtectionHook;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Hook for GriefPrevention protection plugin.
 * Checks if players can break blocks in claimed areas.
 */
public class GriefPreventionHook implements ProtectionHook {

    private final VeinMinerPlugin plugin;
    private final GriefPrevention griefPrevention;

    public GriefPreventionHook(VeinMinerPlugin plugin) {
        this.plugin = plugin;
        this.griefPrevention = GriefPrevention.instance;
    }

    @Override
    public String getName() {
        return "GriefPrevention";
    }

    @Override
    public boolean isAvailable() {
        return griefPrevention != null;
    }

    @Override
    public boolean canBreak(Player player, Block block) {
        Claim claim = griefPrevention.dataStore.getClaimAt(block.getLocation(), true, null);

        // No claim at this location, allow breaking
        if (claim == null) {
            return true;
        }

        // Check if player has permission to break blocks in this claim
        String denyMessage = claim.allowBreak(player, block.getType());
        return denyMessage == null;
    }
}
