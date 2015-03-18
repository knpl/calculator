package com.knpl.simplecalculator;

import java.util.List;

import com.knpl.simplecalculator.PlotMenuFragment.PlotEntry;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlotEntryAdapter extends ArrayAdapter<PlotEntry> {
	
	private static class ViewHolder {
		public final TextView tv;
		public final ImageView iv;
		
		public ViewHolder(TextView tv, ImageView iv) {
			this.tv = tv;
			this.iv = iv;
		}
	}

	private final Context context;
	private final List<PlotEntry> plotEntries;
	
	public PlotEntryAdapter(Context context, List<PlotEntry> plotEntries) {
		super(context, R.layout.row_layout, plotEntries);
		this.context = context;
		this.plotEntries = plotEntries;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_layout, parent, false);
			
			ViewHolder viewHolder = new ViewHolder(
					(TextView) convertView.findViewById(R.id.name),
					(ImageView) convertView.findViewById(R.id.color));
			
			convertView.setTag(viewHolder);
		}
		
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.iv.getBackground().setColorFilter(plotEntries.get(position).color, Mode.MULTIPLY);
		viewHolder.tv.setText(""+plotEntries.get(position));
		
		return convertView;
	}
}
