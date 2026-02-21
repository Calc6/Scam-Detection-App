package org.setu.scamdetector.models

import android.content.Context
import android.util.Log
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.BufferedReader
import java.io.InputStreamReader

class NaiveBayesScamModel(private val context: Context) {

    private var vocab: Map<String, Int>? = null
    private var classLogPrior: FloatArray? = null
    private var featureLogProb: Array<FloatArray>? = null

    fun loadIfNeeded() {
        if (vocab != null) return

        val vocabJson = context.assets.open("vocab.json").use { input ->
            BufferedReader(InputStreamReader(input)).readText()
        }
        val jsonObj = Json.parseToJsonElement(vocabJson).jsonObject
        vocab = jsonObj.mapValues { it.value.jsonPrimitive.content.toInt() }

        Log.d("NBModel", "Loaded vocab size=${vocab!!.size}")
    }
}