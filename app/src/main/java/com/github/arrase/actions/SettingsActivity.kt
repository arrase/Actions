package com.github.arrase.actions

import android.content.Intent
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, SettingsFragment())
                    .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private var preferenceChangeListener: OnSharedPreferenceChangeListener? = null

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            preferenceChangeListener =
                    OnSharedPreferenceChangeListener { _, key ->
                        when (val preference = findPreference<Preference>(key)) {
                            is SwitchPreferenceCompat -> {
                                if (key == "svc_enabled") {
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
}
