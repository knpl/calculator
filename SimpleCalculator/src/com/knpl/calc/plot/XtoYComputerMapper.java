package com.knpl.calc.plot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.knpl.calc.util.PlotComputer;

import android.graphics.Matrix;
import android.graphics.Path;
import android.opengl.GLES20;

public class XtoYComputerMapper implements Mapper {
	
	public interface MapperListener {
		void update(float[] buffer, int id);
	}
	private MapperListener listener;
	private int id;
	
	private static final long serialVersionUID = 2805742976654101121L;
	
	private static final int DEFAULT_SAMPLE_COUNT = 1024;
	private static final int SIZEOF_FLOAT = 4;
	private static final int FLOATS_SAMPLE = 2;
	
	int color;
	
	private final PlotComputer computer;
	private final int sampleCount;
	
	private float[] buffer;
	private Range bufRange;
	
	private boolean initialized;

	public XtoYComputerMapper(PlotComputer computer, int color, int sampleCount) {
		this.computer = computer;
		this.color = color;
		this.sampleCount = sampleCount;
		reset();
	}
	
	public XtoYComputerMapper(PlotComputer computer, int color) {
		this(computer, color, DEFAULT_SAMPLE_COUNT);
	}
	
	@Override
	public void reset() {
		listener = null;
		id = -1;
		buffer = null;
		bufRange = null;
		initialized = false;
	}
	
	public void initialize(Range xrange) {
		final double len = xrange.len();
		// screen is the first power of two larger than the screenwidth;
		double screen = Math.pow(2, Math.ceil(Math.log(len)/Math.log(2)));
		// bufStart is one screen before the screen that contains the window edge
		double bufStart = xrange.viewToModel(screen * (Math.floor(xrange.min / screen) - 1));
		double bufEnd   = xrange.viewToModel(bufStart + 4 * screen);
		
		android.util.Log.d("mytag", "start: "+bufStart+" bufEnd: "+bufEnd);
		
		buffer = new float[sampleCount * FLOATS_SAMPLE];
		
		// Fill first buffer;
		bufRange = xrange.create(bufStart, bufEnd);
		bufRange.generate(buffer, 0, FLOATS_SAMPLE);
		computer.execute(buffer, 1, FLOATS_SAMPLE, buffer, 0, FLOATS_SAMPLE);
		
		initialized = true;
	}
	
	public void setListener(MapperListener listener, int id) {
		this.listener = listener;
		this.id = id;
	}
	
	public void currentView(Range xrange, Range yrange) {
		if (listener == null) {
			return;
		}
		
	}

	@Override
	public Path map(Matrix ctm, Range xrange, Range yrange) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getColor() {
		return color;
	}
}
