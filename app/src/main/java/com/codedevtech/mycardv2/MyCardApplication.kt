package com.codedevtech.mycardv2

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyCardApplication: Application(), Configuration.Provider {

    //@Inject lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            //.setWorkerFactory(workerFactory)
            .build()
    }

