package fr.maxlego08.veinminer.api;

import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.Set;

public interface VeinManager extends Listener {

    VeinKeys getVeinKeys();

    Set<Block> getVeinBlocks(Block startBlock, int maxVeinSize);

    void applyPreset(ItemStack itemStack, VeinPreset veinPreset);

    Optional<VeinPreset> getVeinPreset(String name);

    Optional<ItemVeinMinerResult> getVeinMinerResult(ItemStack itemStack);

}
