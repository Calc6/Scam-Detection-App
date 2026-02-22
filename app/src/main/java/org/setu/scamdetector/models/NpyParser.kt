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
}