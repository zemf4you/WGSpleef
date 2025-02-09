@file:Suppress("MemberVisibilityCanBePrivate")

package ru.zemf4you.wgspleef.players.attributes

import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import ru.zemf4you.wgspleef.players.InventoryManager.save


class SavedPlayerAttributes(player: Player) : PlayerAttributes {
    override val location: Location = player.location
    override val gameMode: GameMode = player.gameMode
    override val exp: Float = player.exp
    override val health: Double = player.health
    override val exhaustion: Float = player.exhaustion
    override val foodLevel: Int = player.foodLevel
    override val saturation: Float = player.saturation
    override val allowFlight: Boolean = player.allowFlight
    override val walkSpeed: Float = player.walkSpeed
    override val effects: List<PotionEffect> = player.activePotionEffects.toList()
    override val inventoryData: String = player.inventory.save()
}