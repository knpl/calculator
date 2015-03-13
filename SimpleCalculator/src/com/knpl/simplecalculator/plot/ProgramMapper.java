package com.knpl.simplecalculator.plot;

import android.graphics.Matrix;
import android.graphics.Path;
import com.knpl.simplecalculator.util.Program;

public class ProgramMapper implements Mapper {

	private static final long serialVersionUID = 2595871216194630358L;
	
	public static final int DEFAULT_BUFSIZE = 256;
	public static final int NBUFFERS = 3;
	
	private Program program;
	
	private Range range;
	private float[][] buffers;
	private int bufferIndex;
	private boolean initialized;
	
	public ProgramMapper(Program program) {
		this.program = program;
		this.range = null;
		this.buffers = null;
		this.bufferIndex = 1;
		this.initialized = false;
	}
	
	public void initialize(int buffersize, Range axis) {
		this.range = axis;
		this.buffers = new float[NBUFFERS][2*buffersize];
		this.bufferIndex = 1;
		
		for (int i = 0; i < buffers.length; ++i) {
			float len = axis.len();
			float lb = axis.min  + (i - bufferIndex) * len;
			float ub = lb + len;
			Range a = axis.create(axis.viewToModel(lb), axis.viewToModel(ub));
			a.generate(buffers[i], 0, 2);
		}
		
		for (int i = 0; i < buffers.length; ++i) {
			program.evaluate(buffers[i], 1, 2, buffers[i], 0, 2);
		}
		
		this.initialized = true;
	}
	
	@Override
	public Path map(Matrix ctm, Range xrange, Range yrange) {
		if (!initialized) {
			initialize(DEFAULT_BUFSIZE, xrange);
		}
		
		float ratio = xrange.len()/range.len();
		if (4f/3f < ratio || ratio < 3f/4f) {
			initialize(DEFAULT_BUFSIZE, xrange);
		}
		else if (range.viewToModel(xrange.min) < range.viewToModel(range.min - 0.5f*range.len())) {
			goLeft();
		}
		else if (range.viewToModel(xrange.min) > range.viewToModel(range.min + 0.5f*range.len())) {
			goRight();
		}
		
		Path path = makePath(xrange, yrange);
		path.transform(ctm);
		return path;
	}

	public void goLeft() {
		bufferIndex = (bufferIndex + (NBUFFERS - 1)) % NBUFFERS;
		
		float from = range.viewToModel(range.min - range.len()),
			  to   = range.min;
		range = range.create(from, to);
		
		float[] leftBuf = buffers[(bufferIndex + (NBUFFERS - 1)) % NBUFFERS];
		from = range.viewToModel(range.min - range.len());
		to   = range.min;
		Range leftRange = range.create(from, to);
		
		leftRange.generate(leftBuf, 0, 2);
		program.evaluate(leftBuf, 1, 2, leftBuf, 0, 2);
	}
	
	public void goRight() {
		bufferIndex = (bufferIndex + 1) % NBUFFERS;
		
		float from = range.max,
			  to   = range.viewToModel(range.max + range.len());
		range = range.create(from, to);
		
		float[] rightBuf = buffers[(bufferIndex + 1) % NBUFFERS];
		from = range.max;
		to   = range.viewToModel(range.max + range.len());
		Range rightRange = range.create(from, to);
		
		rightRange.generate(rightBuf, 0, 2);
		program.evaluate(rightBuf, 1, 2, rightBuf, 0, 2);
	}
	
