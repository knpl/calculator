package com.knpl.simplecalculator.util;

import android.os.Parcel;
import android.os.Parcelable;

public class Pair<T0, T1> implements Parcelable {
	private final T0 first;
	private final T1 last;
	
	public Pair(T0 first, T1 last) {
		this.first = first;
		this.last = last;
	}
	
	@SuppressWarnings("unchecked")
	private Pair(Parcel in) {
		first = (T0)in.readValue(null);
		last = (T1)in.readValue(null);
	}
	
	public T0 getFirst() {
		return first;
	}
	
	public T1 getLast() {
		return last;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(first);
		dest.writeValue(last);
	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator<Pair> CREATOR = new Parcelable.Creator<Pair>() {

		@Override
		public Pair createFromParcel(Parcel in) {
			return new Pair(in);
		}

		@Override
		public Pair[] newArray(int size) {
			return new Pair[size];
		}
	};
}
