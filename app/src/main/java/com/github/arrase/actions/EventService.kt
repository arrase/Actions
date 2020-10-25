package com.github.arrase.actions

import android.app.Service
import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.media.AudioManager
import android.os.IBinder
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.util.Log


class EventService : Service() {
    private val logTAG = "ActionService"

    private var mWakeLock: WakeLock? = null
    private var mWiredHeadsetConnected = false
    private var mA2DPConnected = false

    private val mStateListener: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            mWakeLock?.acquire(5 * 60 * 1000L) /*5 minutes*/
            try {
                when (action) {
                    BluetoothAdapter.ACTION_STATE_CHANGED -> if (intent.getIntExtra(
                            BluetoothAdapter.EXTRA_STATE,
                            -1
                        ) == BluetoothAdapter.STATE_OFF
                    ) {
                        Log.d(logTAG, "BluetoothAdapter.ACTION_STATE_CHANGED")
                    }
                    BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED -> {
                        val state = intent.getIntExtra(
                            BluetoothProfile.EXTRA_STATE,
                            BluetoothProfile.STATE_CONNECTED
                        )
                        if (state == BluetoothProfile.STATE_CONNECTED && !mA2DPConnected) {
                            Log.d(logTAG, "BluetoothProfile.STATE_CONNECTED = true")
                            mA2DPConnected = true
                        } else {
                            Log.d(logTAG, "BluetoothProfile.STATE_CONNECTED = false")
                            mA2DPConnected = false
                        }
                    }
                    AudioManager.ACTION_HEADSET_PLUG -> {
                        val useHeadset = intent.getIntExtra("state", 0) == 1
                        if (useHeadset && !mWiredHeadsetConnected) {
                            Log.d(logTAG, "AudioManager.ACTION_HEADSET_PLUG = true")
                            mWiredHeadsetConnected = true
                        } else {
                            Log.d(logTAG, "AudioManager.ACTION_HEADSET_PLUG = false")
                            mWiredHeadsetConnected = false
                        }
                    }
                }
            } finally {
                mWakeLock?.release()
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(logTAG, "onCreate")
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, logTAG)
        // mWakeLock.setReferenceCounted(true)
        registerListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(logTAG, "onDestroy")
        unregisterListener()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(logTAG, "onConfigurationChanged")
    }

    private fun registerListener() {
        Log.d(logTAG, "registerListener")
        val filter = IntentFilter()
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        filter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)
        filter.addAction(AudioManager.ACTION_HEADSET_PLUG)
        this.registerReceiver(mStateListener, filter)
    }

    private fun unregisterListener() {
        Log.d(logTAG, "unregisterListener")
        try {
            unregisterReceiver(mStateListener)
        } catch (e: Exception) {
            Log.e(logTAG, "unregisterListener", e)
        }
    }
}