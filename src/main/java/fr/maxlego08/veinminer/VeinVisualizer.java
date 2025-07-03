package fr.maxlego08.veinminer;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VeinVisualizer {

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

        for (Block block : blocks) {

            // Rework with packet only

            var location = block.getLocation();
            Shulker shulker = location.getWorld().spawn(location, Shulker.class);
            shulker.setInvulnerable(true);
            shulker.setAI(false);
            shulker.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, -1, 1, false, false));
            shulker.setInvisible(true);
            shulker.setCollidable(false);
            shulker.setMetadata("zveinminer", new FixedMetadataValue(this.plugin, true));

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

    public void remove() {
        this.shulkers.forEach(Shulker::remove);
        this.shulkers.clear();
    }
}
