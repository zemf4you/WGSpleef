@file:Suppress("unused")

package ru.zemf4you.wgspleef.events

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent
import com.sk89q.worldguard.bukkit.event.block.BreakBlockEvent
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.*
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause
import ru.zemf4you.wgspleef.SpleefPlugin
import ru.zemf4you.wgspleef.localization.Localization.Companion.send
import ru.zemf4you.wgspleef.localization.Localization.Companion.template
import ru.zemf4you.wgspleef.worldguard.Regions.contains


class EventListener(private val plugin: SpleefPlugin) : Listener {

    /*    external influence    */

    @EventHandler
    fun on(event: PlayerQuitEvent) =  // Also on ban
        plugin.arenaManager.findArena(event.player)?.players?.remove(event.player)

    @EventHandler
    fun on(event: PlayerKickEvent) =
        plugin.arenaManager.findArena(event.player)?.players?.remove(event.player)

    @EventHandler
    fun on(event: PlayerToggleFlightEvent) {
        if (event.player in plugin.arenaManager)
            event.isCancelled = true
    }

    @EventHandler
    fun on(event: PlayerGameModeChangeEvent) {
        if (event.player in plugin.arenaManager)
            event.isCancelled = true
    }

    @EventHandler
    fun on(event: PlayerDeathEvent) {
        if (event.entity !is Player)
            return
        val player = event.entity as Player
        if (player in plugin.arenaManager) {
            event.isCancelled = true
//            player.setAttributes(DefaultPlayerAttributes(player))
        }
    }

    @EventHandler
    fun on(event: PlayerTeleportEvent) {
        if (event.player in plugin.arenaManager && event.cause != TeleportCause.PLUGIN)
            event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun on(event: PlayerCommandPreprocessEvent) {
        if (event.message.startsWith("/spleef "))
            return
        plugin.arenaManager.findArena(event.player)?.let { arena ->
            event.isCancelled = true
            event.player.sendMessage(plugin.localization.illegalCommand.template(arena))
        }
    }

    /*    damage cause    */

    @EventHandler
    fun on(event: EntityDamageEvent) {
        if (event.entity !is Player)
            return
        val player = event.entity as Player
        plugin.arenaManager.findArena(player)?.let { arena ->
            when (event.cause) {
                DamageCause.LAVA, DamageCause.FIRE -> arena.lose(player)?.send(player)
                else -> event.isCancelled = true
            }
        }
    }

    /*    invalid usages of items    */

    @EventHandler
    fun on(event: PlayerDropItemEvent) {
        if (event.player in plugin.arenaManager)
            event.isCancelled = true
    }

    @EventHandler
    fun on(event: PlayerAttemptPickupItemEvent) {
        if (event.player in plugin.arenaManager)
            event.isCancelled = true
    }

    @EventHandler
    fun on(event: PlayerItemDamageEvent) {
        if (event.player in plugin.arenaManager)
            event.isCancelled = true
    }

    @EventHandler
    fun on(event: PlayerItemConsumeEvent) {
        if (event.player in plugin.arenaManager)
            event.isCancelled = true
    }

    /*    interact with world    */

    @EventHandler
    fun on(event: PlayerPickupArrowEvent) {
        if (event.player in plugin.arenaManager)
            event.isCancelled = true
    }

    @EventHandler
    fun on(event: PlayerPickupExperienceEvent) {
        if (event.player in plugin.arenaManager)
            event.isCancelled = true
    }

    @EventHandler
    fun on(event: PlayerExpChangeEvent) {
        if (event.player in plugin.arenaManager)
            event.amount = 0
    }

    /*    WorldGuard events    */

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    fun on(event: BreakBlockEvent) {
        event.cause.firstPlayer?.let { player ->
            plugin.arenaManager.findArena(player)?.let { arena ->
                if (!arena.free) {
                    for (block in event.blocks)
                        if (block.type != arena.params.blockToBreak || !arena.params.region.contains(block)) {
                            event.result = Event.Result.DENY
                            return
                        }
                    event.result = Event.Result.ALLOW
                    for (block in event.blocks) {
                        arena.blocksToRestore.add(block)
                        block.type = Material.AIR
                    }
                }
            }
        }
    }

    /*    WGSpleef events    */

    @EventHandler
    fun on(event: SpleefPlayerLeaveEvent) {
        val otherPlayers = event.arena.players.filter { it != event.player }
        if (otherPlayers.size == 1) {
            val winner = otherPlayers.first()
            event.arena.win(winner)?.send(winner)
        } else if (otherPlayers.isEmpty())
            event.arena.stop()
    }

}