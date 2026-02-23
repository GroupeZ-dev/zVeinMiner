package fr.maxlego08.veinminer.api.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Enum that contains all the messages of the plugin.
 *
 * @author Maxlego08
 */
public enum Message {

    PREFIX("&8(&6zVeinMiner&8) "),

    COMMAND_SYNTAX_ERROR("&cYou must execute the command like this&7: &a%syntax%"),
    COMMAND_NO_PERMISSION("&cYou do not have permission to run this command."),
    COMMAND_NO_CONSOLE("&cOnly one player can execute this command."),
    COMMAND_NO_ARG("&cImpossible to find the command with its arguments."),
    COMMAND_SYNTAX_HELP("&f%syntax% &7» &7%description%"),

    RELOAD("&aYou have just reloaded the configuration files."),

    DESCRIPTION_RELOAD("Reload configuration files"),
    DESCRIPTION_APPLY("Apply a vein preset to an item"),
    DESCRIPTION_SET("Set a vein preset to an item"),
    DESCRIPTION_ADD("Add a taggable to an item"),
    DESCRIPTION_REMOVE("Remove a taggable from an item"),
    DESCRIPTION_SIZE("Change vein size for an item"),
    DESCRIPTION_TOGGLE("Toggle vein mining on/off"),
    DESCRIPTION_LIST("List available presets"),
    DESCRIPTION_INFO("Show info about the item in hand"),
    DESCRIPTION_GIVE("Give an item with a preset"),
    DESCRIPTION_STATS("Show your vein mining statistics"),

    PRESET_NOT_FOUND("&cThe vein preset &f%name% &cwas not found."),
    PRESET_CANNOT_BE_APPLIED("&cYou cannot apply the vein preset &f%name% &cto this item."),
    PRESET_APPLY("&aYou have just applied the vein preset &f%name% &ato the item."),

    SIZE_CANNOT_BE_CHANGED("&cYou cannot apply a vein size to this item."),
    SIZE_CHANGE("&aYou have just applied the vein size &f%size% &ato the item."),
    SIZE_DISPLAY("&aThe vein size of the item is &f%size%."),
    SIZE_INVALID("&cInvalid vein size."),

    TAG_NOT_FOUND("&cThe tag &f%tag% &cwas not found."),
    TAG_CANNOT_BE_ADDED("&cYou cannot add the tag &f%name% &cto this item."),

    TAG_ADD("&aYou have just added the tag &f%tag% &ato the item."),
    TAG_ADD_ERROR("&cThis item have already the tag &f%tag%."),
    TAG_REMOVE("&aYou have just removed the tag &f%tag% &ato the item."),
    TAG_REMOVE_ERROR("&cThis item don't have the tag &f%tag%."),

    TOGGLE_ENABLED("&aVein mining has been &2enabled&a."),
    TOGGLE_DISABLED("&aVein mining has been &cdisabled&a."),

    LIST_HEADER("&6Available presets:"),
    LIST_ENTRY("&7- &f%name% &7(size: &e%size%&7, tags: &e%tags%&7)"),
    LIST_EMPTY("&cNo presets available."),

    INFO_HEADER("&6Item vein mining info:"),
    INFO_SIZE("&7Vein size: &e%size%"),
    INFO_TAGS("&7Tags: &e%tags%"),
    INFO_PRESET("&7Preset: &e%preset%"),
    INFO_NO_DATA("&cThis item has no vein mining data."),

    GIVE_SUCCESS("&aGave &f%player% &aan item with preset &f%preset%&a."),
    GIVE_PLAYER_NOT_FOUND("&cPlayer &f%player% &cnot found."),

    STATS_HEADER("&6Your vein mining statistics:"),
    STATS_BLOCKS_MINED("&7Total blocks mined: &e%blocks%"),
    STATS_VEINS_MINED("&7Total veins mined: &e%veins%"),

    ECONOMY_NOT_ENOUGH("&cYou don't have enough money. Required: &f%amount%"),
    ECONOMY_CHARGED("&aYou have been charged &f%amount%&a."),

    WORLD_DISABLED("&cVein mining is disabled in this world."),
    ;


    private List<String> messages;
    private String message;
    private MessageType type = MessageType.TCHAT;

    /**
     * Constructs a new Message with the specified message string.
     *
     * @param message the message string.
     */
    Message(String message) {
        this.message = message;
    }

    /**
     * Constructs a new Message with multiple message strings.
     *
     * @param message the array of message strings.
     */
    Message(String... message) {
        this.messages = Arrays.asList(message);
    }

    /**
     * Constructs a new Message with a specific type and multiple message strings.
     *
     * @param type    the type of the message.
     * @param message the array of message strings.
     */
    Message(MessageType type, String... message) {
        this.messages = Arrays.asList(message);
        this.type = type;
    }

    /**
     * Constructs a new Message with a specific type and a single message string.
     *
     * @param type    the type of the message.
     * @param message the message string.
     */
    Message(MessageType type, String message) {
        this.message = message;
        this.type = type;
    }

    /**
     * Gets the message string.
     *
     * @return the message string.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Converts the message to a string.
     *
     * @return the message string.
     */
    public String toMsg() {
        return message;
    }

    /**
     * Gets the message string.
     *
     * @return the message string.
     */
    public String msg() {
        return message;
    }

    /**
     * Gets the list of messages.
     *
     * @return the list of messages.
     */
    public List<String> getMessages() {
        return messages == null ? Collections.singletonList(message) : messages;
    }

    /**
     * Sets the list of messages.
     *
     * @param messages the list of messages.
     */
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    /**
     * Checks if the message contains multiple parts.
     *
     * @return true if the message contains multiple parts, false otherwise.
     */
    public boolean isMessage() {
        return messages != null && messages.size() > 1;
    }

    /**
     * Sets the message string.
     *
     * @param message the message string.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Replaces a substring in the message with another string.
     *
     * @param a the substring to replace.
     * @param b the replacement string.
     * @return the modified message string.
     */
    public String replace(String a, String b) {
        return message.replace(a, b);
    }

    /**
     * Gets the type of the message.
     *
     * @return the type of the message.
     */
    public MessageType getType() {
        return type;
    }

    /**
     * Sets the type of the message.
     *
     * @param type the type of the message.
     */
    public void setType(MessageType type) {
        this.type = type;
    }

}
