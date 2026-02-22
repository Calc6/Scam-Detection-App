package org.setu.scamdetector.models

import android.content.Context
import java.io.ByteArrayOutputStream
import java.util.zip.ZipInputStream

/*
 * NPZ reader :
 *   https://numpy.org/doc/stable/reference/generated/numpy.savez.html
 * - Java ZipInputStream:
 *   https://docs.oracle.com/javase/8/docs/api/java/util/zip/ZipInputStream.html
 */
object NpzReader {

    /**
     * Reads all entries from an .npz file in assets into a map:
     * entry name -> raw bytes
     *
     * Example entries usually look like: "class_log_prior.npy"
     */
    fun readEntriesFromAssets(context: Context, assetName: String): Map<String, ByteArray> {
        context.assets.open(assetName).use { input ->
            ZipInputStream(input).use { zip ->
                val out = LinkedHashMap<String, ByteArray>()
                var entry = zip.nextEntry
                while (entry != null) {
                    if (!entry.isDirectory) {
                        out[entry.name] = zip.readAllBytesCompat()
                    }
                    zip.closeEntry()
                    entry = zip.nextEntry
                }
                return out
            }
        }
    }

    private fun ZipInputStream.readAllBytesCompat(): ByteArray {
        val buffer = ByteArray(8 * 1024)
        val baos = ByteArrayOutputStream()
        while (true) {
            val n = read(buffer)
            if (n <= 0) break
            baos.write(buffer, 0, n)
        }
        return baos.toByteArray()
    }
}