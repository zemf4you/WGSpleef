package ru.zemf4you.wgspleef.players

import org.bukkit.entity.Player
import ru.zemf4you.wgspleef.Util.cast
import ru.zemf4you.wgspleef.arenas.Arena
import ru.zemf4you.wgspleef.events.Events
import ru.zemf4you.wgspleef.events.SpleefPlayerJoinEvent
import ru.zemf4you.wgspleef.events.SpleefPlayerLeaveEvent
import ru.zemf4you.wgspleef.players.attributes.PlayerAttributesManager.restore
import ru.zemf4you.wgspleef.players.attributes.PlayerAttributesManager.save
import ru.zemf4you.wgspleef.players.attributes.PlayerAttributesManager.setAttributes
import ru.zemf4you.wgspleef.players.attributes.DefaultPlayerAttributes

class ArenaPlayers(private val arena: Arena): ArrayList<Player>() {
    override fun add(element: Player): Boolean {
        if (element in this)
            return false
        element.save()
        element.setAttributes(DefaultPlayerAttributes(
            player = element,
            location = arena.params.startLocation,
            inventoryData = arena.params.defaultInventoryData
        ))
        Events.callEvent(SpleefPlayerJoinEvent(arena, element))
        return super.add(element)
    }

    override fun remove(element: Player): Boolean {
        if (element !in this)
            return false
        super.remove(element)
        element.restore()
        Events.callEvent(SpleefPlayerLeaveEvent(arena, element))
        return true
    }

    override fun clear() {
        for (player in this.clone())
            this.remove(player)
    }

    override fun clone(): List<Player> =
        super.clone().cast()

}