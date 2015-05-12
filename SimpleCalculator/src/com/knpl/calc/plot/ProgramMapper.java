package com.knpl.calc.plot;

import android.graphics.Matrix;
import android.graphics.Path;

import com.knpl.calc.util.PlotComputer;

public class ProgramMapper implements Mapper {

	private static final long serialVersionUID = 2595871216194630358L;
	
	public static final int DEFAULT_BUFSIZE = 256;
	public static final int NBUFFERS = 3;
	
	private final PlotComputer computer;
	
	private Range range;
	private float[][] buffers;
	private float[] scratch;
	private int scratchBound;
	private int bufferIndex;
	private int color;
	private boolean initialized;
	
	private Path path;
	
	public ProgramMapper(PlotComputer computer, int color) {
		this.computer = computer;
		this.color = color;
		
		reset();
	}

	@Override
	public void reset() {
		range = null;
		buffers = null;
		scratch = null;
		scratchBound = 0;
		bufferIndex = 1;
		initialized = false;
		path = null;
	}
	
	@Override
	public int getColor() {
		return color;
	}
	
	public void initialize(int buffersize, Range range) {
		this.range = range;
		buffers = new float[NBUFFERS][2 * buffersize];
		scratch = new float[4 * buffersize];
		scratchBound = 0;
		bufferIndex = 1;
		
		for (int i = 0; i < buffers.length; ++i) {
			double len =  range.len();
			double lb = (range.min  + (i - bufferIndex) * len);
			double ub = lb + len;
			Range a = range.create(range.viewToModel(lb), range.viewToModel(ub));
			a.generate(buffers[i], 0, 2);
		}
		
		for (int i = 0; i < buffers.length; ++i) {
			computer.execute(buffers[i], 1, 2, buffers[i], 0, 2);
		}
		
		path = new Path();
		
		initialized = true;
	}
	
	
	public void goLeft() {
		bufferIndex = (bufferIndex + (NBUFFERS - 1)) % NBUFFERS;
		
		double from = range.viewToModel(range.min - range.len()),
			   to   = range.min;
		range = range.create(from, to);
		
		float[] leftBuf = buffers[(bufferIndex + (NBUFFERS - 1)) % NBUFFERS];
		from = range.viewToModel(range.min - range.len());
		to   = range.min;
		Range leftRange = range.create(from, to);
		
		leftRange.generate(leftBuf, 0, 2);
		computer.execute(leftBuf, 1, 2, leftBuf, 0, 2);
	}
	
	public void goRight() {
		bufferIndex = (bufferIndex + 1) % NBUFFERS;
		
		double from = range.max,
			   to   = range.viewToModel(range.max + range.len());
		range = range.create(from, to);
		
		float[] rightBuf = buffers[(bufferIndex + 1) % NBUFFERS];
		from = range.max;
		to   = range.viewToModel(range.max + range.len());
		Range rightRange = range.create(from, to);
		
		rightRange.generate(rightBuf, 0, 2);
		computer.execute(rightBuf, 1, 2, rightBuf, 0, 2);
	}
	
	@Override
	public Path map(Matrix ctm, Range xrange, Range yrange) {
		if (!initialized) {
			initialize(DEFAULT_BUFSIZE, xrange);
		}
		
		double ratio = (range.modelToView(xrange.max) - range.modelToView(xrange.min))/range.len();
		if (ratio > 3/2f) {
			double max = range.viewToModel(range.min + range.len()*2);
			initialize(DEFAULT_BUFSIZE, range.create(range.min, max));
		}
		else if (ratio < 2/3f) {
			double max = range.viewToModel(range.min + range.len()/2);
			initialize(DEFAULT_BUFSIZE, range.create(range.min, max));
		}
		else if (xrange.min < range.viewToModel(range.min - 0.5f*range.len())) {
			goLeft();
		}
		else if (xrange.min > range.viewToModel(range.min + 0.5f*range.len())) {
			goRight();
		}
		
		scratch(xrange);
		xrange.modelToView(scratch, scratchBound, 0, 2);
		yrange.modelToView(scratch, scratchBound, 1, 2);
		path.rewind();
		MapperUtils.pathInRangeY(path, scratch, scratchBound, (float)yrange.min, (float)yrange.max);
		path.transform(ctm);
		return path;
	}
	
