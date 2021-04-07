@file:Suppress("MemberVisibilityCanBePrivate")

package ru.zemf4you.wgspleef.arenas

import org.bukkit.block.Block
import org.bukkit.entity.Player
import ru.zemf4you.wgspleef.Dependencies
import ru.zemf4you.wgspleef.SpleefPlugin
import ru.zemf4you.wgspleef.events.Events
import ru.zemf4you.wgspleef.events.SpleefPlayerWinEvent
import ru.zemf4you.wgspleef.events.SpleefStartEvent
import ru.zemf4you.wgspleef.events.SpleefStopEvent
import ru.zemf4you.wgspleef.localization.Localization
import ru.zemf4you.wgspleef.localization.Localization.Companion.template
import ru.zemf4you.wgspleef.players.ArenaPlayers
import ru.zemf4you.wgspleef.worldguard.Regions

class Arena(private val plugin: SpleefPlugin, val params: ArenaParams) {

    private val locale: Localization
        get() = plugin.localization

    operator fun contains(player: Player): Boolean =
        player in this.players

    val players = ArenaPlayers(this)

    var free: Boolean = true
        private set

    val blocksToRestore = mutableListOf<Block>()

    var cd = params.startCd
        private set

    private var task = {
        if (free) {
            if (players.size >= params.minPlayers) {
                broadcast(locale.wait.template(this@Arena).template("remain" to cd.toString()))
                cd--
                if (cd == 0) {
                    start()
                    broadcast(locale.start.template(this@Arena))
                }
            } else {
                resetCd()
            }
            // Fix minecraft bug
            for (player in players)
                if (player.location.y < params.startLocation.y)
                    player.teleport(params.startLocation)
        } else {
            for (player in players.clone())  // just copy
                if (params.regionName !in Regions.getRegionsName(player))
                    player.sendMessage(leave(player))
        }
    }

    private val timer = plugin.server.scheduler.runTaskTimer(plugin, task, 0, 20)

    private fun resetCd() {
        cd = params.startCd
    }

    // TODO:
    //  Working with chat here looks inappropriate (maybe)
    fun broadcast(message: String) =
        players.forEach { it.sendMessage(message) }

    fun start(): Boolean {
        if (!free)
            return false
        free = false
        Events.callEvent(SpleefStartEvent(this))
        return true
    }

    fun stop(): Boolean {
        if (free)
            return false
        free = true
        players.clear()
        restoreBlocks()
        Events.callEvent(SpleefStopEvent(this))
        return true
    }

    fun join(player: Player): String {
        if (player in players)
            return locale.join.alreadyIn.template(this)
        if (players.size >= params.maxPlayers)
            return locale.arenas.full.template(this)
        if (params.startCdReset)
            resetCd()
        players.add(player)
        return locale.join.success.template(this)
    }

    fun leave(player: Player): String {
        if (player !in players)
            return locale.leave.notIn.template(this)
        if (free && players.size == params.minPlayers)
            broadcast(locale.players.notEnough.template(this))
        players.remove(player)
        return locale.leave.success.template(this)
    }

    fun purge() {
        stop()
        players.clear()
        timer.cancel()
    }

    fun win(player: Player): String? {
        if (player !in players)
            return null
        Dependencies.vaultEconomy?.depositPlayer(player, params.winAmount)
        Events.callEvent(SpleefPlayerWinEvent(this, player))
        stop()
        return locale.end.win.template(this)
    }

    fun lose(player: Player): String? {
        if (player !in players)
            return null
        players.remove(player)
        return locale.end.lose.template(this)
    }

    fun restoreBlocks() {
        for (block in blocksToRestore)
            block.type = params.blockToBreak
        blocksToRestore.clear()
    }

}