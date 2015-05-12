package com.knpl.calc.plot;

import java.io.Serializable;

import android.graphics.Path;

public class Range implements Serializable {
	private static final long serialVersionUID = 4451726311629241145L;
	
	public final double min,
		  		 	    max;
	
	public Range(double min, double max) {
		this.min = min;
		this.max = max;
	}
	
	public Range(Range old) {
		this(old.min, old.max);
	}
	
	public Range create(double min, double max) {
		return new Range(min, max);
	}
	
	public boolean contains(double x) {
		return min <= x && x <= max;
	}
	
	public boolean contains(float x) {
		return min <= x && x <= max;
	}

	public double len() {
		return max-min;
	}

	public float modelToView(float v) {
		return v;
	}
	
	public double modelToView(double v) {
		return v;
	}
	
	public float viewToModel(float v) {
		return v;
	}
	
	public double viewToModel(double v) {
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
	
	public Range extend(double factor) {
		double tmp = .5 * (factor - 1) * (max - min);
		return new Range(min - tmp, max + tmp);
	}
	
	public void generate(float[] dst, int index, int step) {
		int size = dst.length / step;
		
		double c = min;
		double cs = (max - min)/(size - 1);
		
		for (int i = index; i < dst.length; i += step) {
			dst[i] = (float)c;
			c += cs;
		}
	}
	
	private static float[] halfMarkerPositions = new float[] {
		0f, .2f, .4f, .6f, .8f
	};
	
	private static Path halfMarkerModel = null;
	
	public Path getHalfMarkerModel() {
		if (halfMarkerModel != null) {
			return new Path(halfMarkerModel);
		}
		
		Path p = new Path();
		for (int i = 0; i < halfMarkerPositions.length; i++) {
			float len = (i == 0) ? 4 : 1;
			p.moveTo(halfMarkerPositions[i], -len/2);
			p.rLineTo(0, len);
		}
		
		halfMarkerModel = p;
		return new Path(p);
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
	
	public MarkerInfo getMarkerInfo() {
		double step = Math.pow(10, Math.floor(Math.log10(max-min)));
		int firstn = (int) Math.floor(min / step);
		int lastn = (int) Math.ceil(max / step);
		int nsteps = lastn - firstn;
		
		Path model;
		if (nsteps <= 2) {
			step /= 2;
			firstn = (int) Math.floor(min / step);
			lastn = (int) Math.ceil(max / step);
			nsteps = lastn - firstn;
			model = getHalfMarkerModel();
		}
		else {
			model = getMarkerModel();
		}
		return new MarkerInfo(model, (float)(step*firstn), (float)step, nsteps);
	}
	
	public static class MarkerInfo {
		public final Path model;
		public final float start,
						   step;
		public final int nsteps;
		
		public MarkerInfo(Path model, float start, float step, int nsteps) {
			this.model = model;
			this.start = start;
			this.step = step;
			this.nsteps = nsteps;
		}
	}
	
	@Override 
	public String toString() {
		return "["+min+", "+max+"]";
	}	
}
