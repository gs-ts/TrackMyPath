package com.gts.trackmypath

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidLogger
import org.koin.android.ext.koin.androidContext

import timber.log.Timber

class TrackMyPathApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger()
            // use the Android context given there
            androidContext(this@TrackMyPathApplication)
            // module list
            modules(listOf(appModule))
        }
    }
}
