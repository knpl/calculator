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
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
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

import com.knpl.simplecalculator.nodes.FuncDefNode;
import com.knpl.simplecalculator.nodes.Signature;
import com.knpl.simplecalculator.nodes.Var;
import com.knpl.simplecalculator.parser.Lexer;
import com.knpl.simplecalculator.parser.Parser;
import com.knpl.simplecalculator.plot.Mapper;
import com.knpl.simplecalculator.plot.ProgramMapper;
import com.knpl.simplecalculator.util.Globals;
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
			args.putString("source", entry.userFuncDef.getSource());
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

	
	public static class PlotEntryDialog extends DialogFragment {
		
		public static final int DEFAULT_COLOR = Color.BLACK;
		
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

		private String getUnusedFunctionName() {
			Globals defs = Globals.getInstance();
			for (char c = 'f'; c < 'z'; ++c) {
				String name = String.valueOf(c);
				if (defs.getFunctionDefinition(name) == null) {
					return name;
				}
			}
			return "";
		}
		
		private void init(View view) {
			exprEditText = (EditText) view.findViewById(R.id.dialog_expression);
			colorIndicator = (ImageView) view.findViewById(R.id.dialog_color_indicator);
			
			Bundle args = getArguments();
			position = args.getInt("position");
			
			int color = args.getInt("color");
			if (color == 0)
				color = DEFAULT_COLOR;
			
			name = args.getString("name");
			if (name == null)
				name = "New function";
			
			source = args.getString("source");
			if (source == null)
				source = getUnusedFunctionName() + "(x) = ";
			
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
			View content = inflater.inflate(R.layout.fragment_dialog, null);
			
			init(content);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setView(content)
				   .setTitle(name)
				   .setPositiveButton("save", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
				   })
				   .setNegativeButton((position != -1) ? "delete" : "cancel", new DialogInterface.OnClickListener() {
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
			if (position != -1) {
				Globals.getInstance().removeUserFuncDef(name);
				plotEntries.remove(position);
			}
			dismiss();
		}
		
		private void onPositive() {
			String expr = exprEditText.getText().toString();
			int color = colorPickerDialog.getColor();
		
			Parser parser = new Parser(new Lexer(expr));
			if (!parser.functionDefinition()) {
				displayMessage("Syntax error: "+expr);
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
				userFuncDef = funcDefNode.createUserFuncDef();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				displayMessage(ex.getMessage());
				return;
			}
			
			Globals defs = Globals.getInstance();
			defs.putUserFuncDef(userFuncDef);
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


