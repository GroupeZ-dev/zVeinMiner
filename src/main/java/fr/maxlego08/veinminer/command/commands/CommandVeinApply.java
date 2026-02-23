package fr.maxlego08.veinminer.command.commands;

import fr.maxlego08.veinminer.VeinMinerPlugin;
import fr.maxlego08.veinminer.api.Config;
import fr.maxlego08.veinminer.api.enums.Message;
import fr.maxlego08.veinminer.api.enums.Permission;
import fr.maxlego08.veinminer.command.VCommand;
import fr.maxlego08.veinminer.utils.commands.CommandType;

public class CommandVeinApply extends VCommand {

    public CommandVeinApply(VeinMinerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZVEINMINER_APPLY);
        this.addSubCommand("apply", "a");
        this.setDescription(Message.DESCRIPTION_APPLY);
        this.addRequireArg("preset", (a, b) -> Config.veinPresets.keySet().stream().toList());
        this.onlyPlayers();
    }

    @Override
    protected CommandType perform(VeinMinerPlugin plugin) {

        var manager = plugin.getVeinManager();
        String presetName = this.argAsString(0);
        var optional = manager.getVeinPreset(presetName);
        if (optional.isEmpty()) {
            message(sender, Message.PRESET_NOT_FOUND, "%name%", presetName);
            return CommandType.DEFAULT;
        }

        var preset = optional.get();
        var itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType().isAir()) {
            message(sender, Message.PRESET_CANNOT_BE_APPLIED, "%name%", presetName);
            return CommandType.DEFAULT;
        }

        manager.applyPreset(itemStack, preset);
        message(sender, Message.PRESET_APPLY, "%name%", presetName);

        return CommandType.SUCCESS;
    }

}
