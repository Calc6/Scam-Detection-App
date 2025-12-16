package org.setu.scamdetector.models

interface ScanResultStore {
    fun findAll(): List<ScanResultModel>
    fun create(scam: ScanResultModel)
    fun update(scam: ScanResultModel)
}