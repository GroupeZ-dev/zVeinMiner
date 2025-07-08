package fr.maxlego08.veinminer.api;

import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a preset for the {@link VeinManager}. A preset is a predefined set of
 * options that can be used to configure the {@link VeinManager}.
 *
 * @author Maxlego08
 */
public interface VeinPreset {

    /**
     * Retrieves the name of the preset.
     *
     * @return the name of the preset
     */
    String getName();

    /**
     * Retrieves the maximum size of the vein that can be mined using this preset.
     *
     * @return the maximum vein size
     */
    int getMaxVeinSize();

    /**
     * Retrieves the list of tags associated with this preset.
     *
     * @return the list of tags associated with this preset
     */
    List<Taggable> getTags();

    /**
     * Checks if the given {@link Block} is tagged with one of the tags in the list
     * returned by {@link #getTags()}.
     *
     * @param block the block to check
     * @return true if the block is tagged, false otherwise
     */
    boolean isTagged(Block block);

    /**
     * Retrieves the tags associated with this preset as a single concatenated string.
     * Each tag is separated by a comma.
     *
     * @return a string representation of the tags associated with this preset
     */
    String getTagsAsString();
}
