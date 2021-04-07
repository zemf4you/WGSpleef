package ru.zemf4you.wgspleef

import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import org.bukkit.Bukkit
import net.milkbowl.vault.economy.Economy


object Dependencies {
    val wg = Bukkit.getPluginManager().getPlugin("WorldGuard") as WorldGuardPlugin
    val vaultEconomy: Economy? by lazy {
        try {
            Bukkit.getServer().servicesManager.getRegistration(Economy::class.java).provider
        } catch (e: Throwable) {
            null
        }
    }
}