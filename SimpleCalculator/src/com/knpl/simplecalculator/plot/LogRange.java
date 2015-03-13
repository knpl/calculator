package com.knpl.simplecalculator.plot;

public class LogRange extends Range {

	private static final long serialVersionUID = 3457489932408306624L;
	
	public LogRange(float min, float max) {
		super(min, max);
	}
	
	public LogRange(LogRange old) {
		this(old.min, old.max);
	}
	
	@Override
	public LogRange create(float min, float max) {
		return new LogRange(min, max);
	}
	
	@Override
	public LogRange extend(float factor) {
		float tmp = .5f * (factor - 1);
		float newmin = (float)(min * Math.pow((max / min),   - tmp));
		float newmax = (float)(min * Math.pow((max / min), 1 + tmp));
		return new LogRange(newmin, newmax);
	}
	
	@Override
	public void generate(float[] dst, int index, int step) {
		int size = dst.length / step;
		
		float c = min;
		float cs = (max-min)/(size-1);
		
		for (int i = index; i < dst.length; i += step) {
			dst[i] = c;
			c += cs;
		}
		
		viewToModel(dst, index, step);
	}
	
	@Override
	public int generateMarkers(float[] dst, int index, int stride) {
		double c;
		int n;
		
		double startorder = Math.ceil(Math.log10(min));
		double endorder = Math.floor(Math.log10(max));
		
		n = (int)Math.min(dst.length/stride, endorder - startorder + 1);
		c = Math.pow(10, startorder);
		for (int i = index; i < n*stride; i += stride) {
			dst[i] = (float)c;
			c *= 10.0;
		}
		
		return n;
	}
	
	@Override
	public float modelToView(float v) {
		return (float) (min + (max-min) * (Math.log(v/min) / Math.log(max/min)));
	}
	
	@Override
	public float viewToModel(float v) {
		return (float) (min * Math.pow((max/min), (v-min) / (max-min)));
	}
	
	public void viewToModel(float[] v, int index, int step) {
		for (int i = index; i < v.length; i += step) {
			v[i] = viewToModel(v[i]);
		}
	}
	
	@Override
	public void modelToView(float[] v) {
		for (int i = 0; i < v.length; ++i) {
			v[i] = modelToView(v[i]);
		}
	}
	
	@Override
	public void modelToView(float[] v, int index, int step) {
		for (int i = index; i < v.length; i += step) {
			v[i] = modelToView(v[i]);
		}
	}
}
