package ru.zemf4you.wgspleef.permissions

import org.bukkit.entity.Player
import org.bukkit.permissions.Permissible

object PermissionsManager {

    val Permissible.isSpleefPlayer: Boolean
        get() = this is Player && this.hasPermission(Permissions.PLAYER.permission)

    val Permissible.isSpleefAdmin: Boolean
        get() = this.hasPermission(Permissions.ADMIN.permission)

}