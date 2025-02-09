@file:Suppress("unused")

package ru.zemf4you.wgspleef.events

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import ru.zemf4you.wgspleef.arenas.Arena

class SpleefPlayerWinEvent(val arena: Arena, val player: Player) : Event() {

    override fun getHandlers() = HANDLERS

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList() = HANDLERS
    }

}