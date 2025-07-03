package fr.maxlego08.veinminer;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VeinVisualizer {

    private static final BlockFace[] ADJACENT_FACES = new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
    private final VeinMinerPlugin plugin;
    private final Player player;
    private final Team team;
    private final List<Shulker> shulkers = new ArrayList<>();

    public VeinVisualizer(VeinMinerPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;

        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        this.team = board.getTeams().stream().filter(e -> e.getName().equals("zveinminerteam")).findFirst().orElseGet(() -> board.registerNewTeam("zveinminerteam"));
        this.team.color(NamedTextColor.WHITE);
    }

    public void spawn(Set<Block> blocks) {

        this.remove();

        Set<Block> borderBlocks = filterBorderBlocks(blocks);

        for (Block block : borderBlocks) {

            // Rework with packet only

            var location = block.getLocation();
            Shulker shulker = location.getWorld().spawn(location, Shulker.class, e -> {
                e.setInvulnerable(true);
                e.setAI(false);
                e.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, -1, 1, false, false));
                e.setInvisible(true);
                e.setCollidable(false);
                e.setMetadata("zveinminer", new FixedMetadataValue(this.plugin, true));
            });

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer == this.player) continue;
                onlinePlayer.hideEntity(this.plugin, shulker);
            }

            String uuid = shulker.getUniqueId().toString();
            this.team.removeEntry(uuid);
            this.team.addEntry(uuid);

            this.shulkers.add(shulker);
        }
    }

    private Set<Block> filterBorderBlocks(Set<Block> blocks) {
        Set<Block> borderBlocks = new HashSet<>();

        for (Block block : blocks) {
            for (BlockFace face : ADJACENT_FACES) {
                Block adjacent = block.getRelative(face);
                if (!blocks.contains(adjacent)) {
                    borderBlocks.add(block);
                    break;
                }
            }
        }

        return borderBlocks;
    }

    public void remove() {
        this.shulkers.forEach(Shulker::remove);
        this.shulkers.clear();
    }
}
