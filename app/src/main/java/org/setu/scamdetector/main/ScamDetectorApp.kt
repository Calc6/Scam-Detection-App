package org.setu.scamdetector.main

import android.app.Application
import org.setu.scamdetector.models.ScanResultMemStore
import timber.log.Timber

class ScamDetectorApp : Application() {

    val scams = ScanResultMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("ScamDetector started")
    }
}