package com.knpl.calc;

import java.util.ArrayList;
import com.knpl.calc.R;
import com.knpl.calc.nodes.defs.ConstDef;
import com.knpl.calc.nodes.defs.UserConstDef;
import com.knpl.calc.storage.CalculatorDb;
import com.knpl.calc.util.Globals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConstDefFragment extends ListFragment {
	private SimpleCalculatorActivity activity;
	private ArrayAdapter<ConstDef> adapter;
	private EditText input;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.fragment_deflist, container, false);
		
		Globals defs = Globals.getInstance();
		ArrayList<ConstDef> userConstDefs =
				new ArrayList<ConstDef>(defs.getUserConstDefs());
		adapter = new ArrayAdapter<ConstDef>(getActivity(), 
			android.R.layout.simple_list_item_1, userConstDefs);
		setListAdapter(adapter);
		
		input = (EditText) view.findViewById(R.id.input_definition);
		activity.registerEditTextToKeyboard(input);
		input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				validate(v.getText().toString());
				return true;
			}
		});
		input.setHint("Example: \u03C6 = (1+sqrt(5))/2");
		
		return view;
	}

	public void validate(String inputText) {
		try {
			UserConstDef ucd = UserConstDef.fromSource(inputText);
			Globals defs = Globals.getInstance();
			String newName = ucd.getName();
			if (defs.getConstDef(newName) != null) {
				throw new Exception("Constant "+newName+" already exists.");
			}
			
			ucd.resolve();
			defs.putUserConstDef(ucd);
			CalculatorDb.insertUCD(ucd);
			adapter.add(ucd);
			input.getText().clear();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			displayMessage(ex.getMessage());
			return;
		}
	}
	
	private void displayMessage(String message) {
		Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		final ConstDef cd = adapter.getItem(position);
		if (!(cd instanceof UserConstDef)) {
			return;
		}
		
		final CharSequence[] options = new CharSequence[]{"Edit", "Remove", "Cancel"};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("Choose action");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0: // Edit
					edit((UserConstDef) cd);
					break;
				case 1: // Remove
					remove((UserConstDef) cd);
					break;
				default:
					;
				}
			}
		});
		builder.show();
		
		super.onListItemClick(l, v, position, id);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.constdef_actions, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_show_builtin_constdefs:
			adapter.clear();
			adapter.addAll(Globals.getInstance().getBuiltinConstDefs());
			input.setVisibility(View.GONE);
			break;
		case R.id.action_show_user_constdefs:
			adapter.clear();
			adapter.addAll(Globals.getInstance().getUserConstDefs());
			input.setVisibility(View.VISIBLE);
			input.requestFocus();
			break;
		default:
			;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void edit(UserConstDef ucd) {
		input.setText(ucd.getDescription());
		input.setSelection(input.length());
		String id = ucd.getName();
		Globals.getInstance().removeUserConstDef(id);
		CalculatorDb.deleteUCD(id);
		adapter.remove(ucd);
	}
	
	public void remove(UserConstDef ucd) {
		String id = ucd.getName();
		Globals.getInstance().removeUserConstDef(id);
		CalculatorDb.deleteUCD(id);
		adapter.remove(ucd);
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
}
