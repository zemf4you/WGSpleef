@file:Suppress("MemberVisibilityCanBePrivate")

package ru.zemf4you.wgspleef.players.attributes

import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import ru.zemf4you.wgspleef.players.InventoryManager
import ru.zemf4you.wgspleef.players.InventoryManager.save

class DefaultPlayerAttributes(
    player: Player,
    override val location: Location? = null,
    override val gameMode: GameMode = GameMode.SURVIVAL,
    override val exp: Float = 0F,
    override val health: Double = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).value,
    override val exhaustion: Float = 0F,
    override val foodLevel: Int = 20,  // It`s max value
    override val saturation: Float = 0F,
    override val allowFlight: Boolean = false,
    override val walkSpeed: Float = 0.2F,  // It`s default value
    override val effects: List<PotionEffect> = emptyList(),
    override val inventoryData: String? = InventoryManager.createInventory().save()
) : PlayerAttributes