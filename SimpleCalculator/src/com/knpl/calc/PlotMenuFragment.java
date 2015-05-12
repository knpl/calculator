package com.knpl.calc;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.knpl.calc.R;
import com.knpl.calc.nodes.Var;
import com.knpl.calc.nodes.defs.UserFuncDef;
import com.knpl.calc.plot.Mapper;
import com.knpl.calc.plot.ParametricMapper;
import com.knpl.calc.plot.PolarMapper;
import com.knpl.calc.plot.ProgramMapper;
import com.knpl.calc.plot.Range;
import com.knpl.calc.plot.XtoYComputerMapper;
import com.knpl.calc.util.PlotComputer;
import com.knpl.calc.visitors.NumEvaluate;

public class PlotMenuFragment extends ListFragment {
	
	public static final int[] colors = {
		0xFFFF0000, 0xFF00FF00, 0xFF0000FF, /* Red, Green, Blue */
		0xFF00FFFF, 0xFFFF00FF, 0xFFFFFF00, /* Cyan, Magenta, Yellow */
		0xFFFFA500, 0xFF7171C6, 0xFF228B22, /* Orange, Slateblue, Forestgreen */
		0xFF9ACD32, 0xFF4B0082, 0xFF8A2BE2, /* Olivedrab, Indigo, Blueviolet */
		0xFFFFD700, 0xFFFF6347, 0xFFFA8072  /* Gold, Tomato, Salmon */
	};
	
	private static final ArrayList<PlotEntry> plotEntries = new ArrayList<PlotEntry>();
	
	private SimpleCalculatorActivity activity;
	
	private ColorPickerDialog colorPickerDialog;
	
	private LinearLayout range;
	private TextView rangeLabel;
	private EditText rangeFrom,
					 rangeTo;
	
	private EditText input,
					 secondInput;
	private ImageView colorIndicator;
	private int color;
	
	private PlotEntryAdapter adapter;
	private PlotMode mode;
	
