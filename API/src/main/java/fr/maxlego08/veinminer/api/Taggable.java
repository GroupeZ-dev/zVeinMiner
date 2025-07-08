package fr.maxlego08.veinminer.api;

import org.bukkit.block.Block;

/**
 * Represents a tag that can be applied to a block to determine if it can be
 * mined by the {@link VeinManager}. A tag is a way to identify a set of blocks
 * that can be mined together in a single operation.
 *
 * @author Maxlego08
 */
public interface Taggable {

    /**
     * Checks if the given {@link Block} is tagged with this tag.
     *
     * @param block the block to check
     * @return true if the block is tagged, false otherwise
     */
    boolean isTagged(Block block);

    /**
     * Converts this tag to a string representation that can be used to identify
     * it in configuration files.
     *
     * @return the string representation of the tag
     */
    String asString();
}
