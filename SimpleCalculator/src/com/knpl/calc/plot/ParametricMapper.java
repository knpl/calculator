package com.knpl.calc.plot;

import com.knpl.calc.util.Program;

import android.graphics.Matrix;
import android.graphics.Path;

public class ParametricMapper implements Mapper {
	
	private static final long serialVersionUID = 8430429963387594160L;
	
	public static final int DEFAULT_SAMPLE_COUNT = 256;
	
	private float[] buffer;
	
	private final Program xprogram,
						  yprogram;
	private final Range trange;
	private final int sampleCount,
					  color;
	
	public ParametricMapper(Program xprogram, Program yprogram, Range trange, int sampleCount, int color) {
		this.xprogram = xprogram;
		this.yprogram = yprogram;
		this.trange = trange;
		this.sampleCount = sampleCount;
		this.color = color;
		buffer = null;
	}

	public ParametricMapper(Program xprogram, Program yprogram, Range trange, int color) {
		this(xprogram, yprogram, trange, DEFAULT_SAMPLE_COUNT, color);
	}
	
	@Override
	public void reset() {
		buffer = null;
	}
	
	@Override
	public int getColor() {
		return color;
	}
	
	@Override
	public Path map(Matrix ctm, Range xrange, Range yrange) {
		if (buffer == null) {
			buffer = new float[2 * sampleCount];
			trange.generate(buffer, 0, 2);
			trange.generate(buffer, 1, 2);
			xprogram.evaluate(buffer, 0, 2, buffer, 0, 2);
			yprogram.evaluate(buffer, 1, 2, buffer, 1, 2);
		}
		
		Path path = MapperUtils.pathInWindow(new Path(), buffer, buffer.length, 
						xrange.min, xrange.max, yrange.min, yrange.max);
		path.transform(ctm);
		return path;
	}
}