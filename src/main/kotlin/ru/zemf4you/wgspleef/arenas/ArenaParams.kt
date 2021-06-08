@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package ru.zemf4you.wgspleef.arenas

import com.sk89q.worldguard.protection.flags.DefaultFlag
import com.sk89q.worldguard.protection.flags.StateFlag
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import ru.zemf4you.wgspleef.Util.cast
import ru.zemf4you.wgspleef.players.InventoryManager
import ru.zemf4you.wgspleef.players.InventoryManager.save
import ru.zemf4you.wgspleef.worldguard.Regions


class ArenaParams(val regionName: String, map: Map<String, Any>) {

    // TODO:
    //  You need to init params as a human
    val minPlayersCount: Int
    val maxPlayersCount: Int
    val blockToBreak: Material
    val startItem: Material
    private lateinit var worldName: String
    val world: World by lazy {
        Bukkit.getWorld(worldName)
    }
    private lateinit var startCoords: Map<String, Double>
    val startLocation: Location by lazy {
        Location(
            world,
            startCoords.getValue("x"),
            startCoords.getValue("y"),
            startCoords.getValue("z")
        )
    }
    val startCountdown: Int
    val startCountdownReset: Boolean
    val reward: Double

    val region: ProtectedRegion
    val defaultInventoryData: String

    init {
        try {
            this.minPlayersCount = map["minPlayersCount"].toString().toInt()
            if (this.minPlayersCount < 2)
                throw InvalidArena("Minimum number of players must be greater than 2 in arena $regionName!")
            this.maxPlayersCount = map["maxPlayersCount"].toString().toInt()
            if (this.maxPlayersCount < this.minPlayersCount)
                throw InvalidArena(
                    "Maximum number of players must be greater than or equal" +
                            "to the minimum number of players in arena $regionName!"
                )
            this.blockToBreak = Material.getMaterial(map["blockToBreak"].toString())
                ?: throw InvalidArena("Invalid block to break in arena $regionName!")
            this.startItem = Material.getMaterial(map["startItem"].toString())
                ?: throw InvalidArena("Invalid start item in arena $regionName!")
            this.worldName = map["world"].toString()
            this.startCoords = map.getValue("startCoords").cast()
            this.startCountdown = map["startCountdown"].toString().toInt()
            this.startCountdownReset = map["startCountdownReset"].toString() == "true"
            this.reward = map["reward"]?.toString()?.toInt()?.toDouble() ?: 0.0
            if (this.reward < 0)
                throw InvalidArena("reward must be greater than zero!")

            region = Regions.getRegionsName(world, regionName)
                ?: throw InvalidArena("Arena $regionName not exists!")
            region.setFlag(DefaultFlag.INTERACT, StateFlag.State.ALLOW)  // IMPORTANT! for breaking blocks with tools

            defaultInventoryData = InventoryManager.createInventory(startItem)!!.save()
        } catch (e: Throwable) {
            throw InvalidArena(e.message)
        }
    }

}