package fr.maxlego08.veinminer;

import fr.maxlego08.veinminer.api.PlayerData;
import fr.maxlego08.veinminer.api.PlayerManager;
import fr.maxlego08.veinminer.storage.YamlPlayerDataStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of PlayerManager that handles player data and settings.
 */
public class ZPlayerManager implements PlayerManager, Listener {

    private final VeinMinerPlugin plugin;
    private final YamlPlayerDataStorage storage;
    private final Map<UUID, PlayerData> playerDataCache = new ConcurrentHashMap<>();

    public ZPlayerManager(VeinMinerPlugin plugin) {
        this.plugin = plugin;
        this.storage = new YamlPlayerDataStorage(plugin);
    }

    @Override
    public PlayerData getPlayerData(Player player) {
        return playerDataCache.computeIfAbsent(player.getUniqueId(), uuid -> storage.load(uuid));
    }

    @Override
    public Optional<PlayerData> getPlayerData(UUID uuid) {
        return Optional.ofNullable(playerDataCache.get(uuid));
    }

    @Override
    public boolean toggle(Player player) {
        PlayerData data = getPlayerData(player);
        PlayerData newData = data.withToggled();
        playerDataCache.put(player.getUniqueId(), newData);
        return newData.veinMinerEnabled();
    }

    @Override
    public void setEnabled(Player player, boolean enabled) {
        PlayerData data = getPlayerData(player);
        playerDataCache.put(player.getUniqueId(), data.withEnabled(enabled));
    }

    @Override
    public boolean isEnabled(Player player) {
        return getPlayerData(player).veinMinerEnabled();
    }

    @Override
    public void updateStats(Player player, int blocksMined) {
        PlayerData data = getPlayerData(player);
        playerDataCache.put(player.getUniqueId(), data.withStats(blocksMined, true));
    }

    @Override
    public void savePlayerData(UUID uuid) {
        PlayerData data = playerDataCache.get(uuid);
        if (data != null) {
            storage.save(data);
        }
    }

    @Override
    public void loadPlayerData(UUID uuid) {
        playerDataCache.put(uuid, storage.load(uuid));
    }

    @Override
    public void unloadPlayerData(UUID uuid) {
        PlayerData data = playerDataCache.remove(uuid);
        if (data != null) {
            storage.save(data);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        loadPlayerData(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        unloadPlayerData(event.getPlayer().getUniqueId());
    }

    /**
     * Saves all loaded player data. Called on plugin disable.
     */
    public void saveAll() {
        playerDataCache.forEach((uuid, data) -> storage.save(data));
    }
}
