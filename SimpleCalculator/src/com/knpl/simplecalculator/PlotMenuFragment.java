package com.knpl.simplecalculator;

import java.util.ArrayList;
import java.util.List;
import afzkl.development.colorpickerview.dialog.ColorPickerDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
	
	private SimpleCalculatorActivity activity;
	
	private ColorPickerDialog colorPickerDialog;
	
	private EditText input;
	private ImageView colorIndicator;
	private int color;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.fragment_plotmenu, container, false);
		
		colorIndicator = (ImageView) view.findViewById(R.id.color_indicator);
		colorIndicator.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				colorPickerDialog.show();
			}
		});

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		int color = prefs.getBoolean("pref_key_dark_background", false) ? Color.WHITE : Color.BLACK;
		setColor(color);
		
		colorPickerDialog = createColorPickerDialog();
		
		input = (EditText) view.findViewById(R.id.expression);
		activity.registerEditTextToKeyboard(input);
		input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				validate(v.getText().toString());
				return true;
			}
		});
		input.setText("f(x) = ");
		input.setSelection(input.length());
		
		return view;
	}
	
	public void setColor(int color) {
		colorIndicator.getBackground().setColorFilter(color, Mode.MULTIPLY);
		this.color = color;
	}

	@Override
	public void onListItemClick(ListView l, View v, final int position, long id) {	
		final CharSequence[] options = new CharSequence[]{"Edit", "Remove", "Cancel"};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("Choose action");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0: // Edit
					edit(position);
					break;
				case 1: // Remove
					remove(position);
					break;
				default:
					;
				}
			}
		});
		builder.show();
		
		super.onListItemClick(l, v, position, id);
	}
	
	public void edit(int position) {
		PlotEntry entry = (PlotEntry) getListAdapter().getItem(position);
		setColor(entry.color);
		input.setText(entry.userFuncDef.getDescription());
		input.setSelection(input.length());
		input.requestFocus();
		remove(position);
	}
	
	public void add(PlotEntry entry) {
		plotEntries.add(entry);
		((PlotEntryAdapter) getListAdapter()).notifyDataSetChanged();
	}
	
	public void remove(int position) {
		plotEntries.remove(position);
		((PlotEntryAdapter) getListAdapter()).notifyDataSetChanged();
	}
	
	private ColorPickerDialog createColorPickerDialog() {
		final ColorPickerDialog dialog = new ColorPickerDialog(activity, color);
		
		dialog.setAlphaSliderVisible(true);
		dialog.setTitle("Pick a Color");
		dialog.setButton(DialogInterface.BUTTON_POSITIVE,
				getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			@Override 
			public void onClick(DialogInterface unused, int which) {
				setColor(dialog.getColor());
			}
		});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
				getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
			@Override 
			public void onClick(DialogInterface dialog, int which) {}
		});
		
		return dialog;
	}
	
	private void displayMessage(String message) {
		Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
	}

	private void validate(String source) {
		Parser parser = new Parser(new Lexer(source));
		if (!parser.funcDef()) {
			displayMessage("Syntax error");
			return;
		}
		
		FuncDefNode funcDefNode = (FuncDefNode) parser.getResult();
		Signature signature = funcDefNode.getSignature();
		
		List<Var> parameters = signature.getParameters();
		if (parameters.size() != 1 || !parameters.get(0).getName().equals("x")) {
			displayMessage("Invalid signature: "+signature);
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
		
		add(new PlotEntry(userFuncDef, color));
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
		activity.plot(mappers);
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
			
		default:
			;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof SimpleCalculatorActivity)) {
			throw new ClassCastException(activity.toString()
					+ " must implement SimpleCalculatorActivity");
		}
		
		this.activity = (SimpleCalculatorActivity) activity;
		setListAdapter(new PlotEntryAdapter(activity, plotEntries));
	}

	@Override
	public void onDetach() {
		super.onDetach();
		activity = null;
	}
}


