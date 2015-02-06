package com.knpl.simplecalculator.plot;

import java.text.DecimalFormat;
import java.util.List;
import com.knpl.simplecalculator.SimpleCalculatorActivity;
import com.knpl.simplecalculator.util.Pair;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PlotView extends View {
	
	private static final DecimalFormat EFMT = new DecimalFormat("0.###E0");
	private static final DecimalFormat FMT = new DecimalFormat("0.###");
	
	private static final Paint standardPaint;
	
	public static final float leftmargin 	= .5f,
							  topmargin 	= .5f,
							  rightmargin 	= .5f,
							  bottommargin 	= .5f;

	static {
		standardPaint = new Paint();
		standardPaint.setAntiAlias(true);
		standardPaint.setColor(Color.BLACK);
		standardPaint.setStyle(Paint.Style.STROKE);
		standardPaint.setStrokeWidth(0f);
		standardPaint.setTextSize(16f);
	}
	
	private List<Pair<Mapper, Integer>> mappers;
	
	private Axis xaxis,
				 yaxis;
	
	private Matrix ctm;
	
	public PlotView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mappers = null;
		
		xaxis = SimpleCalculatorActivity.DEFAULT_AXIS;
		yaxis = SimpleCalculatorActivity.DEFAULT_AXIS;
		
		ctm = new Matrix();
	}
	
	public PlotView init(List<Pair<Mapper, Integer>> paths, Axis x, Axis y) {
		this.mappers = paths;
		this.xaxis = x;
		this.yaxis = y;
		
		return this;
	}
	
	
	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);
		
		final float screenwidth  = getWidth(),
					screenheight = getHeight(),
					plotwidth    = screenwidth - (leftmargin + rightmargin),
					plotheight   = screenheight - (topmargin + bottommargin);
		
		Axis x = xaxis, y = yaxis;
		if (plotwidth > plotheight)
			x = xaxis.extend(plotwidth/plotheight);
		else
			y = yaxis.extend(plotheight/plotwidth);

		ctm.reset();
		ctm.preTranslate(leftmargin, topmargin);
		ctm.preTranslate(0, plotheight);
		ctm.preScale(plotwidth, -plotheight);
		ctm.preScale(1f/(float)x.len(), 1f/(float)y.len());
		ctm.preTranslate(-x.min, -y.min);
		
		drawAxes(c, ctm, x, y);
		
		c.concat(ctm);

		float[] data = new float[512];
		RectF window = new RectF(x.min, y.min, x.max, y.max);
		
		for (Pair<Mapper, Integer> pair : mappers) {
			Mapper mapper = pair.getFirst();
			int color = pair.getLast();
			
			mapper.map(data, x, y);
			x.modelToView(data, 0, 2);
			y.modelToView(data, 1, 2);
			
			standardPaint.setColor(color);
			c.drawPath(trace(data, window), standardPaint);
		}
		standardPaint.setColor(Color.BLACK);
	}
	

	
	private static boolean ok(float x, float y) {
		return !(Float.isNaN(x) || Float.isNaN(y) ||
				 Float.isInfinite(x) || Float.isInfinite(y));
	}
	

	private enum State {
		DRAW, SKIP;
	}
	
	private Path trace(float[] data, RectF window) {
		Path path = new Path();
		
		State state = State.SKIP;
		if (ok(data[0], data[1]) && window.contains(data[0], data[1])) {
			path.moveTo(data[0], data[1]);
			state = State.DRAW;
		}
		
		for (int i = 2; i < data.length; i+=2) {
			
			switch (state) {
			
			case SKIP:
				
				if (ok(data[i], data[i+1]) && window.contains(data[i], data[i+1])) {
					if (data[i-1] == Float.POSITIVE_INFINITY) {
						path.moveTo(data[i], window.bottom);
						path.lineTo(data[i], data[i+1]);
					}
					else if (data[i-1] == Float.NEGATIVE_INFINITY) {
						path.moveTo(data[i], window.top);
						path.lineTo(data[i], data[i+1]);
					}
					else if (Float.isNaN(data[i-1])) {
						path.moveTo(data[i], data[i+1]);
					}
					else {
						path.moveTo(data[i], data[i+1]);
					}
					state = State.DRAW;
				}
				break;
				
			case DRAW:
				
				if (ok(data[i], data[i+1])) {
					if (!window.contains(data[i], data[i+1])) {
						state = State.SKIP;
					}
					else {
						path.lineTo(data[i], data[i+1]);
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
	

	private void fill(float[] dst, int index, int step, float fill, int n) {
		for (int i = index; i < step*n; i += step) {
			dst[i] = fill;
		}
	}
	
	private void generateLabels(String[] labels, float[] values, int n, int index, int step) {
		int i,j;
		for (j = 0, i = index; i < n*step; i += step, j++) {
			if ((values[i] + 0.0) == 0) {
				labels[j] = "";
			}
			else {
				labels[j] = (-100 <= values[i] && values[i] <= 100) ? FMT.format(0.0 + values[i])
																	: EFMT.format(values[i]); 
			}
		}
	}

	public void drawAxes(Canvas c, Matrix ctm, Axis x, Axis y) {
		Path p = new Path();
		float[] markers = new float[40];
		String[] labels = new String[20];
		int i, n;
		
		final float xaxisy = (y.min <= 0f && 0f <= y.max) ? 0f : y.min,
			        yaxisx = (x.min <= 0f && 0f <= x.max) ? 0f : x.min; 
	
		// Draw axes
		p.moveTo(x.min, xaxisy);
		p.lineTo(x.max, xaxisy);
		p.moveTo(yaxisx, y.min);
		p.lineTo(yaxisx, y.max);
		p.transform(ctm);
		c.drawPath(p, standardPaint);
		p.rewind();
		
		// Draw markers on x axis
		n = x.generateMarkers(markers, 0, 2);
		generateLabels(labels, markers, n, 0, 2);
		fill(markers, 1, 2, xaxisy, n);
		Axis.modelToView(markers, x, y);
		ctm.mapPoints(markers);
		for (i = 0; i < n; ++i) {
			p.moveTo(markers[2*i], markers[2*i+1]);
			p.rLineTo(0, -10);
		}
		c.drawPath(p, standardPaint);
		p.rewind();
		
		// Draw labels on x axis
		standardPaint.setTextAlign(Paint.Align.CENTER);
		for (i = 0; i < n; ++i) {
			c.drawText(labels[i], markers[2*i], markers[2*i+1] + standardPaint.ascent(), standardPaint);
		}
		
		// Draw markers on y axis
		n = y.generateMarkers(markers, 1, 2);
		generateLabels(labels, markers, n, 1, 2);
		fill(markers, 0, 2, yaxisx, n);
		Axis.modelToView(markers, x, y);
		ctm.mapPoints(markers);
		for (i = 0; i < n; ++i) {
			p.moveTo(markers[2*i], markers[2*i+1]);
			p.rLineTo(10, 0);
		}
		c.drawPath(p, standardPaint);
		p.rewind();

		// Draw labels on y axis
		standardPaint.setTextAlign(Paint.Align.LEFT);
		for (i = 0; i < n; ++i) {
			c.drawText(labels[i], markers[2*i] + 15, markers[2*i+1], standardPaint);
		}
	}
}
