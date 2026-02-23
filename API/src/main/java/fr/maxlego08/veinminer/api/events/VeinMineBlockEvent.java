package fr.maxlego08.veinminer.api.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired for each individual block during a vein mining operation.
 * This event is cancellable - cancelling it will prevent that specific block from being broken.
 */
public class VeinMineBlockEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Block block;
    private final Block originBlock;
    private final ItemStack tool;
    private final int blockIndex;
    private final int totalBlocks;
    private boolean cancelled;

    /**
     * Creates a new VeinMineBlockEvent.
     *
     * @param player      the player performing the vein mine
     * @param block       the block about to be broken
     * @param originBlock the first block that was broken
     * @param tool        the tool being used
     * @param blockIndex  the index of this block in the vein (0-based)
     * @param totalBlocks the total number of blocks in the vein
     */
    public VeinMineBlockEvent(@NotNull Player player, @NotNull Block block, @NotNull Block originBlock, @NotNull ItemStack tool, int blockIndex, int totalBlocks) {
        super(player);
        this.block = block;
        this.originBlock = originBlock;
        this.tool = tool;
        this.blockIndex = blockIndex;
        this.totalBlocks = totalBlocks;
        this.cancelled = false;
    }

    /**
     * Gets the block that is about to be broken.
     *
     * @return the block
     */
    @NotNull
    public Block getBlock() {
        return block;
    }

    /**
     * Gets the origin block (the first block broken by the player).
     *
     * @return the origin block
     */
    @NotNull
    public Block getOriginBlock() {
        return originBlock;
    }

    /**
     * Gets the tool being used for vein mining.
     *
     * @return the tool
     */
    @NotNull
    public ItemStack getTool() {
        return tool;
    }

    /**
     * Gets the index of this block in the vein (0-based).
     *
     * @return the block index
     */
    public int getBlockIndex() {
        return blockIndex;
    }

    /**
     * Gets the total number of blocks in the vein.
     *
     * @return the total number of blocks
     */
    public int getTotalBlocks() {
        return totalBlocks;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
