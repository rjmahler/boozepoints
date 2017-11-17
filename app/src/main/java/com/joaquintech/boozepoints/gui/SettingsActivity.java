package com.joaquintech.boozepoints.gui;

import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.Preference;

import com.joaquintech.boozepoints.database.fragments.SettingsFragment;

/**
 * Created by rjmahler on 9/4/2017.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String
            KEY_VOLUME_UNIT_SWITCH = "VOLUME";
    public static final String
            KEY_ALCOHOL_UNIT_SWITCH = "ALCOHOL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }


    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(KEY_VOLUME_UNIT_SWITCH)) {
            Preference connectionPref = findPreference(key);
            // Set summary to be the user-description for the selected value
            connectionPref.setSummary(sharedPreferences.getString(key, ""));
        }
    }


}
