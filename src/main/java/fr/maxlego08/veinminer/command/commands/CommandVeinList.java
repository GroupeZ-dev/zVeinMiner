package fr.maxlego08.veinminer.command.commands;

import fr.maxlego08.veinminer.VeinMinerPlugin;
import fr.maxlego08.veinminer.api.Config;
import fr.maxlego08.veinminer.api.Taggable;
import fr.maxlego08.veinminer.api.VeinPreset;
import fr.maxlego08.veinminer.api.enums.Message;
import fr.maxlego08.veinminer.api.enums.Permission;
import fr.maxlego08.veinminer.command.VCommand;
import fr.maxlego08.veinminer.utils.commands.CommandType;

import java.util.stream.Collectors;

public class CommandVeinList extends VCommand {

    public CommandVeinList(VeinMinerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZVEINMINER_LIST);
        this.addSubCommand("list", "l");
        this.setDescription(Message.DESCRIPTION_LIST);
    }

    @Override
    protected CommandType perform(VeinMinerPlugin plugin) {

        if (Config.veinPresets.isEmpty()) {
            message(sender, Message.LIST_EMPTY);
            return CommandType.SUCCESS;
        }

        message(sender, Message.LIST_HEADER);

        for (VeinPreset preset : Config.veinPresets.values()) {
            String tags = preset.getTags().stream()
                    .map(Taggable::asString)
                    .collect(Collectors.joining(", "));

            message(sender, Message.LIST_ENTRY,
                    "%name%", preset.getName(),
                    "%size%", preset.getMaxVeinSize(),
                    "%tags%", tags);
        }

        return CommandType.SUCCESS;
    }
}
