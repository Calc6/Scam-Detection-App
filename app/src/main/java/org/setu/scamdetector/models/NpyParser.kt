package org.setu.scamdetector.models

import java.nio.ByteBuffer
import java.nio.ByteOrder

// .npy format reference: https://numpy.org/doc/stable/reference/generated/numpy.lib.format.html
object NpyParser {

    // .npy files start with a fixed 10 byte
    // bytes 8 tell us how long the header string is
    private fun getHeaderLen(bytes: ByteArray): Int {
        return ByteBuffer.wrap(bytes, 8, 2)
            .order(ByteOrder.LITTLE_ENDIAN).short.toInt() and 0xFFFF
    }

    // reads the flat float values after the header
    // using ByteBuffer to handle bytes
    // byteBuffer : https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html
    fun parseFloatArray(bytes: ByteArray): FloatArray {
        val headerLen = getHeaderLen(bytes)
        val dataOffset = 10 + headerLen
        val buf = ByteBuffer.wrap(bytes, dataOffset, bytes.size - dataOffset)
            .order(ByteOrder.LITTLE_ENDIAN)
        val count = (bytes.size - dataOffset) / 4
        return FloatArray(count) { buf.float }
    }
}