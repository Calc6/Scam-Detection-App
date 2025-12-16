package org.setu.scamdetector.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class ScanResultMemStore : ScanResultStore {

    val scams = ArrayList<ScanResultModel>()

    override fun findAll(): List<ScanResultModel> {
        return scams
    }

    override fun create(scam: ScanResultModel) {
        scam.id = getId()
        scams.add(scam)
        logAll()
    }

    override fun update(scam: ScanResultModel) {
        var foundScam: ScanResultModel? = scams.find { p -> p.id == scam.id }
        if (foundScam != null) {
            foundScam.title = scam.title
            foundScam.description = scam.description
            logAll()
        }
    }

    private fun logAll() {
        scams.forEach { i("$it") }
    }
}