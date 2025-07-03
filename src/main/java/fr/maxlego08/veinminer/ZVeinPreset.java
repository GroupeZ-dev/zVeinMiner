package fr.maxlego08.veinminer;

import fr.maxlego08.veinminer.api.Taggable;
import fr.maxlego08.veinminer.api.VeinPreset;
import org.bukkit.block.Block;

import java.util.List;

public class ZVeinPreset implements VeinPreset {

    private final String name;
    private final int size;
    private final List<Taggable> tags;

    public ZVeinPreset(String name, int size, List<Taggable> tags) {
        this.name = name;
        this.size = size;
        this.tags = tags;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getMaxVeinSize() {
        return this.size;
    }

    @Override
    public List<Taggable> getTags() {
        return this.tags;
    }

    @Override
    public boolean isTagged(Block block) {
        return this.tags.stream().anyMatch(tag -> tag.isTagged(block));
    }

    @Override
    public String toString() {
        return "ZVeinPreset{" + "name='" + name + '\'' + ", size=" + size + ", tags=" + tags + '}';
    }
}
