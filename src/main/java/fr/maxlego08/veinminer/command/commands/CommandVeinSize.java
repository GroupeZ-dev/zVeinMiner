package fr.maxlego08.veinminer.command.commands;

import fr.maxlego08.veinminer.VeinMinerPlugin;
import fr.maxlego08.veinminer.api.enums.Message;
import fr.maxlego08.veinminer.api.enums.Permission;
import fr.maxlego08.veinminer.command.VCommand;
import fr.maxlego08.veinminer.utils.commands.CommandType;

import java.util.List;

public class CommandVeinSize extends VCommand {

    public CommandVeinSize(VeinMinerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZVEINMINER_SIZE);
        this.addSubCommand("size", "s");
        this.setDescription(Message.DESCRIPTION_SIZE);
        this.addOptionalArg("size", (a, b) -> List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
        this.onlyPlayers();
    }

    @Override
    protected CommandType perform(VeinMinerPlugin plugin) {

        var manager = plugin.getVeinManager();
        int size = this.argAsInteger(0, -1);
        if (size == -1) {
            manager.displayCurrentSize(this.player);
            return CommandType.SUCCESS;
        }

        if (size < 0) {
            message(sender, Message.SIZE_INVALID);
            return CommandType.DEFAULT;
        }

        var itemStack = this.player.getInventory().getItemInMainHand();
        if (itemStack.getType().isAir()) {
            message(sender, Message.SIZE_CANNOT_BE_CHANGED);
            return CommandType.DEFAULT;
        }

        manager.changeSize(itemStack, size);
        message(sender, Message.SIZE_CHANGE, "%size%", size);

        return CommandType.SUCCESS;
    }

}
