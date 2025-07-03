package fr.maxlego08.veinminer;

import fr.maxlego08.veinminer.api.Config;
import fr.maxlego08.veinminer.api.VeinKeys;
import fr.maxlego08.veinminer.api.VeinManager;
import fr.maxlego08.veinminer.api.VeinPreset;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

public class ZVeinManager implements VeinManager {

    private final VeinMinerPlugin plugin;
    private final VeinKeys veinKeys;
    private final Map<Player, VeinVisualizer> visualizers = new HashMap<>();

    public ZVeinManager(VeinMinerPlugin plugin) {
        this.plugin = plugin;
        this.veinKeys = new ZVeinKeys(plugin);
    }

    @Override
    public VeinKeys getVeinKeys() {
        return this.veinKeys;
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

    @Override
    public void applyPreset(ItemStack itemStack, VeinPreset veinPreset) {

        var meta = itemStack.getItemMeta();
        var pdc = meta.getPersistentDataContainer();
        pdc.set(this.veinKeys.getPresetKey(), PersistentDataType.STRING, veinPreset.getName());
        itemStack.setItemMeta(meta);

    }

    @Override
    public Optional<VeinPreset> getVeinPreset(String name) {
        return Optional.ofNullable(Config.veinPresets.get(name));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {

        var player = event.getPlayer();
        if (player.hasMetadata("vein-miner-cooldown")) return;

        var block = event.getBlock();
        var itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType().isAir()) return;

        var blocks = this.getVeinBlocks(block, 100);
        if (blocks.isEmpty()) return;

        onVisualize(player, VeinVisualizer::remove);

        blocks.remove(block);

        player.setMetadata("vein-miner-cooldown", new FixedMetadataValue(plugin, true));

        for (Block targetBlock : blocks) targetBlock.breakNaturally(itemStack);

        if (player.getGameMode() != GameMode.CREATIVE) {
            itemStack.damage(blocks.size(), player);
        }

        player.removeMetadata("vein-miner-cooldown", plugin);
    }

    private void onVisualize(Player player, Consumer<VeinVisualizer> veinVisualizerConsumer) {
        Optional.ofNullable(this.visualizers.get(player)).ifPresent(veinVisualizerConsumer);
    }

    private VeinVisualizer getVisualizer(Player player) {
        return this.visualizers.computeIfAbsent(player, k -> new VeinVisualizer(this.plugin, player));
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {

        var player = event.getPlayer();
        var isSneaking = event.isSneaking();

        var block = player.getTargetBlock(null, 10);
        if (block.getType().isAir()) return;

        if (isSneaking) {
            var blocks = this.getVeinBlocks(block, 100);
            if (blocks.isEmpty()) return;

            var veinVisualizer = this.getVisualizer(player);
            veinVisualizer.spawn(blocks);

        } else {

            onVisualize(player, VeinVisualizer::remove);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        var player = event.getPlayer();
        onVisualize(player, VeinVisualizer::remove);
        this.visualizers.remove(player);
    }
}
