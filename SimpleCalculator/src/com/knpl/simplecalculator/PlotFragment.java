package com.knpl.simplecalculator;

import java.util.ArrayList;
import java.util.List;

import com.knpl.simplecalculator.plot.Axis;
import com.knpl.simplecalculator.plot.PathGenerator;
import com.knpl.simplecalculator.plot.PlotView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlotFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 PlotView v = (PlotView) inflater.inflate(R.layout.fragment_plot, container, false); 
		 return initPlotFragment(v);
	}
	
	public static PlotFragment createPlotFragment(ArrayList<PathGenerator> paths, Axis x, Axis y) {
		PlotFragment fragment = new PlotFragment();
		Bundle args = new Bundle();
		
		args.putSerializable("paths", paths);
		args.putSerializable("xaxis", x);
		args.putSerializable("yaxis", y);
		fragment.setArguments(args);
		
		return fragment;
	}
	
	public PlotView initPlotFragment(PlotView v) {
		Bundle args = getArguments();
		
		@SuppressWarnings("unchecked")
		final List<PathGenerator> paths = (ArrayList<PathGenerator>) args.getSerializable("paths");
		final Axis x = (Axis) args.getSerializable("xaxis");
		final Axis y = (Axis) args.getSerializable("yaxis");
		
		return v.init(paths, x, y);
	}
}
