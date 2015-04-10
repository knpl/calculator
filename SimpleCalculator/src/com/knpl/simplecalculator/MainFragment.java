package com.knpl.simplecalculator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class MainFragment extends Fragment {
	
	private SimpleCalculatorActivity activity;
	
	private EditText input;
	private TextView output;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main, container, false);
		
		input  = (EditText) v.findViewById(R.id.input);
		output = (TextView) v.findViewById(R.id.output);
		
		activity.registerEditTextToKeyboard(input);
		input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				activity.enter(v.getText().toString());
				return true;
			}
		});
		
		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof SimpleCalculatorActivity) {
			this.activity = (SimpleCalculatorActivity) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement SimpleCalculatorActivity");
		}
	}

	@Override
	public void onDetach() {
		activity = null;
		super.onDetach();
	}
}
