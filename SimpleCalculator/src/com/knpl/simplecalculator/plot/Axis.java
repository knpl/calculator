package com.knpl.simplecalculator.plot;

import java.io.Serializable;

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
}
