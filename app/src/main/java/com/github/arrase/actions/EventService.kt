package com.github.arrase.actions

import android.app.Service
import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import android.util.Log

class EventService : Service() {
    private val logTAG = "ActionService"

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(logTAG, "onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(logTAG, "onDestroy")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(logTAG, "onConfigurationChanged")
    }
}