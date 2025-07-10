package fr.maxlego08.veinminer.command.commands;

import fr.maxlego08.veinminer.VeinMinerPlugin;
import fr.maxlego08.veinminer.api.enums.Message;
import fr.maxlego08.veinminer.api.enums.Permission;
import fr.maxlego08.veinminer.command.VCommand;
import fr.maxlego08.veinminer.utils.commands.CommandType;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandVeinRemove extends VCommand {

    public CommandVeinRemove(VeinMinerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZVEINMINER_REMOVE);
        this.addSubCommand("remove", "r");
        this.setDescription(Message.DESCRIPTION_REMOVE);
        this.addRequireArg("tag", (sender, b) -> {
            if (sender instanceof Player playerSender) {
                return plugin.getVeinManager().getTags(playerSender);
            }
            return List.of();
        });
        this.onlyPlayers();
    }


    @Override
    protected CommandType perform(VeinMinerPlugin plugin) {

        var manager = plugin.getVeinManager();
        String tag = this.argAsString(0);
        var taggable = plugin.toTag(tag);
        if (taggable == null) {
            message(sender, Message.TAG_NOT_FOUND, "%tag%", tag);
            return CommandType.DEFAULT;
        }

        var itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType().isAir()) {
            message(sender, Message.TAG_CANNOT_BE_ADDED, "%tag%", tag);
            return CommandType.DEFAULT;
        }

        message(sender, manager.removeTag(itemStack, taggable) ? Message.TAG_REMOVE : Message.TAG_REMOVE_ERROR, "%tag%", tag);

        return CommandType.SUCCESS;
    }

}
