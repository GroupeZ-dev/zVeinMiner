package fr.maxlego08.veinminer;

import fr.maxlego08.veinminer.api.VeinKeys;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public class ZVeinKeys implements VeinKeys {

    private final NamespacedKey sizeKey;
    private final NamespacedKey presetKey;
    private final NamespacedKey taggableKey;

    public ZVeinKeys(Plugin plugin) {
        this.sizeKey = new NamespacedKey(plugin, "size");
        this.presetKey = new NamespacedKey(plugin, "preset");
        this.taggableKey = new NamespacedKey(plugin, "taggable");
    }

    @Override
    public NamespacedKey getSizeKey() {
        return this.sizeKey;
    }

    @Override
    public NamespacedKey getPresetKey() {
        return this.presetKey;
    }

    @Override
    public NamespacedKey getTaggableKey() {
        return this.taggableKey;
    }
}
