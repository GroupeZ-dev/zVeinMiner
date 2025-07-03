package fr.maxlego08.veinminer.api;

import org.bukkit.NamespacedKey;

/**
 * This interface contains the keys used to store data in an {@link org.bukkit.inventory.ItemStack}. These keys
 * are used to store the size and tags of the {@link ItemVeinMinerResult} associated with the item stack.
 *
 * @author Maxlego08
 */
public interface VeinKeys {

    /**
     * Retrieves the {@link NamespacedKey} used to store the size of the {@link ItemVeinMinerResult}
     * associated with an {@link org.bukkit.inventory.ItemStack}. This key is used to store the size
     * of the {@link ItemVeinMinerResult} associated with the item stack.
     *
     * @return the {@link NamespacedKey} used to store the size of the {@link ItemVeinMinerResult}
     * associated with an {@link org.bukkit.inventory.ItemStack}
     */
    NamespacedKey getSizeKey();

    /**
     * Retrieves the {@link NamespacedKey} used to store the name of the {@link VeinPreset}
     * associated with an {@link org.bukkit.inventory.ItemStack}. This key is used to store
     * the name of the {@link VeinPreset} associated with the item stack.
     *
     * @return the {@link NamespacedKey} used to store the name of the {@link VeinPreset}
     * associated with an {@link org.bukkit.inventory.ItemStack}
     */
    NamespacedKey getPresetKey();

    /**
     * Retrieves the {@link NamespacedKey} used to store the tags on an
     * {@link org.bukkit.inventory.ItemStack}. This key is used to store the tags of the
     * {@link ItemVeinMinerResult} associated with the item stack.
     *
     * @return the {@link NamespacedKey} used to store the tags on an
     * {@link org.bukkit.inventory.ItemStack}
     */
    NamespacedKey getTaggableKey();

}
