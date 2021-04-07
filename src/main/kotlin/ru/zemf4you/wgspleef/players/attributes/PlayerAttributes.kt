package ru.zemf4you.wgspleef.players.attributes

import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.potion.PotionEffect

interface PlayerAttributes {
    val location: Location?
    val gameMode: GameMode
    val exp: Float
    val health: Double
    val exhaustion: Float
    val foodLevel: Int
    val saturation: Float
    val allowFlight: Boolean
    val walkSpeed: Float
    val effects: List<PotionEffect>
    val inventoryData: String?

    operator fun component1(): Location? = location
    operator fun component2(): GameMode = gameMode
    operator fun component3(): Float = exp
    operator fun component4(): Double = health
    operator fun component5(): Float = exhaustion
    operator fun component6(): Int = foodLevel
    operator fun component7(): Float = saturation
    operator fun component8(): Boolean = allowFlight
    operator fun component9(): Float = walkSpeed
    operator fun component10(): List<PotionEffect> = effects
    operator fun component11(): String? = inventoryData
}