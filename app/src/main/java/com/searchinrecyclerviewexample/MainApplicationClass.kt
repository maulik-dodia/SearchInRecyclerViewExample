package com.searchinrecyclerviewexample

import android.app.Application
import timber.log.Timber

class MainApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}