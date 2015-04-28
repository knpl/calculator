package com.knpl.calc.plot;

import android.graphics.Path;

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
	
	@Override
	public void modelToView(float[] v, int stop, int index, int step) {
		for (int i = index; i < stop; i += step) {
			v[i] = modelToView(v[i]);
		}
	}
	
	@Override
	public MarkerInfo getMarkerInfo() {
		double step = modelToView(10) - modelToView(1);
		int startn = (int) Math.floor(Math.log10(min));
		int stopn  = (int) Math.ceil (Math.log10(max));
		float start = modelToView((float)Math.pow(10, startn));
		return new MarkerInfo(getMarkerModel(), start, (float)step, stopn - startn);
	}
	
	private static float[] markerPositions = new float[] {
		0f,
		(float)Math.log10(2),
		(float)Math.log10(3),
		(float)Math.log10(4),
		(float)Math.log10(5),
		(float)Math.log10(6),
		(float)Math.log10(7),
		(float)Math.log10(8),
		(float)Math.log10(9),
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
			case 4:  len = 2; break;
			default: len = 1;
			}
			p.moveTo(markerPositions[i], -len/2);
			p.rLineTo(0, len);
		}
		
		markerModel = p;
		return new Path(p);
	}
}