	public void scratch(Range xrange) {
		final double rangeLen = range.len();
		final int npoints = buffers[0].length / 2;
		
		double diff = (range.modelToView(xrange.min) - range.min)/rangeLen;
		int integer = (int)Math.floor(diff);
		double fraction  = diff - integer;
		
		int firstBuffer = (bufferIndex + integer + NBUFFERS) % NBUFFERS;
		int firstPoint = (int) Math.floor(fraction * npoints);
		if (firstPoint < 0)
			firstPoint = 0;
		else if (firstPoint >= npoints)
			firstPoint = npoints - 1;
		
		diff = (range.modelToView(xrange.max) - range.min)/rangeLen;
		integer = (int)Math.floor(diff);
		fraction = diff - integer;
		
		int lastBuffer = (bufferIndex + integer + NBUFFERS) % NBUFFERS;
		int lastPoint = (int) Math.ceil(fraction * npoints);
		if (lastPoint < 0)
			lastPoint = 0;
		else if (lastPoint > npoints)
			lastPoint = npoints;
		
		fillScratch(firstBuffer, firstPoint*2, lastBuffer, lastPoint*2);
	}

	public void fillScratch(int firstBuffer, int first, int lastBuffer, int last) {
		float[] buf;
		int bufcnt, n, start, stop, elements;
		
		bufcnt = ((lastBuffer - firstBuffer + NBUFFERS) % NBUFFERS) + 1;
		n = 0;
		
		for (int i = 0; i < bufcnt; ++i) {
			buf = buffers[(firstBuffer + i) % NBUFFERS];
			start = (i == 0) ? first : 0;
			stop  = (i == bufcnt - 1) ? last - 1  : buffers[0].length - 1;
			
			elements = stop - start + 1;
			if (n + elements > scratch.length)
				elements = scratch.length - n;
			System.arraycopy(buf, start, scratch, n, elements);
			n += elements;
		}
		
		scratchBound = n;
	}

	public Path pathFromScratch(Range xrange, Range yrange) {
		Path path = new Path();
		float x, y, lastx = 0, lasty = 0;

		final int DRAW = 0,
				  SKIP = 1;
		
		int state = SKIP;
		x = scratch[0]; y = scratch[1];
		if (ok(y) && yrange.contains(y)) {
			path.moveTo(x, y);
			state = DRAW;
		}
		lastx = x; lasty = y;
		
		for (int i = 2; i < scratchBound; i += 2) {
			x = scratch[i]; y = scratch[i+1];
			switch (state) {
			
			case SKIP:
				if (!ok(y) || !yrange.contains(y))
					break;
				
				if (Float.isNaN(lasty)) {
					path.moveTo(x, y);
				}
				else if (Float.isInfinite(lasty)) {
					path.moveTo(x, (float) ((lasty < 0) ? yrange.min : yrange.max));
					path.lineTo(x, y);
				}
				else {
					if (lasty > yrange.max) {
						lastx = getIntersectX(lastx, lasty, x, y, (float) yrange.max);
						lasty = (float) yrange.max;
					}
					else if (lasty < yrange.min) {
						lastx = getIntersectX(lastx, lasty, x, y, (float) yrange.min);
						lasty = (float) yrange.min;
					}
					
					path.moveTo(lastx, lasty);
					path.lineTo(x, y);
				}
				state = DRAW;
				
				break;
				
			case DRAW:
				if (!ok(y)) {
					state = SKIP;
					break;
				}
				
				if (!yrange.contains(y)) {
					if (y > yrange.max) {
						x = getIntersectX(lastx, lasty, x, y, (float) yrange.max);
						y = (float) yrange.max;
					}
					else if (y < yrange.min) {
						x = getIntersectX(lastx, lasty, x, y, (float) yrange.min);
						y = (float) yrange.min;
					}
					state = SKIP;
				}
				path.lineTo(x, y);
				break;
				
			default:
				return path;
			}
			
			lastx = x; lasty = y;
		}
		
		return path;
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