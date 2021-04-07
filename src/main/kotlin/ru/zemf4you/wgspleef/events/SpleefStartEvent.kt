@file:Suppress("unused")

package ru.zemf4you.wgspleef.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import ru.zemf4you.wgspleef.arenas.Arena

class SpleefStartEvent(val arena: Arena): Event() {

    override fun getHandlers() = HANDLERS

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList() = HANDLERS
    }

}