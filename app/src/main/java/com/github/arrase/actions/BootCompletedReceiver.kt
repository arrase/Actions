package com.github.arrase.actions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootCompletedReceiver : BroadcastReceiver() {
    private val TAG = "MyActivity"
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "my Message")
    }
}