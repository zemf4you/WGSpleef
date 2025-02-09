package ru.zemf4you.wgspleef.permissions

import org.bukkit.permissions.Permissible

object PermissionsManager {

    val Permissible.isSpleefAdmin: Boolean
        get() = this.hasPermission(Permissions.ADMIN.permission)

}