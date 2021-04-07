@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package ru.zemf4you.wgspleef.worldguard

import com.sk89q.worldguard.bukkit.RegionContainer
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Player
import ru.zemf4you.wgspleef.Dependencies


object Regions {

    private val container: RegionContainer by lazy { Dependencies.wg.regionContainer }

    fun exists(world: World, name: String): Boolean =
        getRegionsName(world, name) != null

    fun getRegionsName(world: World, playerName: String): ProtectedRegion? =
        Dependencies.wg.getRegionManager(world).getRegion(playerName)

    fun getRegionsName(location: Location): List<String> {
        val query = container.createQuery()
        return query.getApplicableRegions(location).map { it.id }
    }

    fun getRegionsName(block: Block): List<String> =
        getRegionsName(block.location)

    fun getRegionsName(player: Player): List<String> {
        if (!player.isOnline)
            return emptyList()
        return getRegionsName(player.location)
    }

    fun ProtectedRegion.contains(location: Location): Boolean =
        this.contains(location.x.toInt(), location.y.toInt(), location.z.toInt())

    fun ProtectedRegion.contains(block: Block): Boolean =
        this.contains(block.location)

    fun ProtectedRegion.hasPermission(player: Player): Boolean {  // TODO
        return player.name in this.ownersList + this.membersList
                || player.hasPermission("worldguard.region.bypass.*")
    }

    val ProtectedRegion.ownersList: List<String>
        get() = this.owners.players.toList()

    val ProtectedRegion.membersList: List<String>
        get() = this.members.players.toList()

}