	public Path makePath(Range xrange, Range yrange) {
		final float rangeLen = range.len();
		final int npoints = buffers[0].length / 2;
		
		float diff = (xrange.min - range.min)/rangeLen;
		int integer = (int)Math.floor(diff);
		float fraction  = diff - integer;
		
		int firstBuffer = (bufferIndex + integer + NBUFFERS) % NBUFFERS;
		int firstPoint = (int) Math.floor(fraction * npoints);
		if (firstPoint < 0)
			firstPoint = 0;
		else if (firstPoint >= npoints)
			firstPoint = npoints - 1;
		
		diff = (xrange.max - range.min)/rangeLen;
		integer = (int)Math.floor(diff);
		fraction = diff - integer;
		
		int lastBuffer = (bufferIndex + integer + NBUFFERS) % NBUFFERS;
		int lastPoint = (int) Math.ceil(fraction * npoints);
		if (lastPoint < 0)
			lastPoint = 0;
		else if (lastPoint >= npoints)
			lastPoint = npoints - 1;
		
		return buildPath(firstBuffer, firstPoint*2, lastBuffer, lastPoint*2, yrange);
	}

	private static final int DRAW = 0,
							 SKIP = 1;
	
	public Path buildPath(int firstBuffer, int firstIndex, int lastBuffer, int lastIndex, Range yrange) {
		final int buflen = buffers[0].length;
		int n = (lastBuffer - firstBuffer + NBUFFERS) % NBUFFERS;
		float[] buf;
		Path path = new Path();
		float x, y, lastx = 0, lasty = 0;
		int state = SKIP;
		
		for (int j = 0; j <= n; ++j) {
			buf = buffers[(firstBuffer + j) % NBUFFERS];
			
			int start = 0;
			int stop  = (j == n) ? lastIndex  : buflen - 2;
			
			if (j == 0) {
				start = firstIndex;
				state = SKIP;
				x = buf[start]; y = buf[start+1];
				if (ok(y) && yrange.contains(y)) {
					path.moveTo(x, y);
					state = DRAW;
				}
				lastx = x; lasty = y;
				start += 2;
			}
			
			for (int i = start; i <= stop; i += 2) {
				x = buf[i]; y = buf[i+1];
				switch (state) {
				
				case SKIP:
					state = skip(path, x, y, lastx, lasty, yrange);
					break;
					
				case DRAW:
					state = draw(path, x, y, lastx, lasty, yrange);
					break;
					
				default:
					return path;
				}
				
				lastx = x; lasty = y;
			}
		}
		
		return path;
	}
	
	private int draw(Path path, float x, float y, float lastx, float lasty, Range yrange) {
		if (!ok(y))
			return SKIP;
		
		int state = DRAW;
		if (!yrange.contains(y)) {
			if (y > yrange.max) {
				x = getIntersectX(lastx, lasty, x, y, yrange.max);
				y = yrange.max;
			}
			else if (y < yrange.min) {
				x = getIntersectX(lastx, lasty, x, y, yrange.min);
				y = yrange.min;
			}
			state = SKIP;
		}
		path.lineTo(x, y);
		
		return state;
	}
	
	private int skip(Path path, float x, float y, float lastx, float lasty, Range yrange) {
		if (!ok(y) || !yrange.contains(y))
			return SKIP;
		
		if (Float.isNaN(lasty)) {
			path.moveTo(x, y);
		}
		else if (Float.isInfinite(lasty)) {
			path.moveTo(x, (lasty < 0) ? yrange.min : yrange.max);
		}
		else {
			if (lasty > yrange.max) {
				lastx = getIntersectX(lastx, lasty, x, y, yrange.max);
				lasty = yrange.max;
			}
			else if (lasty < yrange.min) {
				lastx = getIntersectX(lastx, lasty, x, y, yrange.min);
				lasty = yrange.min;
			}
			
			path.moveTo(lastx, lasty);
			path.lineTo(x, y);
		}
		
		return DRAW;
	}
	
	private static boolean ok(float y) {
		return !(Float.isNaN(y) || Float.isInfinite(y));
	}
	
	private float getIntersectX(float x0, float y0, float x1, float y1, float y) {
		float dx = x1 - x0,
			  dy = y1 - y0;
		return x0 + (dx/dy)*(y - y0);
	}

}
