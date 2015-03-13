package com.knpl.simplecalculator;

import com.knpl.simplecalculator.plot.Range;
import com.knpl.simplecalculator.plot.LogRange;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class PlotOptionsFragment extends Fragment {
	
	private OptionsListener listener;
	
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
		apply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				apply();
			}
		});
		
		Range x = listener.getXAxis(),
			 y = listener.getYAxis();
		
		EditText et;
		et = (EditText) view.findViewById(R.id.xMin);
		et.setText(""+x.min);
		
		et = (EditText) view.findViewById(R.id.xMax);
		et.setText(""+x.max);
		
		et = (EditText) view.findViewById(R.id.yMin);
		et.setText(""+y.min);
		
		et = (EditText) view.findViewById(R.id.yMax);
		et.setText(""+y.max);

	
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
		
		Range oldx = listener.getXAxis();
		Range oldy = listener.getYAxis();
		
		float xmin = getNumber((EditText)v.findViewById(R.id.xMin)),
			  xmax = getNumber((EditText)v.findViewById(R.id.xMax)),
			  ymin = getNumber((EditText)v.findViewById(R.id.yMin)),
			  ymax = getNumber((EditText)v.findViewById(R.id.yMax));
		
		if (!ok(xmin)) xmin = oldx.min;
		if (!ok(xmax)) xmax = oldx.max;
		if (!ok(ymin)) ymin = oldy.min;
		if (!ok(ymax)) ymax = oldy.max;
		
		listener.setXAxis(getAxis((Spinner) v.findViewById(R.id.xType), xmin, xmax));
		listener.setYAxis(getAxis((Spinner) v.findViewById(R.id.yType), ymin, ymax));
	}
	
	public boolean ok(float x) {
		return !(Float.isNaN(x) || Float.isInfinite(x));
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OptionsListener) {
			listener = (OptionsListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet OptionsFragment.OptionsListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}
	
	public interface OptionsListener {
		void setXAxis(Range x);
		Range getXAxis();
		void setYAxis(Range y);
		Range getYAxis();
	}
}
