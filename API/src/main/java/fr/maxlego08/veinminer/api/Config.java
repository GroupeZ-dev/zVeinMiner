package fr.maxlego08.veinminer.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

    // Debug settings
    public static boolean enableDebug = false;
    public static boolean enableDebugTime = false;

    // Vein presets
    public static Map<String, VeinPreset> veinPresets = new HashMap<>();

    // Vein mining settings
    public static int cooldown = 0;
    public static int maxSizeLimit = 64;
    public static boolean requireSneak = false;

    // World settings
    public static List<String> worldBlacklist = new ArrayList<>();
    public static boolean worldWhitelistMode = false;

    // Drop settings
    public static boolean teleportToPlayer = false;
    public static boolean autoPickup = false;

    // Tool settings
    public static boolean checkDurability = true;
    public static boolean applyDamage = true;

    // Economy settings
    public static boolean economyEnabled = false;
    public static double costPerBlock = 0.0;
    public static double costPerVein = 0.0;
}
