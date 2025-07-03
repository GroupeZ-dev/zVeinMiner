package fr.maxlego08.veinminer.api;

import org.bukkit.block.Block;

import java.util.List;

public interface VeinPreset {

    String getName();

    int getMaxVeinSize();

    List<Taggable> getTags();

    boolean isTagged(Block block);

}
