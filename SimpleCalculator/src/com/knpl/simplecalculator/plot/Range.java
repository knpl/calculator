package com.knpl.simplecalculator.plot;

import java.io.Serializable;

import android.graphics.Path;

public class Range implements Serializable {
	private static final long serialVersionUID = 4451726311629241145L;
	
	public final float min,
		  		 	   max;
	
	public Range(float min, float max) {
		this.min = min;
		this.max = max;
	}
	
	public Range(Range old) {
		this(old.min, old.max);
	}
	
	public Range create(float min, float max) {
		return new Range(min, max);
	}
	
	public boolean contains(float x) {
		return min <= x && x <= max;
	}

	public float len() {
		return max-min;
	}

	public float modelToView(float v) {
		return v;
	}
	
	public float viewToModel(float v) {
		return v;
	}

	public void modelToView(float[] v) {
	}
	
	public void modelToView(float[] v, int stop, int index, int step) {
	}
	
	public void modelToView(float[] v, int index, int step) {
	}
	
	public static void modelToView(float[] v, Range x, Range y) {
		x.modelToView(v, 0, 2);
		y.modelToView(v, 1, 2);
	}
	
	public static void modelToView(float[] v, int n, Range x, Range y) {
		x.modelToView(v, n, 0, 2);
		y.modelToView(v, n, 1, 2);
	}
	
	public Range extend(float factor) {
		float tmp = .5f * (factor - 1) * (max-min);
		return new Range(min - tmp, max + tmp);
	}
	
	public void generate(float[] dst, int index, int step) {
		int size = dst.length / step;
		
		float c = min;
		float cs = (max-min)/(size-1);
		
		for (int i = index; i < dst.length; i += step) {
			dst[i] = c;
			c += cs;
		}
	}

	private static float[] markerPositions = new float[] {
		0f, .1f, .2f, .3f, .4f, .5f, .6f, .7f, .8f, .9f	
	};
	
	private static Path markerModel = null;
	
	public Path getMarkerModel() {
		if (markerModel != null) {
			return new Path(markerModel);
		}
		
		Path p = new Path();
		for (int i = 0; i < markerPositions.length; i++) {
			float len;
			switch (i) {
			case 0:  len = 4; break;
			case 5:  len = 2; break;
			default: len = 1;
			}
			p.moveTo(markerPositions[i], -len/2);
			p.rLineTo(0, len);
		}
		
		markerModel = p;
		return new Path(p);
	}
	
	public float[] getMarkerInfo() {
		float[] result = new float[2];
		double step = Math.pow(10, Math.floor(Math.log10(max-min)));
		result[0] = (float)(step*Math.floor(min/step));
		result[1] = (float)step;
		return result;
	}
	
	@Override 
	public String toString() {
		return "["+min+", "+max+"]";
	}
	
}
