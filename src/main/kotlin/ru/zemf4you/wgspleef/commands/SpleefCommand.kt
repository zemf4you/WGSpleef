package ru.zemf4you.wgspleef.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.zemf4you.wgspleef.SpleefPlugin
import ru.zemf4you.wgspleef.localization.Localization
import ru.zemf4you.wgspleef.localization.Localization.Companion.template

class SpleefCommand(private val plugin: SpleefPlugin) : CommandExecutor {

    private val locale: Localization
        get() = plugin.localization

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        when (args.size) {
            1 -> {
                when (args[0].toLowerCase()) {
                    "join" -> {
                        if (sender !is Player)
                            return true
                        sender.sendMessage(
                            plugin.arenaManager.findBestArena()?.join(sender)
                                ?: locale.arenas.notEnough
                        )
                        return true
                    }
                    "leave" -> {
                        if (sender !is Player)
                            return true
                        sender.sendMessage(
                            plugin.arenaManager.findArena(sender)?.leave(sender)
                                ?: locale.leave.notIn
                        )
                        return true
                    }
                    "list" -> {
                        sender.sendMessage(locale.arenas.getArenas())
                        return true
                    }
                    "reload" -> {
                        // TODO:
                        //  create PermissionsManager
                        /*
                        sender.sendMessage(plugin.reload())
                        return true
                        */
                    }
                }
            }
            2 -> {
                val region = args[1]
                when (args[0].toLowerCase()) {
                    "players" -> {
                        sender.sendMessage(when (val game = plugin.arenaManager.findArena(region)) {
                            null -> locale.arenas.notExist.template("arena" to region)
                            else -> locale.players.getPlayers(game)
                        })
                        return true
                    }
                    "join" -> {
                        if (sender !is Player)
                            return true
                        sender.sendMessage(
                            plugin.arenaManager.findArena(region)?.join(sender)
                                ?: locale.arenas.notExist.template("arena" to region)
                        )
                        return true
                    }
                    "remove" -> {
                        // TODO
                    }
                }
            }
        }
        // TODO:
        //  Permission to admin help
        sender.sendMessage(locale.help.user)
        return true
    }
}