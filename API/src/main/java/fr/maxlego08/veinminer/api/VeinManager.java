package fr.maxlego08.veinminer.api;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;

public interface VeinManager extends Listener {

    NamespacedKey getVeinKey();

}
