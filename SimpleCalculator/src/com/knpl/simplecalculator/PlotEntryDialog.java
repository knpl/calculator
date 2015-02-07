package com.knpl.simplecalculator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.knpl.simplecalculator.PlotMenuFragment.PlotEntry;
import com.knpl.simplecalculator.nodes.Expr;
import com.knpl.simplecalculator.nodes.Signature;
import com.knpl.simplecalculator.nodes.Var;
import com.knpl.simplecalculator.parser.Lexer;
import com.knpl.simplecalculator.parser.Parser;
import com.knpl.simplecalculator.util.GlobalDefinitions;
import com.knpl.simplecalculator.util.UserFuncDef;
import com.knpl.simplecalculator.visitors.PrettyPrint;
import com.knpl.simplecalculator.visitors.Resolve;

import afzkl.development.colorpickerview.dialog.ColorPickerDialog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PlotEntryDialog extends DialogFragment implements View.OnClickListener {
	
	public static final int DEFAULT_COLOR = Color.BLACK;
	
	public interface PlotEntryDialogListener {
        void addPlotEntry(PlotEntry entry);
        void setPlotEntry(int position, PlotEntry entry);
        void removePlotEntry(int position);
	}
	
	private PlotEntryDialogListener listener;
	
	private EditText nameEditText;
	private EditText exprEditText;
	private ColorPickerDialog colorPickerDialog;
	private ImageView selectedColorIndicator;
	private boolean edit;
	
	public static PlotEntryDialog createInstance() {
		return new PlotEntryDialog();
	}
	
	public static PlotEntryDialog createInstance(PlotEntry entry, int position) {
		PlotEntryDialog dialog = new PlotEntryDialog();
		
		Bundle args = new Bundle();
		args.putString("title", ""+entry.getUserFuncDef().getSignature().getName());
		args.putString("description", entry.getUserFuncDef().getDescription());
		args.putInt("color", entry.getColor());
		args.putInt("position", position);
        dialog.setArguments(args);
        
        return dialog;
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof PlotEntryDialogListener) {
			listener = (PlotEntryDialogListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement DialogFragment.PlotFuncDialogListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}
	
	private ColorPickerDialog createColorPickerDialog(int color) {
		final ColorPickerDialog colorDialog = new ColorPickerDialog(getActivity(), color);
		colorDialog.setAlphaSliderVisible(true);
		colorDialog.setTitle("Pick a Color");
		
		colorDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedColorIndicator.setBackgroundColor(colorPickerDialog.getColor());
			}
		});
		colorDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		});
		
		return colorDialog;
	}

	private String getUnusedFunctionName() {
		GlobalDefinitions defs = GlobalDefinitions.getInstance();
		for (char c = 'f'; c < 'z'; ++c) {
			String name = String.valueOf(c);
			if (defs.getFunctionDefinition(name) == null) {
				return name;
			}
		}
		/* Give up */
		return "";
	}
	
	private void init(View view) {
		nameEditText = (EditText) view.findViewById(R.id.dialog_function_name);
		exprEditText = (EditText) view.findViewById(R.id.dialog_expression);
		selectedColorIndicator = (ImageView) view.findViewById(R.id.dialog_color_indicator);
		
		
		Bundle args = getArguments();
		if (args != null) {
			edit = true;
			nameEditText.setText(args.getString("title"));
			nameEditText.setEnabled(false);
			exprEditText.setText(args.getString("description"));
			int color = args.getInt("color");
			
			selectedColorIndicator.setBackgroundColor(color);
			colorPickerDialog = createColorPickerDialog(color);
		}
		else {
			edit = false;
			nameEditText.setText(getUnusedFunctionName());
			
			colorPickerDialog = createColorPickerDialog(DEFAULT_COLOR);
			selectedColorIndicator.setBackgroundColor(DEFAULT_COLOR);
		}

		view.findViewById(R.id.dialog_color_pick).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				colorPickerDialog.show();
			}
		});
	}
	
	@SuppressLint("InflateParams") @Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View content = inflater.inflate(R.layout.fragment_dialog, null);
		
		init(content);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(content)
			   .setTitle(edit ? nameEditText.getText() : "new function")
			   .setPositiveButton("save", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {}
			   })
			   .setNegativeButton(edit ? "delete" : "cancel", new DialogInterface.OnClickListener() {
				   	@Override
					public void onClick(DialogInterface dialog, int which) {}
			   });
		
		return builder.create();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		AlertDialog dialog = (AlertDialog)getDialog();
		if (dialog == null) {
			return;
		}
		dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(this);
		dialog.getButton(Dialog.BUTTON_NEGATIVE).setOnClickListener(this);
	}
	

	private void displayMessage(String message) {
		Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View view) {
		String name = nameEditText.getText().toString();
		String expr = exprEditText.getText().toString();
		int color = colorPickerDialog.getColor();
		
		if (view == ((AlertDialog) getDialog()).getButton(Dialog.BUTTON_NEGATIVE)) {
			onNegative(name, expr, color);
		}
		else {
			onPositive(name, expr, color);
		}
	}
	
	private void onNegative(String name, String expr, int color) {
		if (edit) {
			listener.removePlotEntry(getArguments().getInt("position"));
			GlobalDefinitions.getInstance().removeUserFuncDef(name);
			dismiss();
		}
		else {
			dismiss();
		}
	}
	
	private void onPositive(String name, String expr, int color) {
		GlobalDefinitions defs = GlobalDefinitions.getInstance();
		
		if (!edit) {
			if (!name.matches("[_a-zA-Z][_a-zA-Z0-9]*")) {
				displayMessage("Invalid name: "+name);
				return;
			}
			
			if (defs.getFunctionDefinition(name)!= null) {
				displayMessage("Function "+name+" exists.");
				return;
			}
		}
		
		Parser parser = new Parser(new Lexer(expr));
		if (!parser.expr()) {
			displayMessage("Syntax error: "+expr);
			return;
		}
		
		Expr e = (Expr) parser.getResult();
		Map<String, Var> boundedVarMap = new HashMap<String, Var>();
		Var var = new Var("x");
		boundedVarMap.put(var.getName(), var);
		Resolve visitor = new Resolve(boundedVarMap);
			
		try {	
			e = (Expr) e.accept(visitor);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			displayMessage("Resolve error: "+ex.getMessage());
			return;
		}

		if (!visitor.getFreeVarMap().isEmpty()) {
			String vars = Arrays.toString(visitor.getFreeVarMap().keySet().toArray());
			displayMessage("Resolve error: undeclared variables " + vars);
			return;
		}
		
		Signature s = new Signature(name, Arrays.asList(var));
		String d;
		try {
			PrettyPrint prettyPrint = new PrettyPrint();
			e.accept(prettyPrint);
			d = prettyPrint.toString();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			displayMessage("Error: "+ex.getMessage());
			return;
		}
			
		UserFuncDef ufd = new UserFuncDef(s, d, e);
		try {
			ufd.compile();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			displayMessage("Compilation failed: "+ex.getMessage());
			return;
		}
		
		defs.putUserFuncDef(ufd);
		if (edit) {
			int position = getArguments().getInt("position");
			listener.setPlotEntry(position, new PlotEntry(ufd, color));
		}
		else {
			listener.addPlotEntry(new PlotEntry(ufd, color));
		}
		
		dismiss();
	}
}
