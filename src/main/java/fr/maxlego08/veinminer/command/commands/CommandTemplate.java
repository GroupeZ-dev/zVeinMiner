package fr.maxlego08.veinminer.command.commands;


import fr.maxlego08.veinminer.VeinMinerPlugin;
import fr.maxlego08.veinminer.api.enums.Permission;
import fr.maxlego08.veinminer.command.VCommand;
import fr.maxlego08.veinminer.utils.commands.CommandType;

public class CommandTemplate extends VCommand {

    public CommandTemplate(VeinMinerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZVEINMINER_USE);
        this.addSubCommand(new CommandTemplateReload(plugin));
    }

    @Override
    protected CommandType perform(VeinMinerPlugin plugin) {
        syntaxMessage();
        return CommandType.SUCCESS;
    }

}
