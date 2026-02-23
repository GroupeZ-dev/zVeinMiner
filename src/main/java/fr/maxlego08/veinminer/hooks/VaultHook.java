package fr.maxlego08.veinminer.hooks;

import fr.maxlego08.veinminer.VeinMinerPlugin;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Hook for Vault economy plugin.
 * Provides economy integration for vein mining costs.
 */
public class VaultHook {

    private final VeinMinerPlugin plugin;
    private Economy economy;
    private boolean available;

    public VaultHook(VeinMinerPlugin plugin) {
        this.plugin = plugin;
        this.available = setupEconomy();
    }

    /**
     * Sets up the Vault economy integration.
     *
     * @return true if economy was successfully set up
     */
    private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();
        plugin.getLogger().info("Hooked into Vault economy: " + economy.getName());
        return true;
    }

    /**
     * Checks if Vault economy is available.
     *
     * @return true if economy is available
     */
    public boolean isAvailable() {
        return available && economy != null;
    }

    /**
     * Gets the player's balance.
     *
     * @param player the player
     * @return the player's balance
     */
    public double getBalance(Player player) {
        if (!isAvailable()) return 0;
        return economy.getBalance(player);
    }

    /**
     * Checks if the player has at least the specified amount.
     *
     * @param player the player
     * @param amount the amount to check
     * @return true if the player has enough money
     */
    public boolean has(Player player, double amount) {
        if (!isAvailable()) return true;
        return economy.has(player, amount);
    }

    /**
     * Withdraws the specified amount from the player's balance.
     *
     * @param player the player
     * @param amount the amount to withdraw
     * @return true if the withdrawal was successful
     */
    public boolean withdraw(Player player, double amount) {
        if (!isAvailable()) return true;
        if (amount <= 0) return true;

        EconomyResponse response = economy.withdrawPlayer(player, amount);
        return response.transactionSuccess();
    }

    /**
     * Deposits the specified amount to the player's balance.
     *
     * @param player the player
     * @param amount the amount to deposit
     * @return true if the deposit was successful
     */
    public boolean deposit(Player player, double amount) {
        if (!isAvailable()) return true;
        if (amount <= 0) return true;

        EconomyResponse response = economy.depositPlayer(player, amount);
        return response.transactionSuccess();
    }

    /**
     * Formats the amount using the economy's currency format.
     *
     * @param amount the amount to format
     * @return the formatted amount
     */
    public String format(double amount) {
        if (!isAvailable()) return String.valueOf(amount);
        return economy.format(amount);
    }
}
