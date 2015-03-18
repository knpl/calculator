package com.knpl.simplecalculator;

import java.util.ArrayList;

import com.knpl.simplecalculator.util.FuncDef;
import com.knpl.simplecalculator.util.Globals;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
		super.onListItemClick(l, v, position, id);
	}
}
