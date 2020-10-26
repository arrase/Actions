package com.github.arrase.actions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager


class BootCompletedReceiver : BroadcastReceiver() {
    private val logTAG = "BootCompletedReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        try {
            val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            if (prefs.getBoolean("svc_enabled", false)) {
                Log.d(logTAG, "onReceive " + intent.action)
                context.startForegroundService(Intent(context, EventService::class.java))
            }
        } catch (e: Exception) {
            Log.e(logTAG, "Can't start actions service", e)
        }
    }
}