package com.spaceandjonin.mycrd

import android.app.Application
import androidx.work.Configuration
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyCardApplication : Application(), Configuration.Provider {

    //@Inject lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        /*else
            todo add class for writing logs to a txt file or something
            Timber.plant(CrashReportingTree)*/
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            //.setWorkerFactory(workerFactory)
            .build()
}

