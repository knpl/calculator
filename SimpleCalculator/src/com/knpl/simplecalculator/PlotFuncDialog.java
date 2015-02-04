package com.knpl.simplecalculator;

import com.knpl.simplecalculator.nodes.FuncDefNode;
import com.knpl.simplecalculator.nodes.Signature;
import com.knpl.simplecalculator.parser.Lexer;
import com.knpl.simplecalculator.parser.Parser;
import com.knpl.simplecalculator.visitors.Resolve;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PlotFuncDialog extends DialogFragment {
	
	private EditText input;
	private TextView message;
	

	public interface PlotFuncDialogListener {
        void addUserFuncDef(UserFuncDef ufd);
        void removeUserFuncDef(int position);
    }
	
	PlotFuncDialogListener listener;

	public PlotFuncDialog() {
	}
	
	private void confirm() {
		Parser parser = 
			new Parser(new Lexer(input.getText().toString().toCharArray()));
		
		if (!parser.definition()) {
			message.setText("Syntax Error");
			return;
		}
		
		FuncDefNode fdn = (FuncDefNode) parser.getResult();
		Signature sig = fdn.getSignature();
		UserFuncDef ufd;
		
		try {
			fdn.accept(new Resolve());
			
			ufd = new UserFuncDef(sig, fdn.getExpression());
			ufd.compile();
			

			GlobalDefinitions defs = GlobalDefinitions.getInstance();
			if(!defs.putUserFuncDef(ufd)) {
				throw new Exception("Function \""+sig.getName()+"\" already defined");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			message.setText("Caught Exception: "+e.getMessage());
			return;
		}
		
		listener.addUserFuncDef(ufd);
		getDialog().dismiss();
	}
	
	private void delete(String id, int position) {
		GlobalDefinitions defs = GlobalDefinitions.getInstance();
		defs.removeUserFuncDef(id);
		listener.removeUserFuncDef(position);
		getDialog().dismiss();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof PlotFuncDialogListener) {
			listener = (PlotFuncDialogListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet DialogFragment.PlotFuncDialogListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_dialog, container);
		
		Bundle args = getArguments();
		final String name = args.getString("title");
		final int position = args.getInt("position");
		
		Dialog dialog = getDialog();
		dialog.setTitle(args.getString("title"));
		
		message = (TextView) view.findViewById(R.id.function_message);
		input = (EditText) view.findViewById(R.id.function_input);
		Button confirm = (Button) view.findViewById(R.id.function_confirm);
		Button delete = (Button) view.findViewById(R.id.function_delete);
		if (position == -1) {
			delete.setEnabled(false);
		}
		
		confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				confirm();
			}
		});
		
		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				delete(name, position);
			}
		});
		
		return view;
	}
}
