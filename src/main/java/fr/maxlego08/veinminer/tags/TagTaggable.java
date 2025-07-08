package fr.maxlego08.veinminer.tags;

import fr.maxlego08.veinminer.api.Taggable;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;

public class TagTaggable implements Taggable {

    private final String name;
    private final Tag<Material> tag;

    public TagTaggable(String name, Tag<Material> tag) {
        this.name = name;
        this.tag = tag;
    }

    @Override
    public boolean isTagged(Block block) {
        return this.tag.isTagged(block.getType());
    }

    @Override
    public String asString() {
        return this.name;
    }
}
