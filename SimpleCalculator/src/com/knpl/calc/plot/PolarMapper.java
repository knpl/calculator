package com.knpl.calc.plot;

import com.knpl.calc.util.Program;

import android.graphics.Matrix;
import android.graphics.Path;

public class PolarMapper implements Mapper {
	private static final long serialVersionUID = -2678735095392079846L;
	
	private static final int DEFAULT_SAMPLE_COUNT = 360;

	private final Program program;
	private final Range trange;
	private final int sampleCount;
	private final int color;
	
	private float[] buffer;
	
	public PolarMapper(Program program, Range trange, int sampleCount, int color) {
		this.program = program;
		this.trange = trange;
		this.sampleCount = sampleCount;
		this.color = color;
		buffer = null;
	}
	
	public PolarMapper(Program program, Range trange, int color) {
		this(program, trange, DEFAULT_SAMPLE_COUNT, color);
	}
	
	@Override
	public Path map(Matrix ctm, Range xrange, Range yrange) {
		if (buffer == null) {
			buffer = new float[2 * sampleCount];
			trange.generate(buffer, 0, 2);
			program.evaluate(buffer, 1, 2, buffer, 0, 2);
			polarToCartesian(buffer);
		}
		
		Path path = MapperUtils.pathInWindow(new Path(), buffer, buffer.length, 
						xrange.min, xrange.max, yrange.min, yrange.max);
		path.transform(ctm);
		return path;
	}
	
	private static void polarToCartesian(float[] src) {
		float theta, r, x, y;
		for (int i = 0; i + 1 < src.length; i += 2) {
			theta = src[i]; r = src[i+1];
			x = (float) (r * Math.cos(theta)); y = (float) (r * Math.sin(theta));
			src[i] = x; src[i+1] = y;
		}
	}

	@Override
	public void reset() {
		buffer = null;
	}

	@Override
	public int getColor() {
		return color;
	}
}
