package com.knpl.calc;

import com.knpl.calc.R;
import com.knpl.calc.nodes.Node;
import com.knpl.calc.nodes.Num;
import com.knpl.calc.parser.Lexer;
import com.knpl.calc.parser.Parser;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
				enter(v.getText().toString());
				return true;
			}
		});
		
		return v;
	}

    public void enter(String inputString) {
    	try {
    		Parser parser = new Parser(new Lexer(inputString));
        	if (!parser.start()) {
        		print("Syntax error");
        		return;
        	}
        	
    		Node parseResult = parser.getResult();
	    	parseResult.execute(activity);
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    		print("Caught exception: "+ex.getMessage());
    	}
    }
    
    public void print(String s) {
    	output.setText(s);
    }
    
    public void print(Num n) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
    	int ndecimals = prefs.getInt(CalculatorPreferenceFragment.PREF_KEY_PRECISION, 10);
    	boolean polar = prefs.getBoolean("pref_key_complex_polar", false);
    	
    	output.setText(n.format(ndecimals, polar));
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
