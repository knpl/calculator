package com.knpl.simplecalculator.plot;

import android.graphics.Path;
import android.graphics.RectF;

public class RegularPathGenerator implements PathGenerator {
	private static final long serialVersionUID = -124124100440099951L;
	
	public static final int DEFAULT_NSAMPLES = 256;
	
	private enum State {
		DRAW, SKIP;
	}
	
	private Mapper _evalStrategy;
	private float[] _data;
	private int _nsamples;
	
	public RegularPathGenerator(Mapper evalStrategy) {
		this(evalStrategy, DEFAULT_NSAMPLES);
	}
	
	public RegularPathGenerator(Mapper evalStrategy, int nsamples) {
		_evalStrategy = evalStrategy;
		_nsamples = nsamples;
		_data = null;
	}
	
	private float[] getData() {
		if (_data == null) {
			_data = new float[_nsamples*2];
		}
		return _data;
	}
	
	@Override
	public Path generatePath(Axis x, Axis y) {
		float[] data = getData();
		RectF window = new RectF(x.min, y.min, x.max, y.max);
		_evalStrategy.map(data, x, y);
		x.modelToView(data, 0, 2);
		y.modelToView(data, 1, 2);
		return trace(window);
	}
	
	private static boolean ok(float x, float y) {
		return !(Float.isNaN(x) || Float.isNaN(y) ||
				 Float.isInfinite(x) || Float.isInfinite(y));
	}
	
	private Path trace(RectF window) {
		Path path = new Path();
		
		State state = State.SKIP;
		if (ok(_data[0], _data[1]) && window.contains(_data[0], _data[1])) {
			path.moveTo(_data[0], _data[1]);
			state = State.DRAW;
		}
		
		for (int i = 2; i < _data.length; i+=2) {
			
			switch (state) {
			
			case SKIP:
				
				if (ok(_data[i], _data[i+1]) && window.contains(_data[i], _data[i+1])) {
					if (_data[i-1] == Float.POSITIVE_INFINITY) {
						path.moveTo(_data[i], window.bottom);
						path.lineTo(_data[i], _data[i+1]);
					}
					else if (_data[i-1] == Float.NEGATIVE_INFINITY) {
						path.moveTo(_data[i], window.top);
						path.lineTo(_data[i], _data[i+1]);
					}
					else if (Float.isNaN(_data[i-1])) {
						path.moveTo(_data[i], _data[i+1]);
					}
					else {
						path.moveTo(_data[i], _data[i+1]);
					}
					state = State.DRAW;
				}
				break;
				
			case DRAW:
				
				if (ok(_data[i], _data[i+1])) {
					if (!window.contains(_data[i], _data[i+1])) {
						state = State.SKIP;
					}
					else {
						path.lineTo(_data[i], _data[i+1]);
					}
				}
				else {
					state = State.SKIP;
				}
				break;
				
			default:
				return path;
			}
		}
		
		return path;
	}
}
