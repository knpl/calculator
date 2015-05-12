package com.knpl.calc;

import java.util.ArrayList;
import java.util.List;

import com.knpl.calc.plot.Mapper;
import com.knpl.calc.plot.PlotGLSurfaceView;
import com.knpl.calc.plot.Range;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GLPlotFragment extends Fragment {
	
	private PlotGLSurfaceView surface;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		surface = new PlotGLSurfaceView(getActivity());
		Bundle args = getArguments();
		
		@SuppressWarnings("unchecked")
		final List<Mapper> paths = (List<Mapper>) args.getSerializable("paths");	
		final Range x = (Range) args.getSerializable("xaxis");
		final Range y = (Range) args.getSerializable("yaxis");
		surface.setValues(paths, x, y);
		
		return surface;
	}
	
	@Override
	public void onPause() {
		surface.onPause();
		super.onPause();
	}
	
	@Override
	public void onResume() {
		surface.onResume();
		super.onResume();
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

	public static GLPlotFragment createGLPlotFragment(ArrayList<Mapper> mappers, Range x, Range y) {
		GLPlotFragment fragment = new GLPlotFragment();
		Bundle args = new Bundle();
		
		args.putSerializable("paths", mappers);
		args.putSerializable("xaxis", x);
		args.putSerializable("yaxis", y);
		fragment.setArguments(args);
		
		return fragment;
	}
}
