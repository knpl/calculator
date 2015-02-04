package com.knpl.simplecalculator;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UserFuncDefAdapter extends ArrayAdapter<UserFuncDef> {

	private final Context context;
	private final List<UserFuncDef> ufdlist;
	
	public UserFuncDefAdapter(Context context, List<UserFuncDef> ufdlist) {
		super(context, R.layout.row_layout, ufdlist);
		this.context = context;
		this.ufdlist = ufdlist;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.row_layout, parent, false);
		TextView tv = (TextView) view.findViewById(R.id.name);
		tv.setText(""+ufdlist.get(position).getSignature());
		return view;
	}
}
