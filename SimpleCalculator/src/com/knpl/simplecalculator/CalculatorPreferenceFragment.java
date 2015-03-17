package com.knpl.simplecalculator;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.preference.PreferenceFragment;

public class CalculatorPreferenceFragment extends PreferenceFragment
			 implements OnSharedPreferenceChangeListener {
	
	public static final String PREF_KEY_PRECISION = "pref_key_precision";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		addPreferencesFromResource(R.xml.preferences);
		SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();
		
		Preference pref = findPreference(PREF_KEY_PRECISION);
		int value = prefs.getInt(PREF_KEY_PRECISION, 10);
		pref.setSummary(String.format("Calculator currently displays results using %d digits.", value));
		
		prefs.registerOnSharedPreferenceChangeListener(this);
	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(PREF_KEY_PRECISION)) {
			Preference pref = findPreference(key);
			int value = sharedPreferences.getInt(key, 10);
			pref.setSummary(String.format("Calculator currently displays results using %d digits.", value));
		}
	}
}
