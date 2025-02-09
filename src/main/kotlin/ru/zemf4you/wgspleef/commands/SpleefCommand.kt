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
        arrayOf<String?>("arenas") to ::arenas,
        arrayOf<String?>("list") to ::arenas,
        arrayOf<String?>("players") to ::players,
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
                    !is Player -> locale.stuff.playersOnly
                    else -> plugin.arenaManager.findBestArena()?.join(sender)
                        ?: locale.commands.player.join.fail.allArenasAreFull
                }
            }

            else -> {
                val regionName = args[0]
                when (sender) {
                    !is Player -> locale.stuff.playersOnly
                    else -> plugin.arenaManager.findArena(regionName)?.join(sender)
                        ?: locale.commands.player.join.fail.arenaIsNotExist.template(
                            "arena" to regionName,
                            "region" to regionName
                        )
                }
            }
        }

    }

    private fun leave(sender: CommandSender, args: Array<String>): String {
        return when (sender) {
            !is Player -> locale.stuff.playersOnly
            else -> plugin.arenaManager.findArena(sender)?.leave(sender)
                ?: locale.commands.player.leave.fail.notIn
        }
    }

    private fun arenas(sender: CommandSender, args: Array<String>): String {
        return locale.commands.general.arenas.toString()
    }

    private fun players(sender: CommandSender, args: Array<String>): String? {
        return when {
            args.isEmpty() -> {
                when (sender) {
                    !is Player -> locale.stuff.playersOnly
                    else -> plugin.arenaManager.findArena(sender)
                        ?.let { arena -> locale.commands.general.players.toString(arena) }
                }
            }

            else -> {
                val regionName = args[0]
                when (val arena = plugin.arenaManager.findArena(regionName)) {
                    null -> locale.commands.general.players.fail.arenaIsNotExist.template(
                        "arena" to regionName,
                        "region" to regionName
                    )

                    else -> locale.commands.general.players.toString(arena)
                }
            }
        }
    }

    private fun reload(sender: CommandSender, args: Array<String>): String {
        return when {
            sender.isSpleefAdmin -> plugin.reload()
            else -> locale.stuff.noPermission
        }
    }

    private fun help(sender: CommandSender): String {
        return when {
            sender !is Player -> locale.commands.general.help.console
            sender.isSpleefAdmin -> locale.commands.general.help.admin
            else -> locale.commands.general.help.player
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        sender.sendMessage(executeCommand(sender, args))
        return true
    }

}