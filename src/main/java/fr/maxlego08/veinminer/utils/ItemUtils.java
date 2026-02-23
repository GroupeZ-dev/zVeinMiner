package fr.maxlego08.veinminer.utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

/**
 * Utility class for item-related operations.
 * Provides methods for durability calculations and checks.
 */
public final class ItemUtils {

    private ItemUtils() {
        // Utility class
    }

    /**
     * Gets the remaining durability of an item.
     *
     * @param item the item to check
     * @return the remaining durability, or -1 if the item has no durability
     */
    public static int getRemainingDurability(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return -1;
        }

        if (!(item.getItemMeta() instanceof Damageable damageable)) {
            return -1;
        }

        int maxDurability = item.getType().getMaxDurability();
        if (maxDurability == 0) {
            return -1;
        }

        return maxDurability - damageable.getDamage();
    }

    /**
     * Checks if an item is unbreakable.
     *
     * @param item the item to check
     * @return true if the item is unbreakable, false otherwise
     */
    public static boolean isUnbreakable(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().isUnbreakable();
    }

    /**
     * Calculates the effective durability considering Unbreaking enchantment.
     * With Unbreaking, there's a chance the tool won't take damage.
     * Average durability multiplier is (level + 1) for tools.
     *
     * @param item   the item to check
     * @param blocks the number of blocks to mine
     * @return the estimated number of blocks that can be mined, or -1 for unbreakable/non-damageable items
     */
    public static int calculateEffectiveDurability(ItemStack item, int blocks) {
        if (isUnbreakable(item)) {
            return blocks;
        }

        int remaining = getRemainingDurability(item);
        if (remaining == -1) {
            return blocks;
        }

        int unbreakingLevel = item.getEnchantmentLevel(Enchantment.UNBREAKING);

        // With Unbreaking, each use has a 1/(level+1) chance to consume durability
        // So on average, we can mine (remaining * (level + 1)) blocks
        int effectiveBlocks = remaining * (unbreakingLevel + 1);

        return Math.min(blocks, effectiveBlocks);
    }

    /**
     * Calculates the maximum number of blocks that can be broken with the current durability.
     *
     * @param item the item to check
     * @return the maximum number of blocks, or Integer.MAX_VALUE for unbreakable items
     */
    public static int getMaxBlocksBreakable(ItemStack item) {
        if (isUnbreakable(item)) {
            return Integer.MAX_VALUE;
        }

        int remaining = getRemainingDurability(item);
        if (remaining == -1) {
            return Integer.MAX_VALUE;
        }

        int unbreakingLevel = item.getEnchantmentLevel(Enchantment.UNBREAKING);

        // With Unbreaking, we can break more blocks on average
        return remaining * (unbreakingLevel + 1);
    }

    /**
     * Checks if an item has Silk Touch enchantment.
     *
     * @param item the item to check
     * @return true if the item has Silk Touch
     */
    public static boolean hasSilkTouch(ItemStack item) {
        if (item == null) {
            return false;
        }
        return item.containsEnchantment(Enchantment.SILK_TOUCH);
    }

    /**
     * Gets the Fortune level of an item.
     *
     * @param item the item to check
     * @return the Fortune level, or 0 if not enchanted
     */
    public static int getFortuneLevel(ItemStack item) {
        if (item == null) {
            return 0;
        }
        return item.getEnchantmentLevel(Enchantment.FORTUNE);
    }
}
