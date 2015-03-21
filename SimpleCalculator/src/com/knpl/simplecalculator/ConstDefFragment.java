package com.knpl.simplecalculator;

import java.util.ArrayList;

import com.knpl.simplecalculator.nodes.ConstDef;
import com.knpl.simplecalculator.nodes.ConstDefNode;
import com.knpl.simplecalculator.parser.Lexer;
import com.knpl.simplecalculator.parser.Parser;
import com.knpl.simplecalculator.util.Globals;
import com.knpl.simplecalculator.nodes.UserConstDef;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ConstDefFragment extends ListFragment {
	public static final int NEW_CONSTDEF_REQUEST_CODE = 0,
							EDIT_CONSTDEF_REQUEST_CODE = 1;
	
	public static final String EXTRA_NEW_CONSTDEF_ID = "EXTRA_NEW_CONSTDEF_ID";
	
	private ArrayList<ConstDef> constDefs;
	private ArrayAdapter<ConstDef> adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		
		Globals defs = Globals.getInstance();
		constDefs = new ArrayList<ConstDef>(defs.getConstDefMap().values());
		adapter = new ArrayAdapter<ConstDef>(getActivity(), 
				android.R.layout.simple_list_item_1, constDefs);
		setListAdapter(adapter);
		
		return inflater.inflate(R.layout.fragment_funcdef, container, false);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		ConstDef constDef = (ConstDef) adapter.getItem(position);
		if (constDef instanceof UserConstDef) {
			createFuncDefDialog(position).show(getChildFragmentManager(), null);
		}
		super.onListItemClick(l, v, position, id);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.funcdef_actions, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.action_new_function:
			createFuncDefDialog().show(getChildFragmentManager(), null);
			break;
		
		default:
			;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public ConstDefDialog createFuncDefDialog(int position) {
		ConstDefDialog dialog = new ConstDefDialog();
		Bundle args = new Bundle();
		UserConstDef userConstDef = (UserConstDef) adapter.getItem(position);
		args.putString("name", userConstDef.getName());
		args.putString("description", userConstDef.getDescription());
		args.putInt("position", position);
		dialog.setTargetFragment(this, EDIT_CONSTDEF_REQUEST_CODE);
		dialog.setArguments(args);
		return dialog;
	}
	
	public ConstDefDialog createFuncDefDialog() {
		ConstDefDialog dialog = new ConstDefDialog();
		dialog.setTargetFragment(this, NEW_CONSTDEF_REQUEST_CODE);
		return dialog;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		String id;
		Globals defs = Globals.getInstance();
	
		switch (requestCode) {
		
		case NEW_CONSTDEF_REQUEST_CODE:
			id = (String) (data.getExtras().get(EXTRA_NEW_CONSTDEF_ID));
			constDefs.add(defs.getConstDef(id));
			break;
		
		case EDIT_CONSTDEF_REQUEST_CODE:
			if (data != null) {
				id = (String) data.getExtras().get(EXTRA_NEW_CONSTDEF_ID);
				constDefs.set(resultCode, defs.getConstDef(id));
			}
			else {
				constDefs.remove(resultCode);
			}
			break;
		
		default:
			;
		}
		adapter.notifyDataSetChanged();
	}
	
	public static class ConstDefDialog extends DialogFragment
		implements DialogInterface.OnClickListener {
		
		public static final int NO_POSITION = -1;
	
		private String oldName;
		private String description;
		private int position;
		private boolean edit;
		
		private EditText editText;
		
		public void init(View view) {
			editText = (EditText) view.findViewById(R.id.dialog_expression);
			
			Bundle args = getArguments();
			edit = (getTargetRequestCode() == EDIT_CONSTDEF_REQUEST_CODE);
		
			if (edit) {
				description = args.getString("description");
				oldName = args.getString("name");
				position = args.getInt("position");
				
				editText.setText(description);
			}
			else {
				description = null;
				oldName = null;
				position = NO_POSITION;
			}
		}
		
		@SuppressLint("InflateParams") @Override @NonNull
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View content = inflater.inflate(R.layout.fragment_funcdef_dialog, null);
			
			init(content);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setView(content)
			   .setTitle(edit ? "edit "+oldName : "new constant")
			   .setPositiveButton(edit ? "replace" : "save", this)
			   .setNegativeButton(edit ? "delete" : "cancel", this);
			final AlertDialog d = builder.create();
			d.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				d.getButton(AlertDialog.BUTTON_NEGATIVE)
				 .setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onNegative();
					}
				});
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
			
		public void onNegative() {
			if (edit) {
				Globals defs = Globals.getInstance();
				defs.removeConstDef(oldName);
				sendResultNegative();
			}
			dismiss();
		}
		
		public void onPositive() {
			String input = editText.getText().toString();
			
			Parser parser = new Parser(new Lexer(input));
			if (!parser.constDef()) {
				displayMessage("Syntax error.");
				return;
			}
			
			ConstDefNode constDefNode = (ConstDefNode) parser.getResult();
			Globals defs = Globals.getInstance();
			String newName = constDefNode.getName();
			// Return if  name already exists, unless it is equal to the name we are editting.
			if (!(edit && oldName.equals(newName)) 
					&& defs.getConstDef(newName) != null) {
				displayMessage("Constant "+newName+" already exists.");
				return;
			}
		
			UserConstDef userConstDef;
			try {
				userConstDef = new UserConstDef(constDefNode);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				displayMessage(ex.getMessage());
				return;
			}
		
			if (edit)
				defs.removeConstDef(oldName);
			defs.putConstDef(userConstDef);
			
			String newFuncDefId = userConstDef.getName();
			sendResultPositive(newFuncDefId);
			
			dismiss();
		}
		
		public void sendResultPositive(String id) {
			Intent data = new Intent();
			data.putExtra(EXTRA_NEW_CONSTDEF_ID, id);
			getTargetFragment().onActivityResult(getTargetRequestCode(), position, data);	
		}
		
		public void sendResultNegative() {
			getTargetFragment().onActivityResult(getTargetRequestCode(), position, null);
		}
		
		private void displayMessage(String message) {
			Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) { /* Never called */ }
	}
}
