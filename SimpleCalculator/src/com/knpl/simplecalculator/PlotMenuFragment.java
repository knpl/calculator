package com.knpl.simplecalculator;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.knpl.simplecalculator.PlotFuncDialog.PlotFuncDialogListener;
import com.knpl.simplecalculator.plot.PathGenerator;
import com.knpl.simplecalculator.plot.ProgramXtoYMapper;
import com.knpl.simplecalculator.plot.RegularPathGenerator;

public class PlotMenuFragment extends ListFragment implements PlotFuncDialogListener {
	
	private static ArrayList<UserFuncDef> ufds;
	
	private PlotListener listener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		
		GlobalDefinitions defs = GlobalDefinitions.getInstance();
		ufds = new ArrayList<UserFuncDef>(defs.getUserFuncDefs());
		
		UserFuncDefAdapter adapter = new UserFuncDefAdapter(getActivity(), ufds);
		setListAdapter(adapter);
		
		return inflater.inflate(R.layout.fragment_plotmenu, container, false);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {	
		UserFuncDef ufd = (UserFuncDef) getListAdapter().getItem(position);
		showDialog(ufd, position);
		
		super.onListItemClick(l, v, position, id);
	}
	
	
	
	private void showDialog() {
		Bundle args = new Bundle();
		args.putString("title", "new function");
		args.putInt("position", -1);
		FragmentManager fm = getChildFragmentManager();
        PlotFuncDialog dialog = new PlotFuncDialog();
        dialog.setArguments(args);
        dialog.show(fm, null);
	}
	
	private void showDialog(UserFuncDef ufd, int position) {
		Bundle args = new Bundle();
		args.putString("title", ""+ufd.getSignature().getName());
		args.putInt("position", position);
		FragmentManager fm = getChildFragmentManager();
        PlotFuncDialog dialog = new PlotFuncDialog();
        dialog.setArguments(args);
        dialog.show(fm, null);
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
			compile_and_plot();
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
			setListAdapter(new UserFuncDefAdapter(activity, ufds));
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
		void plot(ArrayList<PathGenerator> pglist);
	}
	

	private void compile_and_plot() {
		ArrayList<PathGenerator> pglist = new ArrayList<PathGenerator>();
		Program p;
		for (UserFuncDef ufd : ufds) {
			try {
				p = ufd.getProgram();
				pglist.add(new RegularPathGenerator(new ProgramXtoYMapper(p)));
			}
			catch (Exception e) {
	    		e.printStackTrace();
			}
		}
		listener.plot(pglist);
	}

	@Override
	public void addUserFuncDef(UserFuncDef ufd) {
		ufds.add(ufd);
		UserFuncDefAdapter adapter = (UserFuncDefAdapter) getListAdapter();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void removeUserFuncDef(int position) {
		ufds.remove(position);
		UserFuncDefAdapter adapter = (UserFuncDefAdapter) getListAdapter();
		adapter.notifyDataSetChanged();
	}
	
}


