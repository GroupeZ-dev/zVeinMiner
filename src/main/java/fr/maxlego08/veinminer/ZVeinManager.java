package fr.maxlego08.veinminer;

import fr.maxlego08.veinminer.api.VeinManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class ZVeinManager implements VeinManager {

    private final VeinMinerPlugin plugin;
    private final NamespacedKey veinKey;

    public ZVeinManager(VeinMinerPlugin plugin) {
        this.plugin = plugin;
        this.veinKey = new NamespacedKey(plugin, "vein-miner");
    }

    @Override
    public NamespacedKey getVeinKey() {
        return this.veinKey;
    }

    @Override
    public Set<Block> getVeinBlocks(Block startBlock, int maxVeinSize) {
        Set<Block> veinBlocks = new HashSet<>();
        Queue<Block> blocksToCheck = new LinkedList<>();
        Material blockType = startBlock.getType();

        blocksToCheck.add(startBlock);

        while (!blocksToCheck.isEmpty() && veinBlocks.size() < maxVeinSize) {
            Block currentBlock = blocksToCheck.poll();

            if (currentBlock.getType() != blockType || veinBlocks.contains(currentBlock)) {
                continue;
            }

            veinBlocks.add(currentBlock);

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x == 0 && y == 0 && z == 0) continue;

                        Block adjacentBlock = currentBlock.getRelative(x, y, z);

                        if (!veinBlocks.contains(adjacentBlock)) {
                            blocksToCheck.add(adjacentBlock);
                        }
                    }
                }
            }
        }

        return veinBlocks;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {

        var player = event.getPlayer();
        System.out.println("-> " + player.hasMetadata("vein-miner-cooldown"));
        if (player.hasMetadata("vein-miner-cooldown")) return;

        var block = event.getBlock();
        var itemStack = player.getInventory().getItemInMainHand();

        var blocks = this.getVeinBlocks(block, 100);
        if (blocks.isEmpty()) return;

        player.setMetadata("vein-miner-cooldown", new FixedMetadataValue(plugin, true));

        for (Block targetBlock : blocks) {
            targetBlock.breakNaturally(itemStack);
        }

        player.removeMetadata("vein-miner-cooldown", plugin);
    }
}
