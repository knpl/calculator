package com.knpl.calc;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.knpl.calc.R;

public class RangePreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener {
	
	private final int min,
					  max,
					  defaultt;
	
	private SeekBar seekBar;
	private TextView textView;
	private Integer value;

	public RangePreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		setDialogLayoutResource(R.layout.integer_preference_dialog);
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
		
		setDialogIcon(null);
		
		min = attrs.getAttributeIntValue("http://knpl.com", "min", 0);	
		max = attrs.getAttributeIntValue("http://knpl.com", "max", 100);
		defaultt = attrs.getAttributeIntValue("http://knpl.com", "default", 50);
	}

	@Override
	protected View onCreateDialogView() {
		View view = super.onCreateDialogView();
		seekBar = (SeekBar) view.findViewById(R.id.integer_preference_seekbar);
		textView = (TextView) view.findViewById(R.id.integer_preference_textview);

		textView.setText(""+value);
		
		seekBar.setMax(max - min);
		seekBar.setProgress(value - min);
		
		seekBar.setOnSeekBarChangeListener(this);
		return view;
	}
	
	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		if (restorePersistedValue) {
			value = getPersistedInt(defaultt);
		}
		else {
			value = (Integer) defaultValue;
			persistInt(value);
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getInteger(index, defaultt);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			value = seekBar.getProgress() + min;
			persistInt(value);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		textView.setText(""+(progress + min));
	}

	@Override public void onStartTrackingTouch(SeekBar seekBar) {}
	@Override public void onStopTrackingTouch(SeekBar seekBar) {}
}
