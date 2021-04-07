@file:Suppress("MemberVisibilityCanBePrivate")

package ru.zemf4you.wgspleef.players.attributes

import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import ru.zemf4you.wgspleef.players.InventoryManager.restore

object PlayerAttributesManager {
    private val playerAttributes = mutableMapOf<Player, PlayerAttributes>()

    fun Player.save(): Boolean {
        playerAttributes[this] = SavedPlayerAttributes(player)
        return true
    }

    fun Player.restore(): Boolean {
        if (this !in playerAttributes)
            return false
        this.setAttributes(playerAttributes[this]!!)
        playerAttributes.remove(this)
        return true
    }

    fun Player.setAttributes(
        location: Location? = null,
        gameMode: GameMode? = null,
        exp: Float? = null,
        health: Double? = null,
        exhaustion: Float? = null,
        foodLevel: Int? = null,
        saturation: Float? = null,
        allowFlight: Boolean? = null,
        walkSpeed: Float? = null,
        effects: List<PotionEffect>? = null,
        inventoryData: String? = null
    ) {
        location?.let { this.teleport(it) }
        gameMode?.let { this.gameMode = it }
        exp?.let { this.exp = it }
        health?.let { this.health = it }
        exhaustion?.let { this.exhaustion = it }
        foodLevel?.let { this.foodLevel = it }
        saturation?.let { this.saturation = it }
        allowFlight?.let { this.allowFlight = it }
        walkSpeed?.let { this.walkSpeed = it }
        effects?.let {
            this.activePotionEffects.forEach { effect ->
                this.removePotionEffect(effect.type)
            }
            effects.forEach { effect ->
                this.addPotionEffect(effect)
            }
        }
        inventoryData?.let { this.inventory.restore(it) }
            ?: run { this.inventory.clear() }
    }

    fun Player.setAttributes(attributes: PlayerAttributes) {
        val (
            location,
            gameMode,
            exp,
            health,
            exhaustion,
            foodLevel,
            saturation,
            allowFlight,
            walkSpeed,
            effects,
            inventoryData
        ) = attributes
        this.setAttributes(
            location = location,
            gameMode = gameMode,
            exp = exp,
            health = health,
            exhaustion = exhaustion,
            foodLevel = foodLevel,
            saturation = saturation,
            allowFlight = allowFlight,
            walkSpeed = walkSpeed,
            effects = effects,
            inventoryData = inventoryData
        )
    }
}