package com.knpl.simplecalculator;

import java.util.ArrayList;
import com.knpl.simplecalculator.storage.CalculatorDb;
import com.knpl.simplecalculator.util.Globals;
import com.knpl.simplecalculator.nodes.UserConstDef;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConstDefFragment extends ListFragment {
	private SimpleCalculatorActivity activity;
	private ArrayAdapter<UserConstDef> adapter;
	private EditText input;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_deflist, container, false);
		
		Globals defs = Globals.getInstance();
		ArrayList<UserConstDef> userConstDefs =
				new ArrayList<UserConstDef>(defs.getUserConstDefs());
		adapter = new ArrayAdapter<UserConstDef>(getActivity(), 
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
		Globals defs = Globals.getInstance();
		UserConstDef ucd = adapter.getItem(position);
		input.setText(ucd.getDescription());
		input.setSelection(input.length());
		String id = ucd.getName();
		defs.removeUserConstDef(id);
		CalculatorDb.deleteUCD(id);
		adapter.remove(ucd);
	}
	
	public void remove(int position) {
		Globals defs = Globals.getInstance();
		UserConstDef ucd = adapter.getItem(position);
		String id = ucd.getName();
		defs.removeUserConstDef(id);
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
