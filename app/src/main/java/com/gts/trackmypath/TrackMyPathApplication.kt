package com.gts.trackmypath

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

import com.facebook.drawee.backends.pipeline.Fresco

import timber.log.Timber

@HiltAndroidApp
class TrackMyPathApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
