package com.knpl.simplecalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import afzkl.development.colorpickerview.dialog.ColorPickerDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.knpl.simplecalculator.nodes.Expr;
import com.knpl.simplecalculator.nodes.Signature;
import com.knpl.simplecalculator.nodes.Var;
import com.knpl.simplecalculator.parser.Lexer;
import com.knpl.simplecalculator.parser.Parser;
import com.knpl.simplecalculator.plot.Mapper;
import com.knpl.simplecalculator.plot.ProgramMapper;
import com.knpl.simplecalculator.util.GlobalDefinitions;
import com.knpl.simplecalculator.util.Pair;
import com.knpl.simplecalculator.util.Program;
import com.knpl.simplecalculator.util.UserFuncDef;
import com.knpl.simplecalculator.visitors.PrettyPrint;
import com.knpl.simplecalculator.visitors.Resolve;

public class PlotMenuFragment extends ListFragment {
	
	public static class PlotEntry {
		public final UserFuncDef userFuncDef;
		public final int color;
		
		public PlotEntry(UserFuncDef userFuncDef, int color) {
			this.userFuncDef = userFuncDef;
			this.color = color;
		}
		
		@Override
		public String toString() {
			return userFuncDef.toString();
		}
	}
	
	private static final ArrayList<PlotEntry> plotEntries = new ArrayList<PlotEntry>();
	
	private PlotListener listener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		android.util.Log.d("mytag", "PlotMenuFragment: onCreateView");
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.fragment_plotmenu, container, false);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {	
		PlotEntry entry = (PlotEntry) getListAdapter().getItem(position);
		createPlotEntryDialog(entry, position).show(getChildFragmentManager(), null);
		super.onListItemClick(l, v, position, id);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.plotmenu_actions, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.action_plot:
			compileAndPlot();
			break;
			
		case R.id.action_new_function:
			createPlotEntryDialog().show(getChildFragmentManager(), null);
			break;
			
		default:
			;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof PlotListener) {
			listener = (PlotListener) activity;
			setListAdapter(new PlotEntryAdapter(activity, plotEntries));
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet OptionsFragment.PlotListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}
	
	public interface PlotListener {
		void plot(ArrayList<Pair<Mapper, Integer>> mappers);
	}

	private void compileAndPlot() {
		ArrayList<Pair<Mapper, Integer>> mappers = new ArrayList<Pair<Mapper, Integer>>(plotEntries.size());
		Program p;
		for (PlotEntry entry : plotEntries) {
			try {
				p = entry.userFuncDef.getProgram();
				mappers.add(new Pair<Mapper, Integer>(
						new ProgramMapper(p), entry.color));
			}
			catch (Exception e) {
	    		e.printStackTrace();
			}
		}
		listener.plot(mappers);
	}
	
	public PlotEntryDialog createPlotEntryDialog(PlotEntry entry, int position) {
		PlotEntryDialog dialog = new PlotEntryDialog();
		dialog.setPlotEntry(entry, position);
        return dialog;
	}
	
	public PlotEntryDialog createPlotEntryDialog() {
		PlotEntryDialog dialog = new PlotEntryDialog();
		dialog.setPlotEntry(null, -1);
        return dialog;
	}
	
	public class PlotEntryDialog extends DialogFragment {
		
		public static final int DEFAULT_COLOR = Color.BLACK;
		
		private EditText nameEditText;
		private EditText exprEditText;
		private ColorPickerDialog colorPickerDialog;
		private ImageView selectedColorIndicator;
		
		private PlotEntry plotEntry;
		private int position;
		
		public void setPlotEntry(PlotEntry entry, int position) {
			plotEntry = entry;
			this.position = position;
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
			return "";
		}
		
		private void init(View view) {
			nameEditText = (EditText) view.findViewById(R.id.dialog_function_name);
			exprEditText = (EditText) view.findViewById(R.id.dialog_expression);
			selectedColorIndicator = (ImageView) view.findViewById(R.id.dialog_color_indicator);
			
			if (plotEntry != null) {
				nameEditText.setText(plotEntry.userFuncDef.getSignature().getName());
				nameEditText.setEnabled(false);
				exprEditText.setText(plotEntry.userFuncDef.getSource());
				selectedColorIndicator.setBackgroundColor(plotEntry.color);
				colorPickerDialog = createColorPickerDialog(plotEntry.color);
			}
			else {
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
		
		@Override
		@NonNull
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View content = inflater.inflate(R.layout.fragment_dialog, null);
			
			init(content);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setView(content)
				   .setTitle((plotEntry != null) ? ""+plotEntry.userFuncDef.getSignature() 
						   						 : "new function")
				   .setPositiveButton("save", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
				   })
				   .setNegativeButton((plotEntry != null) ? "delete" : "cancel", new DialogInterface.OnClickListener() {
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
			dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onPositive();
				}
			});
			dialog.getButton(Dialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onNegative();
				}
			});
		}
		
		private void onNegative() {
			String name = nameEditText.getText().toString();
			if ((plotEntry != null)) {
				GlobalDefinitions.getInstance().removeUserFuncDef(name);
				plotEntries.remove(position);
				((PlotEntryAdapter)getListAdapter()).notifyDataSetChanged();
			}
			dismiss();
		}
		
		private void onPositive() {
			String name = nameEditText.getText().toString();
			String expr = exprEditText.getText().toString();
			int color = colorPickerDialog.getColor();
			
			GlobalDefinitions defs = GlobalDefinitions.getInstance();
			
			if (plotEntry == null) {
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
				e = (Expr) e.accept(visitor, null);
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
				e.accept(prettyPrint, null);
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
			if ((plotEntry != null)) {
				plotEntries.set(position, new PlotEntry(ufd, color));		
			}
			else {
				plotEntries.add(new PlotEntry(ufd, color));
			}
			((PlotEntryAdapter)getListAdapter()).notifyDataSetChanged();
			
			dismiss();
		}
		
		private void displayMessage(String message) {
			Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
		}
	}
}


