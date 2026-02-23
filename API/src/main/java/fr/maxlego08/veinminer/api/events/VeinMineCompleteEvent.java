package fr.maxlego08.veinminer.api.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Event fired when a vein mining operation is completed.
 * This event is NOT cancellable as the blocks have already been broken.
 */
public class VeinMineCompleteEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Block originBlock;
    private final Set<Block> brokenBlocks;
    private final ItemStack tool;
    private final int totalBlocksBroken;

    /**
     * Creates a new VeinMineCompleteEvent.
     *
     * @param player            the player who performed the vein mine
     * @param originBlock       the first block that was broken
     * @param brokenBlocks      the set of blocks that were broken (excluding origin)
     * @param tool              the tool that was used
     * @param totalBlocksBroken the total number of blocks broken (including origin)
     */
    public VeinMineCompleteEvent(@NotNull Player player, @NotNull Block originBlock, @NotNull Set<Block> brokenBlocks, @NotNull ItemStack tool, int totalBlocksBroken) {
        super(player);
        this.originBlock = originBlock;
        this.brokenBlocks = brokenBlocks;
        this.tool = tool;
        this.totalBlocksBroken = totalBlocksBroken;
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
     * Gets the set of blocks that were broken (excluding the origin block).
     *
     * @return the set of broken blocks
     */
    @NotNull
    public Set<Block> getBrokenBlocks() {
        return brokenBlocks;
    }

    /**
     * Gets the tool that was used for vein mining.
     *
     * @return the tool
     */
    @NotNull
    public ItemStack getTool() {
        return tool;
    }

    /**
     * Gets the total number of blocks that were broken (including the origin block).
     *
     * @return the total number of blocks broken
     */
    public int getTotalBlocksBroken() {
        return totalBlocksBroken;
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
