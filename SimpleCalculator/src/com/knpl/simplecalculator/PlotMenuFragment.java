package com.knpl.simplecalculator;

import java.util.ArrayList;
import java.util.List;
import afzkl.development.colorpickerview.dialog.ColorPickerDialog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.knpl.simplecalculator.nodes.FuncDefNode;
import com.knpl.simplecalculator.nodes.Signature;
import com.knpl.simplecalculator.nodes.Var;
import com.knpl.simplecalculator.parser.Lexer;
import com.knpl.simplecalculator.parser.Parser;
import com.knpl.simplecalculator.plot.Mapper;
import com.knpl.simplecalculator.plot.ProgramMapper;
import com.knpl.simplecalculator.util.Pair;
import com.knpl.simplecalculator.util.Program;
import com.knpl.simplecalculator.util.UserFuncDef;

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

	public static final int REQUEST_CODE = 0;
	
	private PlotListener listener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.fragment_plotmenu, container, false);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {	
		PlotEntry entry = (PlotEntry) getListAdapter().getItem(position);
		createPlotEntryDialog(entry, position).show(getChildFragmentManager(), null);
		super.onListItemClick(l, v, position, id);
	}
	
	public PlotEntryDialog createPlotEntryDialog(PlotEntry entry, int position) {
		PlotEntryDialog dialog = new PlotEntryDialog();
		Bundle args = new Bundle();
		args.putInt("position", position);
		if (entry != null) {
			args.putInt("color", entry.color);
			args.putString("name", entry.userFuncDef.getSignature().getName());
			args.putString("source", entry.userFuncDef.getDescription());
		}
		dialog.setArguments(args);
		dialog.setTargetFragment(this, REQUEST_CODE);
        return dialog;
	}
	

	private void compileAndPlot() {
		ArrayList<Pair<Mapper, Integer>> mappers 
			= new ArrayList<Pair<Mapper, Integer>>(plotEntries.size());
		Program p;
		for (PlotEntry entry : plotEntries) {
			try {
				p = entry.userFuncDef.getProgram();
				mappers.add(new Pair<Mapper, Integer>(new ProgramMapper(p), entry.color));
			}
			catch (Exception e) {
	    		e.printStackTrace();
			}
		}
		listener.plot(mappers);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE) {
			((PlotEntryAdapter) getListAdapter()).notifyDataSetChanged();
		}
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
			createPlotEntryDialog(null, -1).show(getChildFragmentManager(), null);
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

	
	public static class PlotEntryDialog extends DialogFragment
		implements DialogInterface.OnClickListener {
		
//		public static final int DEFAULT_COLOR = Color.BLACK;
		
		private EditText exprEditText;
		private ColorPickerDialog colorPickerDialog;
		private ImageView colorIndicator;
		
		private String name;
		private String source;
		private int position;
		
		private ColorPickerDialog createColorPickerDialog(int color) {
			final ColorPickerDialog colorDialog = new ColorPickerDialog(getActivity(), color);
			colorDialog.setAlphaSliderVisible(true);
			colorDialog.setTitle("Pick a Color");
			
			colorDialog.setButton(DialogInterface.BUTTON_POSITIVE,
					getString(android.R.string.ok), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					colorIndicator.getBackground().setColorFilter(colorPickerDialog.getColor(), Mode.MULTIPLY);
				}
			});
			colorDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
					getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {}
			});
			
			return colorDialog;
		}
		
		private void init(View view) {
			exprEditText = (EditText) view.findViewById(R.id.dialog_expression);
			colorIndicator = (ImageView) view.findViewById(R.id.dialog_color_indicator);
			
			Bundle args = getArguments();
			position = args.getInt("position");
			
			int color = args.getInt("color");
			if (color == 0) {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
				color = prefs.getBoolean("pref_key_dark_background", false) ? Color.WHITE : Color.BLACK;
			}
			
			name = args.getString("name");
			if (name == null)
				name = "New function";
			
			source = args.getString("source");
			if (source == null)
				source = "y(x) = ";
			
			exprEditText.setText(source);
			colorIndicator.getBackground().setColorFilter(color, Mode.MULTIPLY);
			colorPickerDialog = createColorPickerDialog(color);
				
			colorIndicator.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					colorPickerDialog.show();
				}
			});
		}
		
		@SuppressLint("InflateParams") @Override @NonNull
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View content = inflater.inflate(R.layout.fragment_plotmenu_dialog, null);
			
			init(content);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setView(content)
				   .setTitle(name)
				   .setPositiveButton("save", this)
				   .setNegativeButton((position != -1) ? "delete" : "cancel", this);
			
			final AlertDialog d = builder.create();
			d.setOnShowListener(new DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialog) {
					d.getButton(AlertDialog.BUTTON_POSITIVE)
						.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							onPositive();
						}
					});
				}
			});
			return d;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			
			case DialogInterface.BUTTON_NEGATIVE:
				if (position != -1)
					plotEntries.remove(position);
				break;
				
			default:
				;
			}
		}
		
		private void onPositive() {
			String expr = exprEditText.getText().toString();
			int color = colorPickerDialog.getColor();
		
			Parser parser = new Parser(new Lexer(expr));
			if (!parser.funcDef()) {
				displayMessage("Syntax error");
				return;
			}
			
			FuncDefNode funcDefNode = (FuncDefNode) parser.getResult();
			Signature signature = funcDefNode.getSignature();
			if (position != -1) {
				if (!name.equals(signature.getName())) {
					displayMessage("Can't rename function. Create a new function instead.");
					return;
				}
			}
			
			List<Var> parameters = signature.getParameters();
			if (parameters.size() != 1 || !parameters.get(0).getName().equals("x")) {
				displayMessage(""+signature);
				return;
			}
			
			UserFuncDef userFuncDef;
			try {
				userFuncDef = new UserFuncDef(funcDefNode);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				displayMessage(ex.getMessage());
				return;
			}
			
			if (position != -1)
				plotEntries.set(position, new PlotEntry(userFuncDef, color));
			else
				plotEntries.add(new PlotEntry(userFuncDef, color));
			
			dismiss();
		}
		
		@Override
		public void onDismiss(DialogInterface dialog) {
			getTargetFragment().onActivityResult(getTargetRequestCode(), REQUEST_CODE, null);
			super.onDismiss(dialog);
		}

		private void displayMessage(String message) {
			Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
		}
	}
}


