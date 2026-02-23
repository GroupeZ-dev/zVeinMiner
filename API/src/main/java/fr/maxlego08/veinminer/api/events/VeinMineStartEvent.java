package fr.maxlego08.veinminer.api.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Event fired when a player starts a vein mining operation.
 * This event is cancellable - cancelling it will prevent the vein mine from occurring.
 */
public class VeinMineStartEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Block originBlock;
    private final Set<Block> blocks;
    private final ItemStack tool;
    private boolean cancelled;

    /**
     * Creates a new VeinMineStartEvent.
     *
     * @param player      the player performing the vein mine
     * @param originBlock the first block that was broken
     * @param blocks      the set of blocks that will be broken (modifiable)
     * @param tool        the tool being used
     */
    public VeinMineStartEvent(@NotNull Player player, @NotNull Block originBlock, @NotNull Set<Block> blocks, @NotNull ItemStack tool) {
        super(player);
        this.originBlock = originBlock;
        this.blocks = blocks;
        this.tool = tool;
        this.cancelled = false;
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
     * Gets the set of blocks that will be broken.
     * This set is modifiable - you can add or remove blocks to affect the vein mine.
     *
     * @return the set of blocks
     */
    @NotNull
    public Set<Block> getBlocks() {
        return blocks;
    }

    /**
     * Gets the total number of blocks that will be broken.
     *
     * @return the number of blocks
     */
    public int getBlockCount() {
        return blocks.size();
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
