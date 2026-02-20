package org.setu.scamdetector.models

import android.content.Context
import android.util.Log

class NaiveBayesScamModel(private val context: Context) {

    private var vocab: Map<String, Int>? = null
    private var classLogPrior: FloatArray? = null
    private var featureLogProb: Array<FloatArray>? = null

    fun loadIfNeeded() {
        if (vocab != null && classLogPrior != null && featureLogProb != null) {
            Log.d("NBModel", "Already loaded")
            return
        }
        Log.d("NBModel", "Not loaded yet")
    }
}