package org.setu.scamdetector.models

object ScamDetector {

    private val scamKeywords = listOf(
        "urgent", "verify", "click", "account",
        "suspended", "bank", "password", "login", "ATTENTION","fine",
        "log", "https", "You","have","for"
    )

    fun analyzeMessage(message: String): ScamAnalysisResult {
        val lower = message.lowercase()

        val matchedKeywords = scamKeywords.filter { lower.contains(it) }
        val hasLink = lower.contains("http://") || lower.contains("https://")
        var score = 0
        score += matchedKeywords.size * 15
        if (hasLink) score += 30
        if (score > 100) score = 100

        val isScam = score >= 60

        val reasons = mutableListOf<String>()
        if (matchedKeywords.isNotEmpty())
            reasons.add("The Message contains suspicious wording ")
        if (hasLink)
            reasons.add("Contains a suspicious link")

        return ScamAnalysisResult(
            isScam = isScam,
            score = score,
            reasons = reasons
        )
    }
}
