package com.knpl.calc;

import com.knpl.calc.R;
import com.knpl.calc.plot.LogRange;
import com.knpl.calc.plot.Range;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class PlotOptionsFragment extends Fragment {
	
	private SimpleCalculatorActivity activity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_options, container, false);
		
		Spinner xaxistype = (Spinner) view.findViewById(R.id.xType),
				yaxistype = (Spinner) view.findViewById(R.id.yType);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.axistypes, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		xaxistype.setAdapter(adapter);
		yaxistype.setAdapter(adapter);
		
		Button apply = (Button) view.findViewById(R.id.apply);
		apply.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				apply();
			}
		});
		
		EditText et;
		et = (EditText) view.findViewById(R.id.xMin);
		activity.registerEditTextToKeyboard(et);
		
		et = (EditText) view.findViewById(R.id.xMax);
		activity.registerEditTextToKeyboard(et);
		
		et = (EditText) view.findViewById(R.id.yMin);
		activity.registerEditTextToKeyboard(et);
		
		et = (EditText) view.findViewById(R.id.yMax);
		activity.registerEditTextToKeyboard(et);
		
		return view;
	}
	
	public float getNumber(EditText et) {
		String s = et.getText().toString();
		if (s.isEmpty()) {
			return Float.NaN;
		}
		
		try {
			return Float.parseFloat(s);
		}
		catch (NumberFormatException e) {
			return Float.NaN;
		}
	}
	
	public Range getAxis(Spinner spinner, float min, float max) {
		switch (spinner.getSelectedItemPosition()) {
		case 0: 
			return new Range(min, max);
		case 1: 
			return new LogRange(min, max);
		default:	
			return SimpleCalculatorActivity.DEFAULT_AXIS;
		}
	}
	
	public void apply() {
		View v = getView();
		
		Range oldx = activity.getXAxis();
		Range oldy = activity.getYAxis();
		
		float xmin = getNumber((EditText)v.findViewById(R.id.xMin)),
			  xmax = getNumber((EditText)v.findViewById(R.id.xMax)),
			  ymin = getNumber((EditText)v.findViewById(R.id.yMin)),
			  ymax = getNumber((EditText)v.findViewById(R.id.yMax));
		
		if (!ok(xmin)) xmin = oldx.min;
		if (!ok(xmax)) xmax = oldx.max;
		if (!ok(ymin)) ymin = oldy.min;
		if (!ok(ymax)) ymax = oldy.max;
		
		activity.setXAxis(getAxis((Spinner) v.findViewById(R.id.xType), xmin, xmax));
		activity.setYAxis(getAxis((Spinner) v.findViewById(R.id.yType), ymin, ymax));
	}
	
	public boolean ok(float x) {
		return !(Float.isNaN(x) || Float.isInfinite(x));
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof SimpleCalculatorActivity)) {
			throw new ClassCastException(activity.toString()
					+ " must implement SimpleCalculatorActivity.");
		}
		this.activity = (SimpleCalculatorActivity) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		activity = null;
	}
}
