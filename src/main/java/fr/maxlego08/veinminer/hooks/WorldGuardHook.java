package fr.maxlego08.veinminer.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import fr.maxlego08.veinminer.VeinMinerPlugin;
import fr.maxlego08.veinminer.api.hooks.ProtectionHook;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Hook for WorldGuard protection plugin.
 * Checks if players can break blocks in protected regions.
 */
public class WorldGuardHook implements ProtectionHook {

    private final VeinMinerPlugin plugin;
    private final WorldGuard worldGuard;
    private final RegionContainer regionContainer;

    public WorldGuardHook(VeinMinerPlugin plugin) {
        this.plugin = plugin;
        this.worldGuard = WorldGuard.getInstance();
        this.regionContainer = worldGuard.getPlatform().getRegionContainer();
    }

    @Override
    public String getName() {
        return "WorldGuard";
    }

    @Override
    public boolean isAvailable() {
        return worldGuard != null && regionContainer != null;
    }

    @Override
    public boolean canBreak(Player player, Block block) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(block.getLocation());
        RegionQuery query = regionContainer.createQuery();

        // Check if the player can build (break blocks) at this location
        return query.testState(location, localPlayer, Flags.BLOCK_BREAK);
    }
}
