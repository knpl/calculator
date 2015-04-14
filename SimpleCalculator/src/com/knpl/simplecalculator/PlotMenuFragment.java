package com.knpl.simplecalculator;

import java.util.ArrayList;
import java.util.List;
import afzkl.development.colorpickerview.dialog.ColorPickerDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.knpl.simplecalculator.nodes.FuncDefNode;
import com.knpl.simplecalculator.nodes.Signature;
import com.knpl.simplecalculator.nodes.UserFuncDef;
import com.knpl.simplecalculator.nodes.Var;
import com.knpl.simplecalculator.parser.Lexer;
import com.knpl.simplecalculator.parser.Parser;
import com.knpl.simplecalculator.plot.Mapper;
import com.knpl.simplecalculator.plot.ProgramMapper;
import com.knpl.simplecalculator.util.Pair;
import com.knpl.simplecalculator.util.Program;

public class PlotMenuFragment extends ListFragment {
	
	public static final int[] colors = {
		0xFFFF0000, 0xFF00FF00, 0xFF0000FF, /* Red, Green, Blue */
		0xFF00FFFF, 0xFFFF00FF, 0xFFFFFF00, /* Cyan, Magenta, Yellow */
		0xFFFFA500, 0xFF7171C6, 0xFF228B22, /* Orange, Slateblue, Forestgreen */
		0xFF9ACD32, 0xFF4B0082, 0xFF8A2BE2, /* Olivedrab, Indigo, Blueviolet */
		0xFFFFD700, 0xFFFF6347, 0xFFFA8072  /* Gold, Tomato, Salmon */
	};
	
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
	
	private PlotEntryAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.fragment_plotmenu, container, false);
		adapter = new PlotEntryAdapter(activity, plotEntries);
		setListAdapter(adapter);
		
		colorIndicator = (ImageView) view.findViewById(R.id.color_indicator);
		colorIndicator.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				colorPickerDialog.show();
			}
		});
		setColor(randomColor());
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
	
	public void setColor(int color) {
		this.color = color;
		colorIndicator.getBackground().setColorFilter(color, Mode.MULTIPLY);
	}
	
	public int randomColor() {
		return colors[(int)Math.round(Math.random()*(colors.length - 1))];
	}
	
	public void edit(int position) {
		PlotEntry entry = adapter.getItem(position);
		setColor(entry.color);
		input.setText(entry.userFuncDef.getDescription());
		input.setSelection(input.length());
		input.requestFocus();
		remove(position);
	}
	
	public void remove(int position) {
		adapter.remove(adapter.getItem(position));
	}
	
	private ColorPickerDialog createColorPickerDialog() {
		final ColorPickerDialog dialog = new ColorPickerDialog(activity, color);
		
		dialog.setAlphaSliderVisible(false);
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
		
		adapter.add(new PlotEntry(userFuncDef, color));
		input.setText("f(x) = ");
		input.setSelection(input.length());
		setColor(randomColor());
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
		case R.id.action_clear:
			adapter.clear();
			break;
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
	}

	@Override
	public void onDetach() {
		super.onDetach();
		activity = null;
	}
	
	public class PlotEntryAdapter extends ArrayAdapter<PlotEntry> {
		
		private class ViewHolder {
			public final TextView tv;
			public final ImageView iv;
			
			public ViewHolder(TextView tv, ImageView iv) {
				this.tv = tv;
				this.iv = iv;
			}
		}
		
		public PlotEntryAdapter(Context context, List<PlotEntry> plotEntries) {
			super(context, R.layout.plotentry_row_layout, plotEntries);
		}
		
		@Override
		public PlotEntry getItem(int position) {
			return super.getItem((getCount()-1) - position);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.plotentry_row_layout, parent, false);
				
				ViewHolder viewHolder = new ViewHolder(
						(TextView) convertView.findViewById(R.id.name),
						(ImageView) convertView.findViewById(R.id.color));
				
				convertView.setTag(viewHolder);
			}
			
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			
			PlotEntry item = getItem(position);
			viewHolder.iv.getBackground().setColorFilter(item.color, Mode.MULTIPLY);
			viewHolder.tv.setText(item.toString());
			
			return convertView;
		}
	}
}


