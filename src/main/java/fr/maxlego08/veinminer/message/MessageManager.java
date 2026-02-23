package fr.maxlego08.veinminer.message;

import fr.maxlego08.veinminer.VeinMinerPlugin;
import fr.maxlego08.veinminer.api.enums.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Manages configurable messages for the plugin.
 * Loads messages from messages.yml and updates the Message enum values.
 */
public class MessageManager {

    private final VeinMinerPlugin plugin;
    private final File messagesFile;
    private FileConfiguration messagesConfig;

    public MessageManager(VeinMinerPlugin plugin) {
        this.plugin = plugin;
        this.messagesFile = new File(plugin.getDataFolder(), "messages.yml");
    }

    /**
     * Loads messages from the messages.yml file.
     * Creates the file with defaults if it doesn't exist.
     */
    public void loadMessages() {
        if (!messagesFile.exists()) {
            saveDefaultMessages();
        }

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

        // Update Message enum values from config
        for (Message message : Message.values()) {
            String path = message.name().toLowerCase().replace("_", "-");

            if (messagesConfig.isList(path)) {
                List<String> messages = messagesConfig.getStringList(path);
                if (!messages.isEmpty()) {
                    message.setMessages(messages);
                }
            } else if (messagesConfig.contains(path)) {
                String value = messagesConfig.getString(path);
                if (value != null) {
                    message.setMessage(value);
                }
            }
        }
    }

    /**
     * Saves the default messages to messages.yml.
     */
    private void saveDefaultMessages() {
        try {
            messagesFile.getParentFile().mkdirs();
            messagesFile.createNewFile();

            YamlConfiguration config = new YamlConfiguration();

            for (Message message : Message.values()) {
                String path = message.name().toLowerCase().replace("_", "-");

                if (message.isMessage()) {
                    config.set(path, message.getMessages());
                } else {
                    config.set(path, message.getMessage());
                }
            }

            config.save(messagesFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save messages.yml: " + e.getMessage());
        }
    }

    /**
     * Reloads messages from the file.
     */
    public void reloadMessages() {
        loadMessages();
    }
}
