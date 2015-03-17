package com.knpl.simplecalculator;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class PrecisionPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener {
	public static int MIN_VALUE = 1,
					  MAX_VALUE = 16,
					  DEFAULT_VALUE = 10;
	
	private SeekBar seekBar;
	private TextView textView;
	private Integer value;

	public PrecisionPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		setDialogLayoutResource(R.layout.integer_preference_dialog);
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
		
		setDialogIcon(null);
	}

	@Override
	protected View onCreateDialogView() {
		View view = super.onCreateDialogView();
		seekBar = (SeekBar) view.findViewById(R.id.integer_preference_seekbar);
		textView = (TextView) view.findViewById(R.id.integer_preference_textview);

		textView.setText(""+value);
		
		seekBar.setMax(MAX_VALUE - MIN_VALUE);
		seekBar.setProgress(value - MIN_VALUE);
		
		seekBar.setOnSeekBarChangeListener(this);
		return view;
	}
	
	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		if (restorePersistedValue) {
			value = getPersistedInt(DEFAULT_VALUE);
		}
		else {
			value = (Integer) defaultValue;
			persistInt(value);
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getInteger(index, DEFAULT_VALUE);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			value = seekBar.getProgress() + MIN_VALUE;
			persistInt(value);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		textView.setText(""+(progress + MIN_VALUE));
	}

	@Override public void onStartTrackingTouch(SeekBar seekBar) {}
	@Override public void onStopTrackingTouch(SeekBar seekBar) {}
}
