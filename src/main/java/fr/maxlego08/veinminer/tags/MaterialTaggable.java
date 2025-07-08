package fr.maxlego08.veinminer.tags;

import fr.maxlego08.veinminer.api.Taggable;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class MaterialTaggable implements Taggable {

    private final Material material;

    public MaterialTaggable(Material material) {
        this.material = material;
    }

    @Override
    public boolean isTagged(Block block) {
        return block.getType() == this.material;
    }

    @Override
    public String asString() {
        return this.material.name();
    }
}
