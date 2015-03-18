package com.knpl.simplecalculator;

import java.util.ArrayList;

import com.knpl.simplecalculator.nodes.ConstDef;
import com.knpl.simplecalculator.util.Globals;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ConstDefFragment extends ListFragment {
	private static ArrayList<ConstDef> constdefs = new ArrayList<ConstDef>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		
		Globals defs = Globals.getInstance();
		constdefs = new ArrayList<ConstDef>(defs.getConstDefMap().values());
		
		ArrayAdapter<ConstDef> adapter= 
				new ArrayAdapter<ConstDef>(getActivity(), 
						android.R.layout.simple_list_item_1, constdefs);
		setListAdapter(adapter);
		
		return inflater.inflate(R.layout.fragment_funcdef, container, false);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {	
		super.onListItemClick(l, v, position, id);
	}
}
