@file:Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")

package ru.zemf4you.wgspleef.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.zemf4you.wgspleef.SpleefPlugin
import ru.zemf4you.wgspleef.Util.isPatternFor
import ru.zemf4you.wgspleef.localization.Localization
import ru.zemf4you.wgspleef.localization.Localization.Companion.template
import ru.zemf4you.wgspleef.permissions.PermissionsManager.isSpleefAdmin

class SpleefCommand(private val plugin: SpleefPlugin) : CommandExecutor {

    private val locale: Localization
        get() = plugin.localization

    @Suppress("RemoveExplicitTypeArguments")
    val commands = arrayOf(
        arrayOf<String?>("join") to ::join,
        arrayOf<String?>("join", null) to ::join,
        arrayOf<String?>("leave") to ::leave,
        arrayOf<String?>("list") to ::list,
        arrayOf<String?>("players", null) to ::players,
        arrayOf<String?>("reload") to ::reload,
    )

    private fun executeCommand(sender: CommandSender, args: Array<String>): String {
        return commands.find { it.first.isPatternFor(args) }?.second?.invoke(sender, args) ?: help(sender)
    }

    private fun join(sender: CommandSender, args: Array<String>): String {
        return when {
            args.isEmpty() -> {
                when (sender) {
                    !is Player -> locale.playerOnly
                    else -> plugin.arenaManager.findBestArena()?.join(sender)
                        ?: locale.arenas.notEnough
                }
            }
            else -> {
                val regionName = args[0]
                when (sender) {
                    !is Player -> locale.playerOnly
                    else -> plugin.arenaManager.findArena(regionName)?.join(sender)
                        ?: locale.arenas.notExist.template(
                            mapOf(
                                "arena" to regionName,
                                "region" to regionName
                            )
                        )
                }
            }
        }

    }

    private fun leave(sender: CommandSender, args: Array<String>): String {
        return when (sender) {
            !is Player -> locale.playerOnly
            else -> plugin.arenaManager.findArena(sender)?.leave(sender)
                ?: locale.leave.notIn
        }
    }

    private fun list(sender: CommandSender, args: Array<String>): String {
        return locale.arenas.getArenas()
    }

    private fun players(sender: CommandSender, args: Array<String>): String {
        val regionName = args[0]
        return when (val arena = plugin.arenaManager.findArena(regionName)) {
            null -> locale.arenas.notExist.template(
                mapOf(
                    "arena" to regionName,
                    "region" to regionName
                )
            )
            else -> locale.players.getPlayers(arena)
        }
    }

    private fun reload(sender: CommandSender, args: Array<String>): String {
        return when {
            sender.isSpleefAdmin -> plugin.reload()
            else -> locale.noPermission
        }
    }

    private fun help(sender: CommandSender): String {
        return when {
            sender.isSpleefAdmin -> locale.help.admin
            else -> locale.help.user
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        sender.sendMessage(executeCommand(sender, args))
        return true
    }

}