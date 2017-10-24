package com.joaquintech.boozepoints.database.fragments;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;

import com.joaquintech.boozepoints.R;

/**
 * Created by rjmahler on 9/4/2017.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        // Assign the data store to a single preference
        //findPreference("myPref").setPreferenceDataStore(new MyDataStore());
    }

}
