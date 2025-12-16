package org.setu.scamdetector.models

data class ScamAnalysisResult(
    val isScam: Boolean,
    val score: Int,
    val reasons: List<String>)