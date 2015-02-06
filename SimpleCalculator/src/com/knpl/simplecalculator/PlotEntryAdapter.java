package com.knpl.simplecalculator;

import java.util.List;

import com.knpl.simplecalculator.PlotMenuFragment.PlotEntry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PlotEntryAdapter extends ArrayAdapter<PlotEntry> {

	private final Context context;
	private final List<PlotEntry> plotEntries;
	
	public PlotEntryAdapter(Context context, List<PlotEntry> plotEntries) {
		super(context, R.layout.row_layout, plotEntries);
		this.context = context;
		this.plotEntries = plotEntries;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.row_layout, parent, false);
		TextView tv = (TextView) view.findViewById(R.id.name);
		tv.setText(""+plotEntries.get(position));
		return view;
	}
}
