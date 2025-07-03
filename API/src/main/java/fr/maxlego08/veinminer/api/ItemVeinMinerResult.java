package fr.maxlego08.veinminer.api;

import java.util.List;

/**
 * Represents the result of a vein mining operation on an item stack.
 *
 * @author Maxlego08
 */
public record ItemVeinMinerResult(int size, List<Taggable> tags) {
}
