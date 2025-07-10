package fr.maxlego08.veinminer.command.commands;


import fr.maxlego08.veinminer.VeinMinerPlugin;
import fr.maxlego08.veinminer.api.enums.Permission;
import fr.maxlego08.veinminer.command.VCommand;
import fr.maxlego08.veinminer.utils.commands.CommandType;

public class CommandVein extends VCommand {

    public CommandVein(VeinMinerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZVEINMINER_USE);
        this.addSubCommand(new CommandVeinReload(plugin));
        this.addSubCommand(new CommandVeinApply(plugin));
        this.addSubCommand(new CommandVeinSize(plugin));
        this.addSubCommand(new CommandVeinSet(plugin));
        this.addSubCommand(new CommandVeinAdd(plugin));
    }

    @Override
    protected CommandType perform(VeinMinerPlugin plugin) {
        syntaxMessage();
        return CommandType.SUCCESS;
    }

}
