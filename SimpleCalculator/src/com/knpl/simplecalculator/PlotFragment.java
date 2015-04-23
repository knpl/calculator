package com.knpl.simplecalculator;

import java.util.ArrayList;
import java.util.List;

import com.knpl.simplecalculator.plot.Range;
import com.knpl.simplecalculator.plot.Mapper;
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
	
	@Override
	public void onDestroyView() {
		Bundle args = getArguments();
		
		@SuppressWarnings("unchecked")
		final List<Mapper> paths = (List<Mapper>) args.getSerializable("paths");
		for (Mapper path : paths) {
			path.reset();
		}
		
		super.onDestroyView();
	}

	public static PlotFragment createPlotFragment(ArrayList<Mapper> mappers, Range x, Range y) {
		PlotFragment fragment = new PlotFragment();
		Bundle args = new Bundle();
		
		args.putSerializable("paths", mappers);
		args.putSerializable("xaxis", x);
		args.putSerializable("yaxis", y);
		fragment.setArguments(args);
		
		return fragment;
	}
	
	public PlotView initPlotFragment(PlotView v) {
		Bundle args = getArguments();
		
		@SuppressWarnings("unchecked")
		final List<Mapper> paths = (List<Mapper>) args.getSerializable("paths");	
		final Range x = (Range) args.getSerializable("xaxis");
		final Range y = (Range) args.getSerializable("yaxis");
		
		return v.init(paths, x, y);
	}
}
