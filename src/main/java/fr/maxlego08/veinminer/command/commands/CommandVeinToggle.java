package fr.maxlego08.veinminer.command.commands;

import fr.maxlego08.veinminer.VeinMinerPlugin;
import fr.maxlego08.veinminer.api.enums.Message;
import fr.maxlego08.veinminer.api.enums.Permission;
import fr.maxlego08.veinminer.command.VCommand;
import fr.maxlego08.veinminer.utils.commands.CommandType;

public class CommandVeinToggle extends VCommand {

    public CommandVeinToggle(VeinMinerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZVEINMINER_TOGGLE);
        this.addSubCommand("toggle", "t");
        this.setDescription(Message.DESCRIPTION_TOGGLE);
        this.onlyPlayers();
    }

    @Override
    protected CommandType perform(VeinMinerPlugin plugin) {
        boolean enabled = plugin.getPlayerManager().toggle(player);
        message(sender, enabled ? Message.TOGGLE_ENABLED : Message.TOGGLE_DISABLED);
        return CommandType.SUCCESS;
    }
}
