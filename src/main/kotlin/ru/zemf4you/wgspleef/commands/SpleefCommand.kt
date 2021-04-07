package ru.zemf4you.wgspleef.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.zemf4you.wgspleef.SpleefPlugin
import ru.zemf4you.wgspleef.localization.Localization
import ru.zemf4you.wgspleef.localization.Localization.Companion.template
import ru.zemf4you.wgspleef.permissions.PermissionsManager.isSpleefAdmin
import ru.zemf4you.wgspleef.permissions.PermissionsManager.isSpleefPlayer

class SpleefCommand(private val plugin: SpleefPlugin) : CommandExecutor {

    private val locale: Localization
        get() = plugin.localization

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        when (args.size) {
            1 -> {
                when (args[0].toLowerCase()) {
                    "join" -> {
                        sender.sendMessage(
                            when {
                                sender !is Player -> locale.playerOnly
                                sender.isSpleefPlayer ->
                                    plugin.arenaManager.findBestArena()?.join(sender)
                                        ?: locale.arenas.notEnough
                                else -> locale.noPermission
                            }
                        )
                        return true
                    }
                    "leave" -> {
                        sender.sendMessage(
                            when {
                                sender !is Player -> locale.playerOnly
                                sender.isSpleefPlayer ->
                                    plugin.arenaManager.findArena(sender)?.leave(sender)
                                        ?: locale.leave.notIn
                                else -> locale.noPermission
                            }
                        )
                        return true
                    }
                    "list" -> {
                        sender.sendMessage(
                            when {
                                sender.isSpleefPlayer -> locale.arenas.getArenas()
                                else -> locale.noPermission
                            }
                        )
                        return true
                    }
                    "reload" -> {
                        sender.sendMessage(
                            when {
                                sender.isSpleefAdmin -> plugin.reload()
                                else -> locale.noPermission
                            }
                        )
                        return true
                    }
                }
            }
            2 -> {
                val regionName = args[1]
                when (args[0].toLowerCase()) {
                    "players" -> {
                        sender.sendMessage(
                            when {
                                sender.isSpleefPlayer -> {
                                    when (val arena = plugin.arenaManager.findArena(regionName)) {
                                        null -> locale.arenas.notExist.template(
                                            mapOf(
                                                "arena" to regionName,
                                                "region" to regionName
                                            )
                                        )
                                        else -> locale.players.getPlayers(arena)
                                    }
                                }
                                else -> locale.noPermission
                            }
                        )
                        return true
                    }
                    "join" -> {
                        sender.sendMessage(
                            when {
                                sender !is Player -> locale.playerOnly
                                sender.isSpleefPlayer ->
                                    plugin.arenaManager.findArena(regionName)?.join(sender)
                                        ?: locale.arenas.notExist.template(
                                            mapOf(
                                                "arena" to regionName,
                                                "region" to regionName
                                            )
                                        )
                                else -> locale.noPermission
                            }
                        )
                        return true
                    }
                    "remove" -> {
                        // TODO
                    }
                }
            }
        }
        sender.sendMessage(
            when (sender) {
                is Player -> {
                    when {
                        sender.isSpleefAdmin -> locale.help.admin
                        sender.isSpleefPlayer -> locale.help.user
                        else -> locale.noPermission
                    }
                }
                else -> locale.help.admin
            }
        )
        return true
    }
}