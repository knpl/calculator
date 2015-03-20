package com.knpl.simplecalculator;

import java.util.ArrayList;

import com.knpl.simplecalculator.nodes.FuncDefNode;
import com.knpl.simplecalculator.nodes.Signature;
import com.knpl.simplecalculator.parser.Lexer;
import com.knpl.simplecalculator.parser.Parser;
import com.knpl.simplecalculator.util.FuncDef;
import com.knpl.simplecalculator.util.Globals;
import com.knpl.simplecalculator.util.UserFuncDef;

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

public class FuncDefFragment extends ListFragment {
	
	public static final int NEW_FUNCDEF_REQUEST_CODE = 0,
							EDIT_FUNCTION_REQUEST_CODE = 1;
	
	public static final String EXTRA_NEW_FUNCDEF_ID = "EXTRA_NEW_FUNCDEF_ID";
	
	private static ArrayList<FuncDef> funcdefs = new ArrayList<FuncDef>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		
		Globals defs = Globals.getInstance();
		funcdefs = new ArrayList<FuncDef>(defs.getFuncDefMap().values());
		
		ArrayAdapter<FuncDef> adapter= 
				new ArrayAdapter<FuncDef>(getActivity(), 
						android.R.layout.simple_list_item_1, funcdefs);
		setListAdapter(adapter);
		
		return inflater.inflate(R.layout.fragment_funcdef, container, false);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		FuncDef funcDef = (FuncDef) getListAdapter().getItem(position);
		if (funcDef instanceof UserFuncDef) {
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
	
	public FuncDefDialog createFuncDefDialog(int position) {
		FuncDefDialog dialog = new FuncDefDialog();
		Bundle args = new Bundle();
		UserFuncDef userFuncDef = (UserFuncDef) getListAdapter().getItem(position);
		args.putSerializable("signature", userFuncDef.getSignature());
		args.putString("description", userFuncDef.getDescription());
		args.putInt("position", position);
		dialog.setTargetFragment(this, EDIT_FUNCTION_REQUEST_CODE);
		dialog.setArguments(args);
		return dialog;
	}
	
	public FuncDefDialog createFuncDefDialog() {
		FuncDefDialog dialog = new FuncDefDialog();
		dialog.setTargetFragment(this, NEW_FUNCDEF_REQUEST_CODE);
		return dialog;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		String id;
		Globals defs = Globals.getInstance();
		
		switch (requestCode) {
		
		case NEW_FUNCDEF_REQUEST_CODE:
			id = (String) (data.getExtras().get(EXTRA_NEW_FUNCDEF_ID));
			funcdefs.add(defs.getFuncDef(id));
			break;
			
		case EDIT_FUNCTION_REQUEST_CODE:
			if (data != null) {
				id = (String) data.getExtras().get(EXTRA_NEW_FUNCDEF_ID);
				funcdefs.set(resultCode, defs.getFuncDef(id));
				((ArrayAdapter<?>) getListAdapter()).notifyDataSetChanged();
			}
			else {
				funcdefs.remove(resultCode);
			}
			break;
			
		default:
			;
		}
		((ArrayAdapter<?>) getListAdapter()).notifyDataSetChanged();
	}
	
	public static class FuncDefDialog extends DialogFragment
		implements DialogInterface.OnClickListener {
		
		private Signature signature;
		private String description;
		private int position;
		private boolean edit;
		
		private EditText editText;

		public void init(View view) {
			editText = (EditText) view.findViewById(R.id.dialog_expression);
			
			Bundle args = getArguments();
			edit = getTargetRequestCode() == EDIT_FUNCTION_REQUEST_CODE;
			
			if (edit) {
				description = args.getString("description");
				signature = (Signature) args.getSerializable("signature");
				position = args.getInt("position");
				
				editText.setText(description);
			}
			else {
				description = null;
				signature = null;
				position = -1;
			}
		}

		@SuppressLint("InflateParams") @Override @NonNull
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View content = inflater.inflate(R.layout.fragment_funcdef_dialog, null);
			
			init(content);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setView(content)
				   .setTitle(edit ? "edit "+signature.getName() : "new function")
				   .setPositiveButton(edit ? "replace" : "save", this)
				   .setNegativeButton(edit ? "delete" : "cancel", this);
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
			if (which == AlertDialog.BUTTON_NEGATIVE && edit) {
				Globals defs = Globals.getInstance();
				defs.removeFuncDef(signature.getName());
				sendResultNegative();
			}
		}
		
		public void onPositive() {
			String input = editText.getText().toString();
			
			Parser parser = new Parser(new Lexer(input));
			if (!parser.functionDefinition()) {
				displayMessage("Syntax error.");
				return;
			}
			
			FuncDefNode funcDefNode = (FuncDefNode) parser.getResult();
			Globals defs = Globals.getInstance();
			String newName = funcDefNode.getSignature().getName();
			// Return if function name already exists, unless it is equal to the function name we are editting.
			if (!(edit && signature.getName().equals(newName)) && defs.getFuncDef(newName) != null) {
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
			
			if (edit)
				defs.removeFuncDef(signature.getName());
			defs.putFuncDef(userFuncDef);
			
			String newFuncDefId = userFuncDef.getSignature().getName();
			sendResultPositive(newFuncDefId);
			
			dismiss();
		}
		
		public void sendResultPositive(String id) {
			Intent data = new Intent();
			data.putExtra(EXTRA_NEW_FUNCDEF_ID, id);
			getTargetFragment().onActivityResult(getTargetRequestCode(), position, data);
			
		}
		
		public void sendResultNegative() {
			getTargetFragment().onActivityResult(getTargetRequestCode(), position, null);
		}
		
		private void displayMessage(String message) {
			Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
		}
	}
}
