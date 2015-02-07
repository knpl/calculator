package com.knpl.simplecalculator;

import java.util.ArrayList;
import java.util.List;

import com.knpl.simplecalculator.plot.Axis;
import com.knpl.simplecalculator.plot.Mapper;
import com.knpl.simplecalculator.plot.PlotView;
import com.knpl.simplecalculator.util.Pair;

import android.os.Bundle;
import android.os.Parcelable;
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
	
	public static PlotFragment createPlotFragment(ArrayList<Pair<Mapper, Integer>> mappers, Axis x, Axis y) {
		PlotFragment fragment = new PlotFragment();
		Bundle args = new Bundle();
		
		args.putParcelableArrayList("paths", mappers);
		args.putSerializable("xaxis", x);
		args.putSerializable("yaxis", y);
		fragment.setArguments(args);
		
		return fragment;
	}
	
	public PlotView initPlotFragment(PlotView v) {
		Bundle args = getArguments();
		
		// Avert your eyes.
		@SuppressWarnings("unchecked")
		final List<Pair<Mapper, Integer>> paths = 
				(List<Pair<Mapper, Integer>>) 
					(List<? extends Parcelable>) args.getParcelableArrayList("paths");	
		final Axis x = (Axis) args.getSerializable("xaxis");
		final Axis y = (Axis) args.getSerializable("yaxis");
		
		return v.init(paths, x, y);
	}
	
}
