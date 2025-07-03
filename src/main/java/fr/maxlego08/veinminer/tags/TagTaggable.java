package fr.maxlego08.veinminer.tags;

import fr.maxlego08.veinminer.api.Taggable;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;

public class TagTaggable implements Taggable {

    private final Tag<Material> tag;

    public TagTaggable(Tag<Material> tag) {
        this.tag = tag;
    }

    @Override
    public boolean isTagged(Block block) {
        return this.tag.isTagged(block.getType());
    }
}
