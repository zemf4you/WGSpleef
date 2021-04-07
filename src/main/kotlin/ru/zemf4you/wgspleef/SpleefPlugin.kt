@file:Suppress("RedundantOverride", "unused", "MemberVisibilityCanBePrivate")

package ru.zemf4you.wgspleef

import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import ru.zemf4you.wgspleef.commands.SpleefCommand
import ru.zemf4you.wgspleef.configs.Config
import ru.zemf4you.wgspleef.events.EventListener
import ru.zemf4you.wgspleef.arenas.ArenaManager
import ru.zemf4you.wgspleef.localization.Localization


class SpleefPlugin : JavaPlugin() {

    val config = Config(this, "config")
    var localization = Localization(this, config.getString("lang"))
        private set
    val eventListener = EventListener(this)
    lateinit var arenaManager: ArenaManager


    fun reload(): String {
        config.reload()
        arenaManager.reload()
        localization = Localization(this, config.getString("lang"))
        return localization.reload
    }

    override fun onEnable() {
        this.getCommand("spleef").executor = SpleefCommand(this)
        server.pluginManager.registerEvents(eventListener, this)
        super.onEnable()
        arenaManager = ArenaManager(this)
    }

    override fun onDisable() {
        super.onDisable()
    }

    companion object {
        val instance: SpleefPlugin by lazy { Bukkit.getPluginManager().getPlugin("WGSpleef") as SpleefPlugin }
    }

}