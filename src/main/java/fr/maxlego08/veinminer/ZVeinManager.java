package fr.maxlego08.veinminer;

import fr.maxlego08.veinminer.api.Config;
import fr.maxlego08.veinminer.api.ItemVeinMinerResult;
import fr.maxlego08.veinminer.api.Taggable;
import fr.maxlego08.veinminer.api.VeinKeys;
import fr.maxlego08.veinminer.api.VeinManager;
import fr.maxlego08.veinminer.api.VeinPreset;
import fr.maxlego08.veinminer.api.enums.Message;
import fr.maxlego08.veinminer.api.events.VeinMineBlockEvent;
import fr.maxlego08.veinminer.api.events.VeinMineCompleteEvent;
import fr.maxlego08.veinminer.api.events.VeinMineStartEvent;
import fr.maxlego08.veinminer.debug.DebugLogger;
import fr.maxlego08.veinminer.hooks.HookManager;
import fr.maxlego08.veinminer.hooks.VaultHook;
import fr.maxlego08.veinminer.utils.ItemUtils;
import fr.maxlego08.veinminer.utils.ZUtils;
import org.bukkit.Bukkit;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ZVeinManager extends ZUtils implements VeinManager {

    private final VeinMinerPlugin plugin;
    private final VeinKeys veinKeys;
    private final HookManager hookManager;
    private final VaultHook vaultHook;
    private final Map<Player, VeinVisualizer> visualizers = new HashMap<>();

    public ZVeinManager(VeinMinerPlugin plugin) {
        this.plugin = plugin;
        this.veinKeys = new ZVeinKeys(plugin);
        this.hookManager = new HookManager(plugin);
        this.vaultHook = new VaultHook(plugin);
    }

    /**
     * Gets the HookManager instance.
     *
     * @return the HookManager
     */
    public HookManager getHookManager() {
        return hookManager;
    }

    /**
     * Gets the VaultHook instance.
     *
     * @return the VaultHook
     */
    public VaultHook getVaultHook() {
        return vaultHook;
    }

    @Override
    public VeinKeys getVeinKeys() {
        return this.veinKeys;
    }

    @Override
    public Set<Block> getVeinBlocks(Block startBlock, int maxVeinSize) {
        return DebugLogger.debugTime("getVeinBlocks", () -> {
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

            DebugLogger.debug("Found vein with {} blocks of type {}", veinBlocks.size(), blockType);
            return veinBlocks;
        });
    }

    @Override
    public void applyPreset(ItemStack itemStack, VeinPreset veinPreset) {

        var meta = itemStack.getItemMeta();
        var pdc = meta.getPersistentDataContainer();
        pdc.set(this.veinKeys.getPresetKey(), PersistentDataType.STRING, veinPreset.getName());
        itemStack.setItemMeta(meta);
    }

    @Override
    public void setPreset(ItemStack itemStack, VeinPreset veinPreset) {

        var meta = itemStack.getItemMeta();
        var pdc = meta.getPersistentDataContainer();
        pdc.set(this.veinKeys.getSizeKey(), PersistentDataType.INTEGER, veinPreset.getMaxVeinSize());
        pdc.set(this.veinKeys.getTaggableKey(), PersistentDataType.STRING, veinPreset.getTagsAsString());
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
            taggables = this.getTaggables(pdc.getOrDefault(this.veinKeys.getTaggableKey(), PersistentDataType.STRING, ""));
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

    @Override
    public List<Taggable> getTaggables(String string) {
        return Arrays.stream(string.split(",")).map(this.plugin::toTag).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public boolean addTag(ItemStack itemStack, Taggable taggable) {
        return updateTagList(itemStack, taggable, true);
    }

    @Override
    public boolean removeTag(ItemStack itemStack, Taggable taggable) {
        return updateTagList(itemStack, taggable, false);
    }

    private boolean updateTagList(ItemStack itemStack, Taggable taggable, boolean add) {
        if (!itemStack.hasItemMeta()) return false;

        var meta = itemStack.getItemMeta();
        var pdc = meta.getPersistentDataContainer();

        List<Taggable> taggables = new ArrayList<>();
        if (pdc.has(this.veinKeys.getTaggableKey(), PersistentDataType.STRING)) {
            taggables = this.getTaggables(pdc.getOrDefault(this.veinKeys.getTaggableKey(), PersistentDataType.STRING, ""));
        }

        boolean modified;
        if (add) {
            if (taggables.contains(taggable)) return false;
            modified = taggables.add(taggable);
        } else {
            if (!taggables.contains(taggable)) return false;
            modified = taggables.remove(taggable);
        }

        if (!modified) return false;

        pdc.set(this.veinKeys.getTaggableKey(), PersistentDataType.STRING, taggables.stream().map(Taggable::asString).collect(Collectors.joining(",")));

        itemStack.setItemMeta(meta);

        return true;
    }


    @Override
    public List<String> getTags(Player playerSender) {

        var itemStack = playerSender.getInventory().getItemInMainHand();
        if (itemStack.getType().isAir() || !itemStack.hasItemMeta()) return List.of();

        return this.getVeinMinerResult(itemStack).map(e -> e.tags().stream().map(Taggable::asString).collect(Collectors.toList())).orElse(List.of());
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
     * Checks if vein mining is allowed in the specified world.
     *
     * @param worldName the world name to check
     * @return true if vein mining is allowed
     */
    private boolean isWorldAllowed(String worldName) {
        boolean inList = Config.worldBlacklist.contains(worldName);
        return Config.worldWhitelistMode ? inList : !inList;
    }

    /**
     * Validates if the vein mining action can be performed with the given player and block.
     *
     * @param player the player attempting the vein mine
     * @param block  the target block
     * @return Optional containing the VeinMinerResult if validation passes, empty otherwise
     */
    private Optional<ItemVeinMinerResult> validateVeinMine(Player player, Block block) {
        // Check if player has vein mining enabled
        var playerManager = this.plugin.getPlayerManager();
        if (playerManager != null && !playerManager.isEnabled(player)) {
            return Optional.empty();
        }

        // Check if world is allowed
        if (!isWorldAllowed(block.getWorld().getName())) {
            return Optional.empty();
        }

        // Check if sneak is required
        if (Config.requireSneak && !player.isSneaking()) {
            return Optional.empty();
        }

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

        // Filter blocks by protection hooks
        Set<Block> allowedBlocks = blocks.stream()
                .filter(b -> hookManager.canBreak(player, b))
                .collect(Collectors.toSet());

        if (allowedBlocks.isEmpty()) return;

        // Fire VeinMineStartEvent
        VeinMineStartEvent startEvent = new VeinMineStartEvent(player, block, allowedBlocks, itemStack);
        Bukkit.getPluginManager().callEvent(startEvent);
        if (startEvent.isCancelled()) return;

        // Check economy cost
        if (Config.economyEnabled && vaultHook.isAvailable()) {
            double totalCost = Config.costPerVein + (Config.costPerBlock * allowedBlocks.size());
            if (totalCost > 0 && !vaultHook.has(player, totalCost)) {
                message(player, Message.ECONOMY_NOT_ENOUGH, "%amount%", vaultHook.format(totalCost));
                return;
            }
            if (totalCost > 0) {
                vaultHook.withdraw(player, totalCost);
                message(player, Message.ECONOMY_CHARGED, "%amount%", vaultHook.format(totalCost));
            }
        }

        // Limit blocks by durability if enabled
        int maxBreakable = allowedBlocks.size();
        if (player.getGameMode() != GameMode.CREATIVE && Config.checkDurability) {
            maxBreakable = Math.min(maxBreakable, ItemUtils.getMaxBlocksBreakable(itemStack));
        }

        List<Block> blocksToBreak = allowedBlocks.stream().limit(maxBreakable).toList();

        player.setMetadata("vein-miner-cooldown", new FixedMetadataValue(plugin, true));

        // Break blocks with per-block events
        List<Block> actuallyBroken = new ArrayList<>();
        int index = 0;
        for (Block targetBlock : blocksToBreak) {
            VeinMineBlockEvent blockEvent = new VeinMineBlockEvent(player, targetBlock, block, itemStack, index, blocksToBreak.size());
            Bukkit.getPluginManager().callEvent(blockEvent);

            if (!blockEvent.isCancelled()) {
                targetBlock.breakNaturally(itemStack, true, true);
                actuallyBroken.add(targetBlock);
            }
            index++;
        }

        // Apply durability damage if enabled
        if (player.getGameMode() != GameMode.CREATIVE && Config.applyDamage && !actuallyBroken.isEmpty()) {
            itemStack.damage(actuallyBroken.size(), player);
        }

        // Update player statistics
        var playerManager = this.plugin.getPlayerManager();
        if (playerManager != null) {
            playerManager.updateStats(player, actuallyBroken.size() + 1); // +1 for the original block
        }

        // Fire VeinMineCompleteEvent
        VeinMineCompleteEvent completeEvent = new VeinMineCompleteEvent(player, block, new HashSet<>(actuallyBroken), itemStack, actuallyBroken.size() + 1);
        Bukkit.getPluginManager().callEvent(completeEvent);

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
