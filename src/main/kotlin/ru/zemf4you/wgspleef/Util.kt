package ru.zemf4you.wgspleef

import java.util.*


object Util {
    // TODO:
    //  Seems like something bad, but @irisism said that it`s good idea
    @Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
    inline fun <T>Any.cast() = this as T

    fun ByteArray.toBase64(): String =
        Base64.getEncoder().encodeToString(this)

    fun String.fromBase64ToByteArray(): ByteArray =
        Base64.getDecoder().decode(this)

    fun <T> Array<out T>.isPatternFor(other: Array<out T>): Boolean {
        if (this.size != other.size)
            return false
        for (i in this.indices)
            if (this[i] != null && this[i] != other[i])
                return false
        return true
    }

}