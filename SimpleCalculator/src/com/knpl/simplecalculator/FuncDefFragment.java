package com.knpl.simplecalculator;

import java.util.ArrayList;

import com.knpl.simplecalculator.PlotMenuFragment.PlotEntry;
import com.knpl.simplecalculator.util.GlobalDefinitions;
import com.knpl.simplecalculator.util.UserFuncDef;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FuncDefFragment extends ListFragment {
	
	private static ArrayList<UserFuncDef> funcdefs = new ArrayList<UserFuncDef>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		
		GlobalDefinitions defs = GlobalDefinitions.getInstance();
		funcdefs = new ArrayList<UserFuncDef>(defs.getUserFuncDefs());
		
		ArrayAdapter<UserFuncDef> adapter= 
				new ArrayAdapter<UserFuncDef>(getActivity(), 
						android.R.layout.simple_list_item_1, funcdefs);
		setListAdapter(adapter);
		
		return inflater.inflate(R.layout.fragment_funcdef, container, false);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {	
//		PlotEntry entry = (PlotEntry) getListAdapter().getItem(position);
//		showDialog(entry, position);
//		
		super.onListItemClick(l, v, position, id);
	}
}
