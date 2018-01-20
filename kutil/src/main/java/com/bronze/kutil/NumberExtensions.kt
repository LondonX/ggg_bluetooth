package com.bronze.kutil

import java.nio.ByteBuffer

/**
 * Created by London on 2017/12/15.
 * extensions for operations of numbers
 */
fun Int.toBytes(): ByteArray = ByteBuffer.allocate(4).putInt(this).array()

fun Short.toBytes(): ByteArray = ByteBuffer.allocate(2).putShort(this).array()

fun ByteArray.toIntOrNull(): Int? {
    return try {
        this.toInt()
    } catch (ignore: IllegalArgumentException) {
        null
    }
}

fun ByteArray.toInt() =
        when (this.size) {
            2 -> ByteBuffer.wrap(this).short.toInt()
            4 -> ByteBuffer.wrap(this).int
            8 -> ByteBuffer.wrap(this).long.toInt()
            else -> throw IllegalArgumentException("ByteArray(size: ${this.size}) is not valid for toInt")
        }


fun Int.getUnsignedLong() = this.toLong() and 0x00000000ffffffffL
