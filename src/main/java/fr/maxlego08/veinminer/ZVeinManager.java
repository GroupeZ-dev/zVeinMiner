package fr.maxlego08.veinminer;

import fr.maxlego08.veinminer.api.Config;
import fr.maxlego08.veinminer.api.ItemVeinMinerResult;
import fr.maxlego08.veinminer.api.Taggable;
import fr.maxlego08.veinminer.api.VeinKeys;
import fr.maxlego08.veinminer.api.VeinManager;
import fr.maxlego08.veinminer.api.VeinPreset;
import fr.maxlego08.veinminer.api.enums.Message;
import fr.maxlego08.veinminer.utils.ZUtils;
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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

public class ZVeinManager extends ZUtils implements VeinManager {

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

    @Override
    public Optional<ItemVeinMinerResult> getVeinMinerResult(ItemStack itemStack) {

        // Create cache

        if (!itemStack.hasItemMeta()) return Optional.empty();

        var meta = itemStack.getItemMeta();
        var pdc = meta.getPersistentDataContainer();
        VeinPreset preset = null;
        if (pdc.has(this.veinKeys.getPresetKey(), PersistentDataType.STRING)) {
            var presetName = pdc.get(this.veinKeys.getPresetKey(), PersistentDataType.STRING);
            preset = this.getVeinPreset(presetName).orElse(null);
        }

        int size = preset == null ? 0 : preset.getMaxVeinSize();
        List<Taggable> taggables = preset == null ? List.of() : preset.getTags();

        if (pdc.has(this.veinKeys.getSizeKey(), PersistentDataType.INTEGER)) {
            size = pdc.getOrDefault(this.veinKeys.getSizeKey(), PersistentDataType.INTEGER, 0);
        }

        if (pdc.has(this.veinKeys.getTaggableKey(), PersistentDataType.STRING)) {
            var taggableString = pdc.getOrDefault(this.veinKeys.getTaggableKey(), PersistentDataType.STRING, "");
            // ToDo
        }

        return Optional.of(new ItemVeinMinerResult(size, taggables));
    }

    @Override
    public void displayCurrentSize(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        int size = this.getVeinMinerResult(itemStack).map(ItemVeinMinerResult::size).orElse(0);
        message(player, Message.SIZE_DISPLAY, "%size%", size);
    }

    @Override
    public void changeSize(ItemStack itemStack, int size) {

        var meta = itemStack.getItemMeta();
        var pdc = meta.getPersistentDataContainer();

        if (size == 0) pdc.remove(this.veinKeys.getSizeKey());
        else pdc.set(this.veinKeys.getSizeKey(), PersistentDataType.INTEGER, size);

        itemStack.setItemMeta(meta);
    }

    /**
     * Executes the given {@link Consumer} on the {@link VeinVisualizer} associated with the given {@link Player}.
     * If no {@link VeinVisualizer} is associated with the player, the consumer is not executed.
     *
     * @param player                 the player to get the {@link VeinVisualizer} for
     * @param veinVisualizerConsumer the consumer to execute
     */
    private void onVisualize(Player player, Consumer<VeinVisualizer> veinVisualizerConsumer) {
        Optional.ofNullable(this.visualizers.get(player)).ifPresent(veinVisualizerConsumer);
    }

    /**
     * Get the {@link VeinVisualizer} associated with the given {@link Player}.
     * If no {@link VeinVisualizer} is associated with the player, a new one is created and stored in the map.
     *
     * @param player the player to get the {@link VeinVisualizer} for
     * @return the {@link VeinVisualizer} associated with the player
     */
    private VeinVisualizer getVisualizer(Player player) {
        return this.visualizers.computeIfAbsent(player, k -> new VeinVisualizer(this.plugin, player));
    }

    /**
     * Validates if the vein mining action can be performed with the given player and block.
     *
     * @param player the player attempting the vein mine
     * @param block  the target block
     * @return Optional containing the VeinMinerResult if validation passes, empty otherwise
     */
    private Optional<ItemVeinMinerResult> validateVeinMine(Player player, Block block) {
        var itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType().isAir()) return Optional.empty();

        var optional = this.getVeinMinerResult(itemStack);
        if (optional.isEmpty()) return Optional.empty();

        var veinMinerResult = optional.get();
        if (veinMinerResult.size() == 0) return Optional.empty();

        if (veinMinerResult.tags().stream().noneMatch(e -> e.isTagged(block))) return Optional.empty();

        return Optional.of(veinMinerResult);
    }

    /**
     * Listens for the player breaking a block. If the player is holding an item with a {@link ItemVeinMinerResult}
     * associated with it, it will break all blocks that are part of the same vein as the broken block, up to a maximum
     * size of the item's size. Additionally, it will remove any {@link VeinVisualizer} associated with the player.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {

        var player = event.getPlayer();
        if (player.hasMetadata("vein-miner-cooldown")) return;

        var block = event.getBlock();
        var veinMinerResult = validateVeinMine(player, block).orElse(null);
        if (veinMinerResult == null) return;

        var blocks = this.getVeinBlocks(block, veinMinerResult.size());
        if (blocks.isEmpty()) return;

        var itemStack = player.getInventory().getItemInMainHand();

        onVisualize(player, VeinVisualizer::remove);

        blocks.remove(block);

        player.setMetadata("vein-miner-cooldown", new FixedMetadataValue(plugin, true));

        for (Block targetBlock : blocks) targetBlock.breakNaturally(itemStack);

        if (player.getGameMode() != GameMode.CREATIVE) {
            itemStack.damage(blocks.size(), player);
        }

        player.removeMetadata("vein-miner-cooldown", plugin);
    }

    /**
     * Listens for the player's sneak state to change. If the player is
     * sneaking, it will spawn a visualizer that shows all the blocks that
     * are in the same vein as the block that the player is currently
     * looking at. If the player is not sneaking, it will remove the
     * visualizer.
     *
     * @param event the event
     */
    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {

        var player = event.getPlayer();
        var isSneaking = event.isSneaking();

        var block = player.getTargetBlock(null, 10);
        if (block.getType().isAir()) return;

        if (isSneaking) {
            var optional = validateVeinMine(player, block);
            if (optional.isEmpty()) return;
            var veinMinerResult = optional.get();

            var blocks = this.getVeinBlocks(block, veinMinerResult.size());
            if (blocks.isEmpty()) return;

            var veinVisualizer = this.getVisualizer(player);
            veinVisualizer.spawn(blocks);

        } else {

            onVisualize(player, VeinVisualizer::remove);
        }
    }

    /**
     * Removes the {@link VeinVisualizer} associated with the given player when
     * they quit the game.
     *
     * @param event the event
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        var player = event.getPlayer();
        onVisualize(player, VeinVisualizer::remove);
        this.visualizers.remove(player);
    }
}
