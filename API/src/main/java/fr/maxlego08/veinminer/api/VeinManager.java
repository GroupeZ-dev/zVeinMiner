package fr.maxlego08.veinminer.api;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * This interface represents the main API of the zVeinMiner plugin. It provides
 * methods to manipulate the size of the vein that can be mined by a player,
 * and to apply a {@link VeinPreset} to a given {@link ItemStack}.
 *
 * @author Maxlego08
 */
public interface VeinManager extends Listener {

    /**
     * Retrieves the {@link VeinKeys} instance that is used internally by
     * this {@link VeinManager} to store and retrieve data from
     * {@link ItemStack}s.
     *
     * @return the {@link VeinKeys} instance used internally by this
     * {@link VeinManager}
     */
    VeinKeys getVeinKeys();

    /**
     * Gets a set of blocks that are part of the same vein as the given
     * {@link Block startBlock}, up to a maximum size of {@code maxVeinSize}.
     *
     * @param startBlock  the block to start the vein search from
     * @param maxVeinSize the maximum size of the vein to search for
     * @return a set of blocks that are part of the same vein as the given
     * start block, up to a maximum size of {@code maxVeinSize}
     */
    Set<Block> getVeinBlocks(Block startBlock, int maxVeinSize);

    /**
     * Applies the given {@link VeinPreset} to the given {@link ItemStack}. This sets the size and tags of the
     * {@link ItemVeinMinerResult} associated with the item stack to the values of the given vein preset.
     *
     * @param itemStack  the item stack to apply the preset to
     * @param veinPreset the vein preset to apply
     */
    void applyPreset(ItemStack itemStack, VeinPreset veinPreset);

    /**
     * Sets the given {@link VeinPreset} to the given {@link ItemStack}, regardless
     * of whether the item stack already has a preset or not. This will overwrite
     * any existing preset.
     *
     * @param itemStack  the item stack to set the preset on
     * @param veinPreset the vein preset to set on the item stack
     */
    void setPreset(ItemStack itemStack, VeinPreset veinPreset);

    /**
     * Retrieves the {@link VeinPreset} associated with the given name.
     *
     * @param name the name of the vein preset to retrieve
     * @return an {@link Optional} containing the {@link VeinPreset} if found, or an empty {@link Optional} if not found
     */
    Optional<VeinPreset> getVeinPreset(String name);

    /**
     * Retrieves the {@link ItemVeinMinerResult} associated with the given {@link ItemStack}, if present.
     *
     * @param itemStack the item stack to retrieve the {@link ItemVeinMinerResult} from
     * @return an {@link Optional} containing the {@link ItemVeinMinerResult} if present, or an empty {@link Optional} if not present
     */
    Optional<ItemVeinMinerResult> getVeinMinerResult(ItemStack itemStack);

    /**
     * Displays the current size of the {@link ItemVeinMinerResult} associated with the
     * item stack in the main hand of the given player.
     *
     * @param player the player to display the current size to
     */
    void displayCurrentSize(Player player);

    /**
     * Change the size of the {@link ItemVeinMinerResult} associated with the given {@link ItemStack}.
     *
     * @param itemStack the item stack to change the size of
     * @param size      the new size
     */
    void changeSize(ItemStack itemStack, int size);

    List<Taggable> getTaggables(String string);


    /**
     * Adds the given {@link Taggable} to the list of tags of the
     * {@link ItemVeinMinerResult} associated with the given {@link ItemStack}.
     * If the tag is already present, this method does nothing and returns false.
     * If the tag is added successfully, this method returns true.
     *
     * @param itemStack the item stack to add the tag to
     * @param taggable  the tag to add
     * @return true if the tag was added successfully, false otherwise
     */
    boolean addTag(ItemStack itemStack, Taggable taggable);

    /**
     * Removes the given {@link Taggable} from the list of tags of the
     * {@link ItemVeinMinerResult} associated with the given {@link ItemStack}.
     * If the tag is not present, this method does nothing and returns false.
     * If the tag is removed successfully, this method returns true.
     *
     * @param itemStack the item stack to remove the tag from
     * @param taggable  the tag to remove
     * @return true if the tag was removed successfully, false otherwise
     */
    boolean removeTag(ItemStack itemStack, Taggable taggable);

    /**
     * Retrieves a list of tag names associated with the item stack in the main hand
     * of the specified player. This list represents the tags applied to the
     * {@link ItemVeinMinerResult} of the item stack.
     *
     * @param playerSender the player whose main hand item stack's tags are to be retrieved
     * @return a list of tag names associated with the item stack in the player's main hand
     */
    List<String> getTags(Player playerSender);
}
