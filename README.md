# zVeinMiner

A powerful and highly configurable vein mining plugin for Paper/Spigot servers (1.21+).

[![Download on Modrinth](https://img.shields.io/badge/Download-Modrinth-00AF5C?style=for-the-badge&logo=modrinth)](https://modrinth.com/project/zveinminer)

## Features

### Core Features
- **Vein Mining** - Break entire ore veins with a single block break
- **Customizable Presets** - Define multiple presets with different sizes and block types
- **Visual Preview** - See which blocks will be mined when sneaking (glowing effect)
- **BFS Algorithm** - Efficient block detection using Breadth-First Search
- **Enchantment Support** - Full support for Silk Touch and Fortune enchantments

### Protection Integrations
- **WorldGuard** - Respects region protection, only mines blocks the player can break
- **GriefPrevention** - Respects claim protection

### Player Features
- **Toggle System** - Players can enable/disable vein mining with `/zveinminer toggle`
- **Statistics Tracking** - Track total blocks and veins mined per player
- **Persistent Data** - Player preferences and stats saved to YAML files
- **Per-World Control** - Blacklist or whitelist specific worlds

### Economy Integration
- **Vault Support** - Charge players per block or per vein mined
- **Configurable Costs** - Set cost per block and/or cost per vein action

### Tool Management
- **Durability Check** - Prevents mining more blocks than tool durability allows
- **Unbreaking Support** - Properly calculates effective durability with Unbreaking enchantment
- **Creative Mode** - No durability damage in creative mode

### Configuration
- **Sneak Requirement** - Optionally require sneaking to activate vein mining
- **Max Size Limit** - Global limit on vein size
- **Cooldown System** - Configurable cooldown between vein mines
- **Configurable Messages** - All messages can be customized

### API for Developers
- **Custom Events**:
  - `VeinMineStartEvent` - Fired before vein mining starts (cancellable)
  - `VeinMineBlockEvent` - Fired for each block (cancellable)
  - `VeinMineCompleteEvent` - Fired after vein mining completes
- **Service Registration** - Access `VeinManager` and `PlayerManager` via Bukkit's ServicesManager
- **Item Data API** - Apply presets and tags to items programmatically

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/zveinminer` | Show help | `zveinminer.use` |
| `/zveinminer reload` | Reload configuration | `zveinminer.reload` |
| `/zveinminer toggle` | Toggle vein mining on/off | `zveinminer.toggle` |
| `/zveinminer list` | List available presets | `zveinminer.list` |
| `/zveinminer info` | Show info about held item | `zveinminer.info` |
| `/zveinminer stats` | Show your statistics | `zveinminer.stats` |
| `/zveinminer apply <preset>` | Apply preset to held item | `zveinminer.apply` |
| `/zveinminer set <preset>` | Set preset properties to item | `zveinminer.set` |
| `/zveinminer size <size>` | Change vein size for item | `zveinminer.size` |
| `/zveinminer add <tag>` | Add a tag to item | `zveinminer.add` |
| `/zveinminer remove <tag>` | Remove a tag from item | `zveinminer.remove` |
| `/zveinminer give <player> <preset> [material]` | Give item with preset | `zveinminer.give` |

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `zveinminer.use` | Use vein mining | `true` |
| `zveinminer.reload` | Reload configuration | `op` |
| `zveinminer.toggle` | Toggle vein mining | `true` |
| `zveinminer.list` | List presets | `true` |
| `zveinminer.info` | View item info | `true` |
| `zveinminer.stats` | View statistics | `true` |
| `zveinminer.apply` | Apply presets | `op` |
| `zveinminer.set` | Set presets | `op` |
| `zveinminer.size` | Change vein size | `op` |
| `zveinminer.add` | Add tags | `op` |
| `zveinminer.remove` | Remove tags | `op` |
| `zveinminer.give` | Give items with presets | `op` |

## Configuration

### config.yml

```yaml
# Debug settings
enable-debug: false
enable-debug-time: false

# Vein mining settings
vein-mining:
  cooldown: 0                # Cooldown in ticks (0 = disabled)
  max-size-limit: 64         # Maximum vein size
  require-sneak: false       # Require sneaking to activate

# World settings
worlds:
  blacklist:
    - world_the_end
  whitelist-mode: false      # If true, list becomes whitelist

# Drop settings
drops:
  teleport-to-player: false  # Teleport drops to player
  auto-pickup: false         # Auto pickup to inventory

# Tool settings
tools:
  check-durability: true     # Check durability before mining
  apply-damage: true         # Apply durability damage

# Economy settings (requires Vault)
economy:
  enabled: false
  cost-per-block: 0.0
  cost-per-vein: 0.0

# Preset definitions
presets:
  - name: "pickaxe-1"
    size: 8
    tags:
      - COAL_ORES
      - COPPER_ORES
      - DIAMOND_ORES
      - EMERALD_ORES
      - GOLD_ORES
      - IRON_ORES
      - LAPIS_ORES
      - REDSTONE_ORES
      - NETHERITE_SCRAP
```

### Supported Tags

You can use Bukkit material tags (like `COAL_ORES`, `DIAMOND_ORES`) or individual materials (like `DIAMOND_ORE`, `DEEPSLATE_DIAMOND_ORE`).

**Common ore tags:**
- `COAL_ORES` - All coal ore variants
- `COPPER_ORES` - All copper ore variants
- `DIAMOND_ORES` - All diamond ore variants
- `EMERALD_ORES` - All emerald ore variants
- `GOLD_ORES` - All gold ore variants
- `IRON_ORES` - All iron ore variants
- `LAPIS_ORES` - All lapis ore variants
- `REDSTONE_ORES` - All redstone ore variants

## Installation

1. Download the latest version from [Modrinth](https://modrinth.com/project/zveinminer)
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. Configure the plugin in `plugins/zVeinMiner/config.yml`

## Requirements

- **Server:** Paper 1.21+ (or compatible forks)
- **Java:** 21 or higher

### Optional Dependencies

- **WorldGuard** - For region protection support
- **GriefPrevention** - For claim protection support
- **Vault** - For economy integration
- **PlaceholderAPI** - For placeholder support in messages

## API Usage

### Maven/Gradle

The API module is available for developers who want to integrate with zVeinMiner.

### Accessing the API

```java
// Get VeinManager
VeinManager veinManager = Bukkit.getServicesManager()
    .getRegistration(VeinManager.class)
    .getProvider();

// Get PlayerManager
PlayerManager playerManager = Bukkit.getServicesManager()
    .getRegistration(PlayerManager.class)
    .getProvider();
```

### Listening to Events

```java
@EventHandler
public void onVeinMineStart(VeinMineStartEvent event) {
    Player player = event.getPlayer();
    Set<Block> blocks = event.getBlocks();

    // Cancel vein mining
    if (someCondition) {
        event.setCancelled(true);
    }

    // Modify blocks to be mined
    blocks.removeIf(block -> block.getY() < 0);
}

@EventHandler
public void onVeinMineComplete(VeinMineCompleteEvent event) {
    int totalBlocks = event.getTotalBlocksBroken();
    // Log or reward player
}
```

## Support

- **Issues:** [GitHub Issues](https://github.com/Starter-Dev/zVeinMiner/issues)
- **Discord:** [GroupeZ Discord](https://discord.groupez.dev)

## License

This project is licensed under the MIT License.

---

Made with :heart: by [Maxlego08](https://github.com/Maxlego08)
