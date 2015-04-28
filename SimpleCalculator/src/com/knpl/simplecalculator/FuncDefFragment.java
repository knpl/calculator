package com.knpl.simplecalculator;

import java.util.ArrayList;

import com.knpl.simplecalculator.nodes.FuncDef;
import com.knpl.simplecalculator.nodes.UserFuncDef;
import com.knpl.simplecalculator.storage.CalculatorDb;
import com.knpl.simplecalculator.util.Globals;

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

public class FuncDefFragment extends ListFragment {
	private SimpleCalculatorActivity activity;
	private ArrayAdapter<FuncDef> adapter;
	private EditText input;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.fragment_deflist, container, false);
		
		Globals defs = Globals.getInstance();
		ArrayList<FuncDef> userFuncDefs =
				new ArrayList<FuncDef>(defs.getUserFuncDefs());
		adapter = new ArrayAdapter<FuncDef>(getActivity(), 
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
		input.setHint("Example: d(a,b) = sqrt(a*a+b*b)");
		
		return view;
	}

	public void validate(String inputText) {
		try {
			UserFuncDef ufd = UserFuncDef.fromSource(inputText);
			Globals defs = Globals.getInstance();
			String name = ufd.getSignature().getName();
			if (defs.getFuncDef(name) != null) {
				throw new Exception("Function "+name+" already exists.");
			}
			
			ufd.resolve();
			defs.putUserFuncDef(ufd);
			CalculatorDb.insertUFD(ufd);
			adapter.add(ufd);
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
		final FuncDef fd = adapter.getItem(position);
		if (!(fd instanceof UserFuncDef)) {
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
					edit((UserFuncDef)fd);
					break;
				case 1: // Remove
					remove((UserFuncDef)fd);
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
		inflater.inflate(R.menu.funcdef_actions, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_show_builtin_funcdefs:
			adapter.clear();
			adapter.addAll(Globals.getInstance().getBuiltinFuncDefs());
			input.setVisibility(View.GONE);
			break;
		case R.id.action_show_user_funcdefs:
			adapter.clear();
			adapter.addAll(Globals.getInstance().getUserFuncDefs());
			input.setVisibility(View.VISIBLE);
			input.requestFocus();
			break;
		default:
			;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void edit(UserFuncDef ufd) {
		input.setText(ufd.toString());
		input.setSelection(input.length());
		String id = ufd.getSignature().getName();
		Globals.getInstance().removeUserFuncDef(id);
		CalculatorDb.deleteUFD(id);
		adapter.remove(ufd);
	}
	
	public void remove(UserFuncDef ufd) {
		String id = ufd.getSignature().getName();
		Globals.getInstance().removeUserFuncDef(id);
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
