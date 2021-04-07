@file:Suppress("unused")

package ru.zemf4you.wgspleef.permissions

import org.bukkit.permissions.Permission

enum class Permissions(val permission: Permission) {
    PLAYER(Permission("wgspleef.player", "required to play")),
    ADMIN(Permission("wgspleef.admin", "required to create and configure arenas and reload plugin"))
}