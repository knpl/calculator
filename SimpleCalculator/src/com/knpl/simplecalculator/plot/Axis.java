package com.knpl.simplecalculator.plot;

import java.io.Serializable;

import com.knpl.simplecalculator.util.Pair;

import android.graphics.Matrix;

public class Axis implements Serializable {
	private static final long serialVersionUID = 4451726311629241145L;
	
	public final float min,
		  		 	   max;
	
	public Axis(float min, float max) {
		this.min = min;
		this.max = max;
	}
	
	public Axis(Axis old) {
		this(old.min, old.max);
	}
	
	public Axis create(float min, float max) {
		return new Axis(min, max);
	}
	
	public boolean contains(float x) {
		return min <= x && x <= max;
	}
	
	public Axis extend(float factor) {
		float tmp = .5f * (factor - 1) * (max-min);
		return new Axis(min - tmp, max + tmp);
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
	
	public int generateMarkers(float[] dst, int index, int stride) {
		double step, begin, c;
		int n;
		
		step = Math.pow(10, Math.floor(Math.log10(max-min)));
		c = Math.ceil(min/step);
		n = (int)(Math.floor(max/step) - c) + 1;
		if (n == 1) {
			step /= 5;
			c = Math.ceil(min/step);
			n = (int)(Math.floor(max/step) - c) + 1;
		}
		else if (2 <= n && n <= 4) {
			step /= 2;
			c = Math.ceil(min/step);
			n = (int)(Math.floor(max/step) - c) + 1;
		}
		
		begin = c*step;
		
		c = begin;
		n = (int)Math.min(dst.length/stride, n);
		for (int i = index; i < n*stride; i += stride) {
			dst[i] = (float)c;
			c += step;
		}
		
		return n;
	}
	
	public float modelToView(float v) {
		return v;
	}

	public void modelToView(float[] v) {
	}
	
	public void modelToView(float[] v, int index, int step) {
	}
	
	public float len() {
		return max-min;
	}
	
	public static void modelToView(float[] v, Axis x, Axis y) {
		x.modelToView(v, 0, 2);
		y.modelToView(v, 1, 2);
	}
	
	@Override 
	public String toString() {
		return "["+min+", "+max+"]";
	}
	
	public static Pair<Axis, Axis> map(Axis x, Axis y, Matrix ctm) {
		float[] pts = new float[] {x.min, y.min, x.max, y.max};
		ctm.mapPoints(pts);
		return new Pair<Axis, Axis>(
					new Axis(pts[0], pts[2]), new Axis(pts[3], pts[1]));
	}

	public float viewToModel(float v) {
		return v;
	}
}
