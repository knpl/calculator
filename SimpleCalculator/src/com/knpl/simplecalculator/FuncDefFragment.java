package com.knpl.simplecalculator;

import java.util.ArrayList;

import com.knpl.simplecalculator.nodes.FuncDefNode;
import com.knpl.simplecalculator.nodes.UserFuncDef;
import com.knpl.simplecalculator.parser.Lexer;
import com.knpl.simplecalculator.parser.Parser;
import com.knpl.simplecalculator.storage.CalculatorDb;
import com.knpl.simplecalculator.util.Globals;

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

public class FuncDefFragment extends ListFragment {
	private SimpleCalculatorActivity activity;
	private ArrayAdapter<UserFuncDef> adapter;
	
	private EditText input;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_deflist, container, false);
		
		Globals defs = Globals.getInstance();
		ArrayList<UserFuncDef> userFuncDefs = new ArrayList<UserFuncDef>(
			defs.getUserFuncDefMap().values());
		adapter = new ArrayAdapter<UserFuncDef>(getActivity(), 
			android.R.layout.simple_list_item_1, userFuncDefs);
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
		Parser parser = new Parser(new Lexer(inputText));
		if (!parser.funcDef()) {
			displayMessage("Syntax error.");
			return;
		}
		
		FuncDefNode funcDefNode = (FuncDefNode) parser.getResult();
		Globals defs = Globals.getInstance();
		String newName = funcDefNode.getSignature().getName();
		
		if (defs.getFuncDef(newName) != null) {
			displayMessage("Function "+newName+" already exists.");
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
		
		defs.putUserFuncDef(userFuncDef);
		CalculatorDb.insertUFD(userFuncDef);
		adapter.add(userFuncDef);
		input.getText().clear();
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
		UserFuncDef ufd = adapter.getItem(position);
		input.setText(ufd.getDescription());
		input.setSelection(input.length());
		String id = ufd.getSignature().getName();
		defs.removeUserFuncDef(id);
		CalculatorDb.deleteUFD(id);
		adapter.remove(ufd);
	}
	
	public void remove(int position) {
		Globals defs = Globals.getInstance();
		UserFuncDef ufd = adapter.getItem(position);
		String id = ufd.getSignature().getName();
		defs.removeUserFuncDef(id);
		CalculatorDb.deleteUFD(id);
		adapter.remove(ufd);
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
