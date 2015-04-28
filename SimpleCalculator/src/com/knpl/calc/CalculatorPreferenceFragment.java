package com.knpl.calc;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.preference.PreferenceFragment;
import com.knpl.calc.R;

public class CalculatorPreferenceFragment extends PreferenceFragment
			 implements OnSharedPreferenceChangeListener {
	
	public static final String PREF_KEY_PRECISION = "pref_key_precision",
							   PREF_KEY_LINE_THICKNESS = "pref_key_line_thickness";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		addPreferencesFromResource(R.xml.preferences);
		SharedPreferences prefs = getPreferenceScreen().getSharedPreferences();
		
		Preference pref = findPreference(PREF_KEY_PRECISION);
		int value = prefs.getInt(PREF_KEY_PRECISION, 10);
		pref.setSummary(String.format("Calculator currently displays results using %d digits.", value));
		
		pref = findPreference(PREF_KEY_LINE_THICKNESS);
		value = prefs.getInt(PREF_KEY_LINE_THICKNESS, 1);
		pref.setSummary(String.format("Plotted lines are currently %d pixels wide", value));
		
		prefs.registerOnSharedPreferenceChangeListener(this);
	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Preference pref;
		int value;
		
		if (key.equals(PREF_KEY_PRECISION)) {
			pref = findPreference(key);
			value = sharedPreferences.getInt(key, 10);
			pref.setSummary(String.format("Calculator currently displays results using %d digits.", value));
		}
		else if (key.equals(PREF_KEY_LINE_THICKNESS)) {
			pref = findPreference(key);
			value = sharedPreferences.getInt(key, 1);
			pref.setSummary(String.format("Plotted lines are currently %d pixels wide", value));
		}
	}
}
