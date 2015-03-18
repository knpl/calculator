package com.knpl.simplecalculator;

import java.util.ArrayList;

import com.knpl.simplecalculator.nodes.Signature;
import com.knpl.simplecalculator.util.FuncDef;
import com.knpl.simplecalculator.util.Globals;
import com.knpl.simplecalculator.util.UserFuncDef;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

public class FuncDefFragment extends ListFragment {
	
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
			createFuncDefDialog((UserFuncDef)funcDef).show(getChildFragmentManager(), null);
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
			createFuncDefDialog(null).show(getChildFragmentManager(), null);
			break;
			
		default:
			;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public static FuncDefDialog createFuncDefDialog(UserFuncDef userFuncDef) {
		FuncDefDialog dialog = new FuncDefDialog();
		Bundle args = new Bundle();
		boolean edit = userFuncDef != null;
		args.putBoolean("edit", edit);
		if (edit) {
			args.putSerializable("signature", userFuncDef.getSignature());
			args.putString("description", userFuncDef.getDescription());
		}
		dialog.setArguments(args);
		return dialog;
	}
	
	public static class FuncDefDialog extends DialogFragment
		implements DialogInterface.OnClickListener {
		
		private Signature signature;
		private String description;
		private boolean edit;
		
		private EditText editText;

		public void init(View view) {
			editText = (EditText) view.findViewById(R.id.dialog_expression);
			
			Bundle args = getArguments();
			edit = args.getBoolean("edit");
			
			if (edit) {
				description = args.getString("description");
				signature = (Signature) args.getSerializable("signature");
			}
			else {
				description = null;
				signature = null;
			}
		}

		@Override @NonNull
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View content = inflater.inflate(R.layout.fragment_funcdef_dialog, null);
			
			init(content);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setView(content)
				   .setTitle("new function")
				   .setPositiveButton("save", this)
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
				android.util.Log.d("mytag", "delete");
			}
		}
		
		public void onPositive() {
		}
	}
	
}
