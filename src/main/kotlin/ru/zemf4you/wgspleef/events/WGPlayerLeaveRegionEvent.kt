@file:Suppress("unused")

package ru.zemf4you.wgspleef.events

import com.sk89q.worldguard.protection.regions.ProtectedRegion
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

// TODO: use this
class WGPlayerLeaveRegionEvent(val region: ProtectedRegion, val player: Player) : Event() {

    override fun getHandlers() = HANDLERS

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList() = HANDLERS
    }

}