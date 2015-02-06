package com.knpl.simplecalculator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.knpl.simplecalculator.PlotMenuFragment.PlotEntry;
import com.knpl.simplecalculator.nodes.Expr;
import com.knpl.simplecalculator.nodes.Signature;
import com.knpl.simplecalculator.nodes.Var;
import com.knpl.simplecalculator.parser.Lexer;
import com.knpl.simplecalculator.parser.Parser;
import com.knpl.simplecalculator.visitors.Resolve;

import afzkl.development.colorpickerview.dialog.ColorPickerDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PlotFuncDialog extends DialogFragment {
	
	public interface PlotFuncDialogListener {
        void addPlotEntry(PlotEntry plotEntry);
    }
	
	private PlotFuncDialogListener listener;
	
	public static PlotFuncDialog createInstance(String title, int position) {
		PlotFuncDialog dialog = new PlotFuncDialog();
		
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putInt("position", position);
        dialog.setArguments(args);
        
        return dialog;
	}
	
	private void displayMessage(String message) {
		Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}
	
	private void save(String name, String expression, int color) {
		
		if (!Pattern.matches("[_a-zA-Z][_a-zA-Z0-9]*", name)) {
			displayMessage("Invalid name: "+name);
			return;
		}
		
		GlobalDefinitions defs = GlobalDefinitions.getInstance();
		if (defs.getFunctionDefinition(name)!= null) {
			displayMessage("Function "+name+" exists.");
			return;
		}
		
		Parser parser = 
			new Parser(new Lexer(expression.toCharArray()));
		
		if (!parser.expr()) {
			displayMessage("Syntax error: "+expression);
			return;
		}
		
		Expr expr = (Expr) parser.getResult();
		Map<String, Var> boundedVarMap = new HashMap<String, Var>();
		Var var = new Var("x");
		boundedVarMap.put(var.getName(), var);
		Resolve visitor = new Resolve(boundedVarMap);
			
		try {	
			expr = (Expr) expr.accept(visitor);
			if (!visitor.getFreeVarMap().isEmpty()) {
				displayMessage("Expression contains free variables");
				return;
			}
			
			UserFuncDef ufd = new UserFuncDef(new Signature(name, Arrays.asList(var)), expr);
			ufd.compile();
			
			defs.putUserFuncDef(ufd);
			
			listener.addPlotEntry(new PlotEntry(ufd, expression, color));
			getDialog().dismiss();
		}
		catch (Exception e) {
			e.printStackTrace();
			displayMessage("Caught Exception: "+e.getMessage());
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof PlotFuncDialogListener) {
			listener = (PlotFuncDialogListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet DialogFragment.PlotFuncDialogListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_dialog, container);
		
		getDialog().setTitle("new expression");
		
		String defaultName = "";
		GlobalDefinitions defs = GlobalDefinitions.getInstance();
		for (char c = 'f'; c < 'z'; ++c) {
			String name = ""+c;
			if (defs.getFunctionDefinition(name) == null) {
				defaultName = name;
				break;
			}
		}
		
		final EditText fnameET = (EditText) view.findViewById(R.id.dialog_function_name);
		final EditText fexprET = (EditText) view.findViewById(R.id.dialog_expression);
		final Button save = (Button) view.findViewById(R.id.dialog_save);
		final Button pickColor = (Button) view.findViewById(R.id.dialog_color_pick);
		
		final ColorPickerDialog colorDialog = new ColorPickerDialog(getActivity(), 0xFF000000);
		colorDialog.setAlphaSliderVisible(true);
		colorDialog.setTitle("Pick a Color");
		
		colorDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				pickColor.setBackgroundColor(colorDialog.getColor());
			}
		});
		
		colorDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		
		fnameET.setText(defaultName);
		
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				save(fnameET.getText().toString(), fexprET.getText().toString(), colorDialog.getColor());
			}
		});
		
		pickColor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				colorDialog.show();
			}
		});
		
		return view;
	}
	
}
