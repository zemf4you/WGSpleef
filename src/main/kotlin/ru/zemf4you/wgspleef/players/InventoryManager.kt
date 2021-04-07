@file:Suppress("MemberVisibilityCanBePrivate")

package ru.zemf4you.wgspleef.players

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import ru.zemf4you.wgspleef.Util.cast
import ru.zemf4you.wgspleef.Util.fromBase64ToByteArray
import ru.zemf4you.wgspleef.Util.toBase64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object InventoryManager {

    fun createInventory(): Inventory =
        Bukkit.createInventory(null, InventoryType.PLAYER)

    fun createInventory(materials: List<Material>): Inventory? {
        if (materials.size !in 0..41)
            return null
        val inventory = createInventory()
        materials.forEachIndexed { index, material ->
            inventory.setItem(index, ItemStack(material))
        }
        return inventory
    }

    fun Inventory.save(): String {
        val outputStream = ByteArrayOutputStream()
        val dataOutput = BukkitObjectOutputStream(outputStream)
        dataOutput.writeInt(this.contents.size)
        for (itemStack in this.contents)
            dataOutput.writeObject(itemStack)
        dataOutput.close()
        return outputStream.toByteArray().toBase64()
    }

    fun Inventory.restore(base64: String) {
        val inputStream = ByteArrayInputStream(base64.fromBase64ToByteArray())
        val dataInput = BukkitObjectInputStream(inputStream)
        val contentsSize = dataInput.readInt()
        try {
            this.contents = Array(contentsSize) { dataInput.readObject()?.cast() }
        } catch (e: Throwable) {
            Bukkit.getLogger().warning("Can`t read inventory from base64: $base64")
            e.printStackTrace()
        }
        dataInput.close()
    }

}