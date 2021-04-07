@file:Suppress("MemberVisibilityCanBePrivate")

package ru.zemf4you.wgspleef.configs

import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.YamlConfiguration
import ru.zemf4you.wgspleef.SpleefPlugin
import ru.zemf4you.wgspleef.Util.cast
import ru.zemf4you.wgspleef.localization.Localization.Companion.toMessage
import java.io.File


class Config(private val plugin: SpleefPlugin, val configName: String) : YamlConfiguration() {

    private val file = File(plugin.dataFolder, "$configName.yml")
    val exists: Boolean
        get() = file.exists()

    init {
        load()
    }

    fun load() {
        if (!exists)
            copyFromPlugin()
        this.load(file)
    }

    fun reload() = load()

    fun copyFromPlugin() {
        file.parentFile.mkdir()
        file.writeBytes(plugin.getResource("$configName.yml").readBytes())
    }

    fun getMessage(path: String, colorChar: Char = '&') =
        this.get(path)?.toString()?.toMessage(colorChar) ?: throw InvalidConfig(path)

    fun <T> getObject(path: String): T =
        this.getConfigurationSection(path).getValues(false).mapValues { (key, value) ->
            if (value is MemorySection)
                getObject<Map<String, Any>>("$path.$key")
            else
                value
        }.cast()

}