package net.common.commonlib.extension

import net.common.commonlib.extension.IntExtension.toByteArray
import java.nio.Buffer
import java.nio.ByteBuffer
import java.util.*

object IntExtension {

    fun Int.toByteArray() : ByteArray {
        val bArray = ByteBuffer.allocate(4).apply {
            putInt(this@toByteArray)
        }.array()
        return Arrays.copyOfRange(bArray, 3,4)
    }

    fun Int.toTwoByteArray() : ByteArray {
        val bArray = ByteBuffer.allocate(4).apply {
            putInt(this@toTwoByteArray)
        }.array()
        return Arrays.copyOfRange(bArray, 2,4)
    }

}