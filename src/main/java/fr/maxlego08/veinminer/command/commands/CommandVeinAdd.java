package fr.maxlego08.veinminer.command.commands;

import fr.maxlego08.veinminer.VeinMinerPlugin;
import fr.maxlego08.veinminer.api.enums.Message;
import fr.maxlego08.veinminer.api.enums.Permission;
import fr.maxlego08.veinminer.command.VCommand;
import fr.maxlego08.veinminer.utils.TagRegistry;
import fr.maxlego08.veinminer.utils.commands.CommandType;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.stream.Stream;

public class CommandVeinAdd extends VCommand {

    public CommandVeinAdd(VeinMinerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZVEINMINER_ADD);
        this.addSubCommand("add", "a");
        this.setDescription(Message.DESCRIPTION_ADD);
        this.addRequireArg("tag", (a, b) -> Stream.concat(TagRegistry.getTags().stream(), Arrays.stream(Material.values()).filter(Material::isBlock).filter(e -> !e.isAir()).map(Enum::name)).toList());
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

        message(sender, manager.addTag(itemStack, taggable) ? Message.TAG_ADD : Message.TAG_ADD_ERROR, "%tag%", tag);

        return CommandType.SUCCESS;
    }

}