	private NormalMode normalMode;
	private PolarMode polarMode;
	private ParametricMode parametricMode;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.fragment_plotmenu, container, false);
		
		input = (EditText) view.findViewById(R.id.expression);
		secondInput = (EditText) view.findViewById(R.id.expression2);
		range = (LinearLayout) view.findViewById(R.id.range);
		rangeLabel = (TextView) range.findViewById(R.id.range_label);
		rangeFrom = (EditText) range.findViewById(R.id.range_from);
		rangeTo = (EditText) range.findViewById(R.id.range_to);
		
		activity.registerEditTextToKeyboard(input);
		activity.registerEditTextToKeyboard(secondInput);
		activity.registerEditTextToKeyboard(rangeFrom);
		activity.registerEditTextToKeyboard(rangeTo);
		
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
		
		normalMode = new NormalMode();
		polarMode = new PolarMode();
		parametricMode = new ParametricMode();
		setMode(normalMode);
		
		OnEditorActionListener editorAction = new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				mode.addPlotEntry();
				return true;
			}
		};
		
		input.setOnEditorActionListener(editorAction);
		secondInput.setOnEditorActionListener(editorAction);
		rangeFrom.setOnEditorActionListener(editorAction);
		rangeTo.setOnEditorActionListener(editorAction);
		
		return view;
	}
	
	public PlotMode getMode(PlotType type) {
		switch (type) {
		case NORMAL: return normalMode;
		case POLAR:	 return polarMode;
		case PARAMETRIC: return parametricMode;
		default: return normalMode;
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("state", mode.getType().name());
		super.onSaveInstanceState(outState);
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
					PlotEntry entry = adapter.getItem(position);
					setMode(getMode(entry.getType()));
					mode.edit(entry);
					break;
				case 1: // Remove
					adapter.remove(adapter.getItem(position));
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
	
	public void setMode(PlotMode mode) {
		this.mode = mode;
		mode.init();
	}
	
	private ColorPickerDialog createColorPickerDialog() {
		final ColorPickerDialog dialog = new ColorPickerDialog(activity, color);
		
		dialog.setAlphaSliderVisible(false);
		dialog.setTitle("Pick a Color");
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok),
				new DialogInterface.OnClickListener() {
			@Override 
			public void onClick(DialogInterface unused, int which) {
				setColor(dialog.getColor());
			}
		});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel),
				new DialogInterface.OnClickListener() {
			@Override 
			public void onClick(DialogInterface dialog, int which) {}
		});
		
		return dialog;
	}
	
	private void displayMessage(String message) {
		Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
	}
	
	private void compileAndPlot() {
		ArrayList<Mapper> mappers = new ArrayList<Mapper>(plotEntries.size());
		for (PlotEntry entry : plotEntries) {
			try {
				mappers.add(entry.getMapper());
			}
			catch (Exception ex) {
	    		ex.printStackTrace();
	    		displayMessage(ex.getMessage());
			}
		}
		activity.plot(mappers);
	}
	
	private void compileAndPlot3d() {
		ArrayList<Mapper> mappers = new ArrayList<Mapper>(plotEntries.size());
		for (PlotEntry entry : plotEntries) {
			try {
				if (entry.type == PlotType.NORMAL) {
					mappers.add(new XtoYComputerMapper(
							new PlotComputer(entry.ufd.getProgram()), entry.color));
				}
			}
			catch (Exception ex) {
	    		ex.printStackTrace();
	    		displayMessage(ex.getMessage());
			}
		}
		activity.plot3d(mappers);
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
		case R.id.action_plot_3d:
			compileAndPlot3d();
			break;
		case R.id.action_plot:
			compileAndPlot();
			break;
		case R.id.action_toggle_normal_mode:
			setMode(normalMode);
			break;
		case R.id.action_toggle_parametric_mode:
			setMode(parametricMode);
			break;
		case R.id.action_toggle_polar_mode:
			setMode(polarMode);
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
	
	private enum PlotType {NORMAL, POLAR, PARAMETRIC};
	
	interface PlotMode {
		PlotType getType();
		void addPlotEntry();
		void edit(PlotEntry entry);
		void init();
	}
	
	private class NormalMode implements PlotMode {	
		@Override
		public void addPlotEntry() {
			String source = input.getText().toString();
			UserFuncDef ufd;
			try {
				ufd = UserFuncDef.fromSource(source);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				displayMessage(ex.getMessage());
				return;
			}
			
			List<Var> params = ufd.getSignature().getParameters();
			if (params.size() != 1) {
				displayMessage("Function must have a single parameter. (Named x)");
				return;
			}
			
			String param = params.get(0).getName();
			if (!param.equals("x")) {
				displayMessage("Parameter must be named x.");
				return;
			}
			
			try {
				ufd.resolve();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				displayMessage(ex.getMessage());
				return;
			}
			
			adapter.add(new PlotEntry(PlotType.NORMAL, ufd, color));
			input.setText("f(x) = ");
			input.setSelection(input.length());
			setColor(randomColor());
		}

		@Override
		public void init() {
			secondInput.setVisibility(View.GONE);
			range.setVisibility(View.GONE);
			
			input.setText("f(x) = ");
			input.setSelection(input.length());
		}

		@Override
		public PlotType getType() {
			return PlotType.NORMAL;
		}

		@Override
		public void edit(PlotEntry entry) {
			setColor(entry.color);
			input.setText(entry.ufd.toString());
			input.setSelection(input.length());
			adapter.remove(entry);
		}
	}
	
	private class PolarMode implements PlotMode {
		@Override
		public void addPlotEntry() {
			String source = input.getText().toString();
			
			UserFuncDef ufd;
			try {
				ufd = UserFuncDef.fromSource(source);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				displayMessage(ex.getMessage());
				return;
			}
			
			List<Var> params = ufd.getSignature().getParameters();
			if (params.size() != 1) {
				displayMessage("Function must have a single parameter. (Named \u03B8)");
				return;
			}
			
			String param = params.get(0).getName();
			if (!param.equals("\u03B8")) {
				displayMessage("Parameter must be named \u03B8.");
				return;
			}
			
			float from = (float) NumEvaluate.fromString(rangeFrom.getText().toString());
			float to = (float) NumEvaluate.fromString(rangeTo.getText().toString());
			if (Float.isNaN(from) || Float.isNaN(to)) {
				displayMessage("Invalid range.");
				return;
			}
			
			try {
				ufd.resolve();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				displayMessage(ex.getMessage());
				return;
			}
			
			adapter.add(new PlotEntry(PlotType.POLAR, ufd, new Range(from, to), color));
			input.setText("r(\u03B8) = ");
			input.setSelection(input.length());
			setColor(randomColor());
		}

		@Override
		public void init() {
			secondInput.setVisibility(View.GONE);
			rangeLabel.setText("\u03B8");
			range.setVisibility(View.VISIBLE);
			
			input.setText("r(\u03B8) = ");
			input.setSelection(input.length());
			rangeFrom.setText("0");
			rangeTo.setText("2*\u03C0");
		}

		@Override
		public PlotType getType() {
			return PlotType.POLAR;
		}

		@Override
		public void edit(PlotEntry entry) {
			setColor(entry.color);
			rangeFrom.setText("0");
			rangeTo.setText("2*\u03C0");
			input.setText(entry.ufd.toString());
			input.setSelection(input.length());
			adapter.remove(entry);
		}
	}
	
	private class ParametricMode implements PlotMode {
		@Override
		public void addPlotEntry() {
			String xsource = input.getText().toString(),
				   ysource = secondInput.getText().toString();
			
			UserFuncDef xufd, yufd;
			try {
				xufd = UserFuncDef.fromSource(xsource);
				yufd = UserFuncDef.fromSource(ysource);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				displayMessage(ex.getMessage());
				return;
			}
			
			List<Var> xparams = xufd.getSignature().getParameters(),
					  yparams = yufd.getSignature().getParameters();
			if (xparams.size() != 1 || yparams.size() != 1) {
				displayMessage("Both functions must have a single parameter. (Named t)");
				return;
			}
			
			String xparam = xparams.get(0).getName(),
				   yparam = yparams.get(0).getName();
			if (!xparam.equals("t") || !yparam.equals("t")) {
				displayMessage("Parameter must be named t.");
				return;
			}
			
			float from = (float) NumEvaluate.fromString(rangeFrom.getText().toString());
			float to = (float) NumEvaluate.fromString(rangeTo.getText().toString());
			if (Float.isNaN(from) || Float.isNaN(to)) {
				displayMessage("Invalid range.");
				return;
			}
			
			try {
				xufd.resolve();
				yufd.resolve();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				displayMessage(ex.getMessage());
				return;
			}
			
			adapter.add(new PlotEntry(PlotType.PARAMETRIC, xufd, yufd,
									  new Range(from, to), color));
			input.setText("x(t) = ");
			secondInput.setText("y(t) = ");
			input.setSelection(input.length());
			setColor(randomColor());
		}
		
		@Override
		public void init() {
			secondInput.setVisibility(View.VISIBLE);
			rangeLabel.setText("t");
			range.setVisibility(View.VISIBLE);
			
			input.setText("x(t) = ");
			input.setSelection(input.length());
			secondInput.setText("y(t) = ");
			rangeFrom.setText("0");
			rangeTo.setText("10");
		}
		
		@Override
		public PlotType getType() {
			return PlotType.PARAMETRIC;
		}

		@Override
		public void edit(PlotEntry entry) {
			setColor(entry.color);
			
			secondInput.setText(entry.ufd2.toString());
			rangeFrom.setText("0");
			rangeTo.setText("10");
			
			input.setText(entry.ufd.toString());
			input.setSelection(input.length());
			adapter.remove(entry);
		}
	}
	
	public static class PlotEntry {
		public final PlotType type;
		public final UserFuncDef ufd;
		public final UserFuncDef ufd2;
		public final Range range;
		public final int color;
		
		public PlotEntry(PlotType type, UserFuncDef ufd, UserFuncDef ufd2, Range range, int color) {
			this.ufd = ufd;
			this.ufd2 = ufd2;
			this.range = range;
			this.type = type;
			this.color = color;
		}
		
		public PlotEntry(PlotType type, UserFuncDef ufd, int color) {
			this(type, ufd, null, null, color);
		}
		
		public PlotEntry(PlotType type, UserFuncDef ufd, Range range, int color) {
			this(type, ufd, null, range, color);
		}
		
		public PlotType getType() {
			return type;
		}
		
		public Mapper getMapper() throws Exception {
			switch (type) {
			case NORMAL:
				return new ProgramMapper(new PlotComputer(ufd.getProgram()), color);
			case POLAR:
				return new PolarMapper(new PlotComputer(ufd.getProgram()), range, color);
			case PARAMETRIC:
				return new ParametricMapper(new PlotComputer(ufd.getProgram()), 
											new PlotComputer(ufd2.getProgram()), range, color);
			default:
				return new ProgramMapper(new PlotComputer(ufd.getProgram()), color);
			}
		}
		
		@Override
		public String toString() {
			StringBuffer buf = new StringBuffer();
			buf.append(ufd.toString());
			if (ufd2 != null) {
				buf.append(", ").append(ufd2.toString());
			}
			return buf.toString();
		}
	}
	
	public static class PlotEntryAdapter extends ArrayAdapter<PlotEntry> {
		
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


