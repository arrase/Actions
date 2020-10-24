package com.github.arrase.actions

import android.app.Service
import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import android.util.Log

class EventService : Service() {
    private val TAG = "ActionService"

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG, "onConfigurationChanged")
    }
}