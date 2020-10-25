package com.github.arrase.actions

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

class SettingsFragment : PreferenceFragmentCompat() {
    val SERVICE_ENABLED = "svc_enabled"

    private var preferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        preferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                when (val preference = findPreference<Preference>(key)) {
                    is SwitchPreferenceCompat -> {
                        if (key == SERVICE_ENABLED) {
                            if (preference.isChecked) {
                                Log.d("PREF_CHANGED", "Start service.")
                                Intent(this.context, EventService::class.java).also { intent ->
                                    activity?.startService(intent)
                                }
                            } else {
                                Log.d("PREF_CHANGED", "Stop service.")
                                Intent(this.context, EventService::class.java).also { intent ->
                                    activity?.stopService(intent)
                                }
                            }
                        }
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences
            .registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }
}