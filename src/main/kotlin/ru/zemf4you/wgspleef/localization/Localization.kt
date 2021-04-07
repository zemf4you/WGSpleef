@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")

package ru.zemf4you.wgspleef.localization

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import ru.zemf4you.wgspleef.SpleefPlugin
import ru.zemf4you.wgspleef.configs.Config
import ru.zemf4you.wgspleef.arenas.Arena

class Localization(private val plugin: SpleefPlugin, val lang: String = "ru") {

    private val config = Config(plugin, "lang/$lang")

    // TODO:
    //  There are no anonymous classes...
    //  Fuck this shit
    //  Very bad idea with maps and very bad init
    //  UPD: so, inner class sucks too, but a little bit better way to solve it
    //  ...
    //  And I should think about win amount message
    //  because I want to turn Vault into softdependency

    val help: Help

    inner class Help {
        val admin = config.getMessage("help.admin")
        val user = config.getMessage("help.user")
    }

    val join: Join

    inner class Join {
        val alreadyIn = config.getMessage("join.alreadyIn")
        val success = config.getMessage("join.success")
    }

    val leave: Leave

    inner class Leave {
        val notIn = config.getMessage("leave.notIn")
        val success = config.getMessage("leave.success")
    }

    val arenas: Arenas

    inner class Arenas {
        val notExist = config.getMessage("arenas.notExist")
        val notEnough = config.getMessage("arenas.notEnough")
        val full = config.getMessage("arenas.full")
        val header = config.getMessage("arenas.header")
        val arena = config.getMessage("arenas.arena")
        val freeArena = config.getMessage("arenas.freeArena")
        val separator = config.getMessage("arenas.separator")
        fun getArenas() =
            header.template(
                mapOf(
                    "arenasCount" to plugin.arenaManager.arenas.size.toString(),
                    "freeArenasCount" to plugin.arenaManager.freeArenas.size.toString()
                )
            ) + plugin.arenaManager.arenas.joinToString(separator) {
                (if (it.free) freeArena else arena).template(it)
            }
    }

    val end: End

    inner class End {
        val lose = config.getMessage("end.lose")
        val win = config.getMessage("end.win")
    }

    val players: Players

    inner class Players {
        val notEnough = config.getMessage("players.notEnough")
        val header = config.getMessage("players.header")
        val player = config.getMessage("players.player")
        val separator = config.getMessage("players.separator")
        fun getPlayers(arena: Arena) = header +
                arena.players.joinToString(separator) {
                    player.template("player" to it.name)
                }
    }

    val start: String
    val wait: String
    val reload: String
    val noPermission: String
    val playerOnly: String
    val illegalCommand: String

    init {
        try {
            this.help = Help()
            this.join = Join()
            this.leave = Leave()
            this.arenas = Arenas()
            this.end = End()
            this.players = Players()
            this.start = config.getMessage("start")
            this.wait = config.getMessage("wait")
            this.reload = config.getMessage("reload")
            this.noPermission = config.getMessage("noPermission")
            this.playerOnly = config.getMessage("playerOnly")
            this.illegalCommand = config.getMessage("illegalCommand")
        } catch (e: Throwable) {
            throw InvalidLocalization(e.message)
        }
    }

    companion object {

        fun String.template(replace: Map<String, String>): String {
            var string = this
            replace.forEach { (key, value) -> string = string.replace("\$$key", value) }
            return string
        }

        fun String.template(pair: Pair<String, String>): String =
            this.template(mapOf(pair))

        fun String.template(arena: Arena): String =
            this.template(
                mapOf(
                    "players" to arena.players.size.toString(),
                    "min" to arena.params.minPlayers.toString(),
                    "max" to arena.params.maxPlayers.toString(),
                    "remain" to arena.cd.toString(),
                    "arena" to arena.params.regionName,
                    "region" to arena.params.regionName,
                    "amount" to arena.params.winAmount.toString()
                )
            )

        fun String.toMessage(char: Char = '&'): String =
            ChatColor.translateAlternateColorCodes(char, this)

        fun String.send(player: Player) =
            player.sendMessage(this)

    }

}