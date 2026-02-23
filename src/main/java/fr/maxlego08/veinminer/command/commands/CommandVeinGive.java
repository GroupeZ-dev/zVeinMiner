package fr.maxlego08.veinminer.command.commands;

import fr.maxlego08.veinminer.VeinMinerPlugin;
import fr.maxlego08.veinminer.api.Config;
import fr.maxlego08.veinminer.api.enums.Message;
import fr.maxlego08.veinminer.api.enums.Permission;
import fr.maxlego08.veinminer.command.VCommand;
import fr.maxlego08.veinminer.utils.commands.CommandType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandVeinGive extends VCommand {

    public CommandVeinGive(VeinMinerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZVEINMINER_GIVE);
        this.addSubCommand("give", "g");
        this.setDescription(Message.DESCRIPTION_GIVE);
        this.addRequireArg("player", (a, b) -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
        this.addRequireArg("preset", (a, b) -> Config.veinPresets.keySet().stream().toList());
        this.addOptionalArg("material", (a, b) -> {
            return java.util.Arrays.stream(Material.values())
                    .filter(m -> m.isItem() && !m.isAir())
                    .map(Material::name)
                    .toList();
        });
    }

    @Override
    protected CommandType perform(VeinMinerPlugin plugin) {

        var manager = plugin.getVeinManager();

        String playerName = this.argAsString(0);
        String presetName = this.argAsString(1);
        String materialName = this.argAsString(2, "DIAMOND_PICKAXE");

        // Find target player
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            message(sender, Message.GIVE_PLAYER_NOT_FOUND, "%player%", playerName);
            return CommandType.DEFAULT;
        }

        // Find preset
        var optional = manager.getVeinPreset(presetName);
        if (optional.isEmpty()) {
            message(sender, Message.PRESET_NOT_FOUND, "%name%", presetName);
            return CommandType.DEFAULT;
        }

        // Parse material
        Material material;
        try {
            material = Material.valueOf(materialName.toUpperCase());
        } catch (IllegalArgumentException e) {
            message(sender, Message.PRESET_CANNOT_BE_APPLIED, "%name%", materialName);
            return CommandType.DEFAULT;
        }

        var preset = optional.get();
        ItemStack itemStack = new ItemStack(material);

        // Apply preset to the item
        manager.setPreset(itemStack, preset);

        // Give item to player
        target.getInventory().addItem(itemStack);

        message(sender, Message.GIVE_SUCCESS, "%player%", target.getName(), "%preset%", presetName);

        return CommandType.SUCCESS;
    }
}
