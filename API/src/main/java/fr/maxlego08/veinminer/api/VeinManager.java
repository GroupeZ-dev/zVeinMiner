package fr.maxlego08.veinminer.api;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;

import java.util.Set;

public interface VeinManager extends Listener {

    NamespacedKey getVeinKey();

    Set<Block> getVeinBlocks(Block startBlock, int maxVeinSize);

}
