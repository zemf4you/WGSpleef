@file:Suppress("MemberVisibilityCanBePrivate")

package ru.zemf4you.wgspleef.arenas

import org.bukkit.block.Block
import org.bukkit.entity.Player
import ru.zemf4you.wgspleef.Dependencies
import ru.zemf4you.wgspleef.SpleefPlugin
import ru.zemf4you.wgspleef.events.EventsManager
import ru.zemf4you.wgspleef.events.SpleefPlayerWinEvent
import ru.zemf4you.wgspleef.events.SpleefStartEvent
import ru.zemf4you.wgspleef.events.SpleefStopEvent
import ru.zemf4you.wgspleef.localization.Localization
import ru.zemf4you.wgspleef.localization.Localization.Companion.template
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

    var countdown = params.startCountdown
        private set

    private var task = {
        if (free) {
            if (players.size >= params.minPlayersCount) {
                broadcast(locale.game.countdown.template(this@Arena))
                countdown--
                if (countdown == 0) {
                    start()
                    broadcast(locale.game.countdown.template(this@Arena))
                }
            } else {
                resetCd()
            }
        } else {
            for (player in players.clone())  // just copy because we can change original players in loop
                if (params.regionName !in Regions.getRegionsName(player))
                    player.sendMessage(leave(player))
        }
    }

    private val timer = plugin.server.scheduler.runTaskTimer(plugin, task, 0, 20)

    private fun resetCd() {
        countdown = params.startCountdown
    }

    // TODO:
    //  Working with chat here looks inappropriate (maybe)
    fun broadcast(message: String) =
        players.forEach { it.sendMessage(message) }

    fun start(): Boolean {
        if (!free)
            return false
        free = false
        EventsManager.call(SpleefStartEvent(this))
        return true
    }

    fun stop(): Boolean {
        if (free)
            return false
        free = true
        players.clear()
        restoreBlocks()
        EventsManager.call(SpleefStopEvent(this))
        return true
    }

    fun join(player: Player): String {
        if (player in players)
            return locale.commands.player.join.fail.alreadyIn.template(this)
        if (players.size >= params.maxPlayersCount)
            return locale.commands.player.join.fail.arenaIsFull.template(this)
        if (params.startCountdownReset)
            resetCd()
        players.add(player)
        return locale.commands.player.join.success.template(this)
    }

    fun leave(player: Player): String {
        if (player !in players)
            return locale.commands.player.leave.fail.notIn.template(this)
        players.remove(player)
        if (free && players.size == params.minPlayersCount - 1)
            broadcast(locale.game.notEnoughPlayers.template(this))
        return locale.commands.player.leave.success.template(this)
    }

    fun purge() {
        stop()
        players.clear()
        timer.cancel()
    }

    fun win(player: Player): String? {
        if (player !in players)
            return null
        EventsManager.call(SpleefPlayerWinEvent(this, player))
        stop()
        Dependencies.vaultEconomy?.let { economy ->
            economy.depositPlayer(player, params.reward)
            return locale.game.end.win.reward.template(this)
        }
        return locale.game.end.win.noReward.template(this)
    }

    fun lose(player: Player): String? {
        if (player !in players)
            return null
        players.remove(player)
        return locale.game.end.lose.template(this)
    }

    fun restoreBlocks() {
        for (block in blocksToRestore)
            block.type = params.blockToBreak
        blocksToRestore.clear()
    }

}