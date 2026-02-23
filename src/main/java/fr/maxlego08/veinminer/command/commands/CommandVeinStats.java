package fr.maxlego08.veinminer.command.commands;

import fr.maxlego08.veinminer.VeinMinerPlugin;
import fr.maxlego08.veinminer.api.PlayerData;
import fr.maxlego08.veinminer.api.enums.Message;
import fr.maxlego08.veinminer.api.enums.Permission;
import fr.maxlego08.veinminer.command.VCommand;
import fr.maxlego08.veinminer.utils.commands.CommandType;

public class CommandVeinStats extends VCommand {

    public CommandVeinStats(VeinMinerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZVEINMINER_STATS);
        this.addSubCommand("stats", "statistics");
        this.setDescription(Message.DESCRIPTION_STATS);
        this.onlyPlayers();
    }

    @Override
    protected CommandType perform(VeinMinerPlugin plugin) {

        var playerManager = plugin.getPlayerManager();
        PlayerData data = playerManager.getPlayerData(player);

        message(sender, Message.STATS_HEADER);
        message(sender, Message.STATS_BLOCKS_MINED, "%blocks%", data.totalBlocksMined());
        message(sender, Message.STATS_VEINS_MINED, "%veins%", data.totalVeinsMined());

        return CommandType.SUCCESS;
    }
}
