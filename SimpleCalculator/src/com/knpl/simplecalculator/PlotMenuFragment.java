package com.knpl.simplecalculator;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.knpl.simplecalculator.PlotEntryDialog.PlotEntryDialogListener;
import com.knpl.simplecalculator.plot.Mapper;
import com.knpl.simplecalculator.plot.ProgramXtoYMapper;
import com.knpl.simplecalculator.util.Pair;
import com.knpl.simplecalculator.util.Program;
import com.knpl.simplecalculator.util.UserFuncDef;

public class PlotMenuFragment extends ListFragment implements PlotEntryDialogListener {
	
	public static class PlotEntry {
		private final UserFuncDef userFuncDef;
		private final String description;
		private final int color;
		
		public PlotEntry(UserFuncDef userFuncDef, String description, int color) {
			this.userFuncDef = userFuncDef;
			this.description = description;
			this.color = color;
		}
		
		public UserFuncDef getUserFuncDef() {
			return userFuncDef;
		}
		
		public String getDescription() {
			return description;
		}
		
		public int getColor() {
			return color;
		}
		
		@Override
		public String toString() {
			return userFuncDef.getSignature().toString() + " = " + description;
		}
	}
	
	private static ArrayList<PlotEntry> plotEntries = new ArrayList<PlotEntry>();
	
	private PlotListener listener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		
		PlotEntryAdapter adapter = new PlotEntryAdapter(getActivity(), plotEntries);
		setListAdapter(adapter);
		
		return inflater.inflate(R.layout.fragment_plotmenu, container, false);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {	
		PlotEntry entry = (PlotEntry) getListAdapter().getItem(position);
		showDialog(entry, position);
		
		super.onListItemClick(l, v, position, id);
	}
	
	private void showDialog() {
		PlotEntryDialog dialog = PlotEntryDialog.createInstance();
        dialog.show(getChildFragmentManager(), null);
	}
	
	private void showDialog(PlotEntry entry, int position) {
		PlotEntryDialog dialog = 
				PlotEntryDialog.createInstance(entry, position);
        dialog.show(getChildFragmentManager(), null);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.plotmenu_actions, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.action_plot:
			compileAndPlot();
			break;
			
		case R.id.action_new_function:
			showDialog();
			break;
			
		default:
			;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof PlotListener) {
			listener = (PlotListener) activity;
			setListAdapter(new PlotEntryAdapter(activity, plotEntries));
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet OptionsFragment.PlotListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}
	
	public interface PlotListener {
		void plot(ArrayList<Pair<Mapper, Integer>> mappers);
	}

	private void compileAndPlot() {
		ArrayList<Pair<Mapper, Integer>> mappers = new ArrayList<Pair<Mapper, Integer>>();
		Program p;
		for (PlotEntry entry : plotEntries) {
			try {
				p = entry.getUserFuncDef().getProgram();
				mappers.add(new Pair<Mapper, Integer>(
						new ProgramXtoYMapper(p), entry.getColor()));
			}
			catch (Exception e) {
	    		e.printStackTrace();
			}
		}
		listener.plot(mappers);
	}

	@Override
	public void addPlotEntry(PlotEntry entry) {
		plotEntries.add(entry);
		((PlotEntryAdapter) getListAdapter()).notifyDataSetChanged();
	}
	
	@Override
	public void setPlotEntry(int position, PlotEntry entry) {
		plotEntries.set(position, entry);
		((PlotEntryAdapter) getListAdapter()).notifyDataSetChanged();
	}

	@Override
	public void removePlotEntry(int position) {
		plotEntries.remove(position);
		((PlotEntryAdapter) getListAdapter()).notifyDataSetChanged();
	}
}


