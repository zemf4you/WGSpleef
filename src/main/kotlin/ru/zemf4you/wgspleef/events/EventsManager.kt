@file:Suppress("unused")

package ru.zemf4you.wgspleef.events

import org.bukkit.Bukkit
import org.bukkit.event.Event

object EventsManager {
    fun call(event: Event) =
        Bukkit.getPluginManager().callEvent(event)
}