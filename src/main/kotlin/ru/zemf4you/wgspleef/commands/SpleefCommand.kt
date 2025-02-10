@file:Suppress("UNUSED_PARAMETER")

package ru.zemf4you.wgspleef.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.zemf4you.wgspleef.SpleefPlugin
import ru.zemf4you.wgspleef.localization.Localization
import ru.zemf4you.wgspleef.localization.Localization.Companion.template
import ru.zemf4you.wgspleef.permissions.PermissionsManager.isSpleefAdmin

class SpleefCommand(private val plugin: SpleefPlugin) : CommandExecutor {

    private val locale: Localization
        get() = plugin.localization

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val subCommand = args.firstOrNull()?.lowercase()
        val subArgs = args.drop(1).toTypedArray()
        val response = when (subCommand) {
            "join" -> join(sender, subArgs)
            "leave" -> leave(sender, subArgs)
            "arenas", "list" -> arenas(sender, subArgs)
            "players" -> players(sender, subArgs)
            "reload" -> reload(sender, subArgs)
            else -> help(sender)
        }
        sender.sendMessage(response)
        return true
    }

    private fun join(sender: CommandSender, args: Array<String>): String {
        if (sender !is Player) return locale.stuff.playersOnly

        val regionName = args.firstOrNull()
        val arena = if (regionName != null)
            plugin.arenaManager.findArena(regionName)
                ?: return locale.commands.player.join.fail.arenaIsNotExist.template(
                    "arena" to regionName,
                    "region" to regionName,
                )
        else
            plugin.arenaManager.findBestArena()
                ?: return locale.commands.player.join.fail.allArenasAreFull

        return arena.join(sender)
    }

    private fun leave(sender: CommandSender, args: Array<String>): String {
        if (sender !is Player) return locale.stuff.playersOnly

        val arena = plugin.arenaManager.findArena(sender) ?: return locale.commands.player.leave.fail.notIn
        return arena.leave(sender)
    }

    private fun arenas(sender: CommandSender, args: Array<String>): String =
        locale.commands.general.arenas.toString()

    private fun players(sender: CommandSender, args: Array<String>): String {
        if (sender !is Player) return locale.stuff.playersOnly

        val regionName = args.firstOrNull()
        val arena = if (regionName != null)
            plugin.arenaManager.findArena(regionName)
                ?: return locale.commands.general.players.fail.arenaIsNotExist.template(
                    "arena" to regionName,
                    "region" to regionName,
                )
        else
            plugin.arenaManager.findArena(sender)
                ?: return locale.commands.general.players.fail.arenaIsNotExist


        return locale.commands.general.players.toString(arena)
    }

    private fun reload(sender: CommandSender, args: Array<String>): String =
        if (sender.isSpleefAdmin)
            plugin.reload()
        else
            locale.stuff.noPermission


    private fun help(sender: CommandSender): String = when {
        sender !is Player -> locale.commands.general.help.console
        sender.isSpleefAdmin -> locale.commands.general.help.admin
        else -> locale.commands.general.help.player
    }

}
