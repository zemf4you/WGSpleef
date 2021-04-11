@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter", "unused")

package ru.zemf4you.wgspleef.localization

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import ru.zemf4you.wgspleef.SpleefPlugin
import ru.zemf4you.wgspleef.arenas.Arena
import ru.zemf4you.wgspleef.arenas.ArenaManager
import ru.zemf4you.wgspleef.configs.Config

class Localization(private val plugin: SpleefPlugin, val lang: String = "ru") {

    private val config = Config(plugin, "lang/$lang")

    inner class Structure {
        val header: String
        val item: String
        val separator: String

        constructor(header: String, item: String, separator: String) {
            this.header = header
            this.item = item
            this.separator = separator
        }

        constructor(path: String) {
            val map = config.getObject<Map<String, String>>(path)
            this.header = map.getValue("header")
            this.item = map.getValue("item")
            this.separator = map.getValue("separator")
        }

        fun getItem(placeholder: String): String {
            return this.item.template("item" to placeholder)
        }
    }

    inner class Stuff {
        val prefix = config.getMessage("stuff.prefix")
        val noPermission = getString("stuff.noPermission")
        val playersOnly = getString("stuff.playersOnly")
        val unavailableCommand = getString("stuff.unavailableCommand")
    }

    inner class Game {
        val notEnoughPlayers = getString("game.notEnoughPlayers")
        val countdown = getString("game.countdown")
        val start = getString("game.start")
        val end = End()

        inner class End {
            val lose = getString("game.end.lose")
            val win = Win()

            inner class Win {
                val reward = getString("game.end.win.reward")
                val noReward = getString("game.end.win.noReward")
            }
        }
    }

    inner class Commands {
        val general = General()
        val player = Player()
        val admin = Admin()

        inner class General {
            val help = Help()
            val arenas = Arenas()
            val players = Players()

            inner class Help {
                val player = getString("commands.general.help.player")
                val admin = getString("commands.general.help.admin")
                val console = getString("commands.general.help.console")
            }

            inner class Arenas {
                val structure = Structure("commands.general.arenas.structure")
                override fun toString(): String {
                    return structure.header.template(
                        "arenasCount" to plugin.arenaManager.arenas.size.toString(),
                        "freeArenasCount" to plugin.arenaManager.freeArenas.size.toString()
                    ) + plugin.arenaManager.arenas.joinToString(structure.separator) {
                        structure.getItem(it.params.regionName).template(it)
                    }
                }
            }

            inner class Players {
                val structure = Structure("commands.general.players.structure")
                val fail = Fail()
                inner class Fail {
                    val arenaIsNotExist = getString("commands.general.players.fail.arenaIsNotExist")
                }
                fun toString(arena: Arena): String {
                    return structure.header.template(arena) +
                            arena.players.joinToString(structure.separator) {
                                structure.getItem(it.name).template(arena)
                            }
                }
            }
        }

        inner class Player {
            val join = Join()
            val leave = Leave()

            inner class Join {
                val success = getString("commands.player.join.success")
                val fail = Fail()

                inner class Fail {
                    val alreadyIn = getString("commands.player.join.fail.alreadyIn")
                    val arenaIsNotExist = getString("commands.player.join.fail.arenaIsNotExist")
                    val arenaIsFull = getString("commands.player.join.fail.arenaIsFull")
                    val allArenasAreFull = getString("commands.player.join.fail.allArenasAreFull")
                    val unavailable = getString("commands.player.join.fail.unavailable")
                }
            }

            inner class Leave {
                val success = getString("commands.player.leave.success")
                val fail = Fail()

                inner class Fail {
                    val notIn = getString("commands.player.leave.fail.notIn")
                }
            }
        }

        inner class Admin {
            val reload = getString("commands.admin.reload")
        }
    }

    val stuff: Stuff
    val game: Game
    val commands: Commands

    init {
        try {
            stuff = Stuff()
            game = Game()
            commands = Commands()
        } catch (e: Throwable) {
            throw InvalidLocalization(e.message)
        }
    }

    val globalVariables= arrayOf(
        "prefix" to config.getMessage("stuff.prefix")
    )

    fun getString(path: String) = config.getMessage(path).template(*globalVariables)

    companion object {

        fun String.template(arena: Arena): String =
            this.template(
                "playersCount" to arena.players.size.toString(),
                "minPlayersCount" to arena.params.minPlayersCount.toString(),
                "maxPlayersCount" to arena.params.maxPlayersCount.toString(),
                "countdown" to arena.countdown.toString(),
                "arena" to arena.params.regionName,
                "region" to arena.params.regionName,
                "reward" to arena.params.reward.toString()
            )

        fun String.template(vararg placeholders: Pair<String, String>): String {
            var string = this
            placeholders.forEach { (key, value) -> string = string.replace("\$$key", value) }
            return string
        }

        fun String.toMessage(char: Char = '&'): String =
            ChatColor.translateAlternateColorCodes(char, this)

        fun String.send(player: Player) =
            player.sendMessage(this)

    }

}