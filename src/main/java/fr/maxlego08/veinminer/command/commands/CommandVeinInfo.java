package fr.maxlego08.veinminer.command.commands;

import fr.maxlego08.veinminer.VeinMinerPlugin;
import fr.maxlego08.veinminer.api.Taggable;
import fr.maxlego08.veinminer.api.enums.Message;
import fr.maxlego08.veinminer.api.enums.Permission;
import fr.maxlego08.veinminer.command.VCommand;
import fr.maxlego08.veinminer.utils.commands.CommandType;

import java.util.stream.Collectors;

public class CommandVeinInfo extends VCommand {

    public CommandVeinInfo(VeinMinerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZVEINMINER_INFO);
        this.addSubCommand("info", "i");
        this.setDescription(Message.DESCRIPTION_INFO);
        this.onlyPlayers();
    }

    @Override
    protected CommandType perform(VeinMinerPlugin plugin) {

        var itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType().isAir()) {
            message(sender, Message.INFO_NO_DATA);
            return CommandType.DEFAULT;
        }

        var manager = plugin.getVeinManager();
        var optional = manager.getVeinMinerResult(itemStack);

        if (optional.isEmpty() || optional.get().size() == 0) {
            message(sender, Message.INFO_NO_DATA);
            return CommandType.DEFAULT;
        }

        var result = optional.get();

        message(sender, Message.INFO_HEADER);
        message(sender, Message.INFO_SIZE, "%size%", result.size());

        String tags = result.tags().stream()
                .map(Taggable::asString)
                .collect(Collectors.joining(", "));

        message(sender, Message.INFO_TAGS, "%tags%", tags.isEmpty() ? "None" : tags);

        return CommandType.SUCCESS;
    }
}
