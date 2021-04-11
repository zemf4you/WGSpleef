@file:Suppress("MemberVisibilityCanBePrivate")

package ru.zemf4you.wgspleef.arenas

import org.bukkit.entity.Player
import ru.zemf4you.wgspleef.SpleefPlugin

class ArenaManager(private val plugin: SpleefPlugin) {

    private val paramsMap
        get() = plugin.config.getObject<Map<String, Map<String, Any>>>("arenas")

    private var paramsList = paramsMap.map { (key, value) -> ArenaParams(key, value) }

    var arenas = paramsList.map { Arena(plugin, it) }
        private set

    val freeArenas
        get() = arenas.filter { it.free }

    operator fun contains(arena: Arena) =
        arena in arenas

    operator fun contains(player: Player) =
        findArena(player) != null

    fun reload() {
        this.arenas.forEach { it.purge() }
        this.paramsList = paramsMap.map { (key, value) -> ArenaParams(key, value) }
        this.arenas = paramsList.map { Arena(plugin, it) }
    }

    fun findArena(regionName: String) =
        freeArenas.find { it.params.regionName == regionName }

    fun findArena(player: Player) =
        arenas.find { player in it }

    fun findBestArena() =
        freeArenas.sortedByDescending { it.players.size }.minByOrNull { it.params.minPlayersCount - it.players.size }

}