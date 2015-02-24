package com.knpl.simplecalculator.plot;

import java.text.DecimalFormat;
import java.util.List;
import com.knpl.simplecalculator.SimpleCalculatorActivity;
import com.knpl.simplecalculator.plot.PlotStates.PlotState;
import com.knpl.simplecalculator.util.Pair;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class PlotView extends View {
	
	private static final DecimalFormat FMT = new DecimalFormat("0.######");
	
	private static final Paint linePaint;
	
	public static final float leftPadding 	= .5f,
							  topPadding 	= .5f,
							  rightPadding 	= .5f,
							  bottomPadding = .5f;

	static {
		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setColor(Color.BLACK);
		linePaint.setStyle(Paint.Style.STROKE);
		linePaint.setStrokeWidth(0f);
		linePaint.setTextSize(16f);
	}
	
	private List<Pair<Mapper, Integer>> mappers;
	
	private Axis xaxis,
				 yaxis;
	
	private float[] translate,
					scalecenter;
	private float   scalefactor;
	
	private PlotState plotState;
	
	private Matrix ctm,
				   modelToScreen,
				   screenToModel;
	
	public PlotView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mappers = null;
		
		xaxis = SimpleCalculatorActivity.DEFAULT_AXIS;
		yaxis = SimpleCalculatorActivity.DEFAULT_AXIS;
		
		translate = new float[]{0f,0f};
		scalecenter = new float[]{0f,0f};
		scalefactor = 1f;
		
		plotState = PlotState.STATIC;
		
		ctm = new Matrix();
		modelToScreen = new Matrix();
		screenToModel = new Matrix();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		final float screenwidth  = w,
					screenheight = h,
					plotwidth    = screenwidth - (leftPadding + rightPadding),
					plotheight   = screenheight - (topPadding + bottomPadding);
		
		if (plotwidth > plotheight) {
			xaxis = xaxis.extend(plotwidth/plotheight);
		}
		else {
			yaxis = yaxis.extend(plotheight/plotwidth);
		}
		
		modelToScreen.reset();
		modelToScreen.preTranslate(leftPadding, topPadding);
		modelToScreen.preTranslate(0, plotheight);
		modelToScreen.preScale(plotwidth, -plotheight);
		modelToScreen.preScale(1f/(float)xaxis.len(), 1f/(float)yaxis.len());
		modelToScreen.preTranslate(-xaxis.min, -yaxis.min);
		
		modelToScreen.invert(screenToModel);
	}

	public PlotView init(List<Pair<Mapper, Integer>> paths, Axis x, Axis y) {
		this.mappers = paths;
		this.xaxis = x;
		this.yaxis = y;
		
		setState(PlotState.STATIC);
		
		return this;
	}
	
	public void setState(PlotState plotState) {
		this.plotState = plotState;
		OnTouchListener l = PlotStates.invalidState;
		switch (plotState) {
		case DRAGGING:	l = PlotStates.dragState;		break;
		case EXPLORE:	l = PlotStates.exploreState;	break;
		case INVALID:	l = PlotStates.invalidState;	break;
		case STATIC:	l = PlotStates.staticState;		break;
		case ZOOMING:	l = PlotStates.zoomState;		break;
		}
		setOnTouchListener(l);
	}
	
	public void setTranslatePreview(float x, float y) {
		translate[0] = x;
		translate[1] = y;
		
		invalidate();
	}
	
	public void translate(float x, float y) {
		float[] dv = new float[]{x,y};
		screenToModel.mapVectors(dv);
		
		xaxis = xaxis.create(xaxis.min - dv[0], xaxis.max - dv[0]);
		yaxis = yaxis.create(yaxis.min - dv[1], yaxis.max - dv[1]);
		
		modelToScreen.postTranslate(x, y);
		modelToScreen.invert(screenToModel);
		
		invalidate();
	}
	public void setScalePreview(float cx, float cy, float f) {
		scalecenter[0] = cx;
		scalecenter[1] = cy;
		scalefactor = f;
		
		invalidate();
	}
	
	public void scale(float cx, float cy, float f) {
		Matrix scale = new Matrix();
		scale.preTranslate(cx, cy);
		scale.preScale(f, f);
		scale.preTranslate(-cx, -cy);
		
		Matrix scaleInverse = new Matrix();
		scale.invert(scaleInverse);
		
		float[] pts = new float[]{xaxis.min, yaxis.min, xaxis.max, yaxis.max};
		modelToScreen.mapPoints(pts);
		scaleInverse.mapPoints(pts);
		screenToModel.mapPoints(pts);
		
		xaxis = new Axis(pts[0], pts[2]);
		yaxis = new Axis(pts[1], pts[3]);
		
		modelToScreen.postConcat(scale);
		modelToScreen.invert(screenToModel);
		
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas c) {
		setBackgroundColor(Color.WHITE);
		super.onDraw(c);
		
		ctm.reset();
		switch (plotState) {
		case DRAGGING:
			ctm.preTranslate(translate[0], translate[1]);
			break;
		case ZOOMING:
			ctm.preTranslate(scalecenter[0], scalecenter[1]);
			ctm.preScale(scalefactor, scalefactor);
			ctm.preTranslate(-scalecenter[0], -scalecenter[1]);
			break;
		default:
		}
		ctm.preConcat(modelToScreen);
		
		drawAxes(c, ctm, xaxis, yaxis);

		float[] data = new float[512];
		Pair<Axis, Axis> axisPair = Axis.map(xaxis, yaxis, ctm);
		
		for (Pair<Mapper, Integer> pair : mappers) {
			Mapper mapper = pair.getFirst();
			int color = pair.getLast();
			
			mapper.map(data, xaxis, yaxis);
			xaxis.modelToView(data, 0, 2);
			yaxis.modelToView(data, 1, 2);
			
			ctm.mapPoints(data);
			
			linePaint.setColor(color);
			c.drawPath(trace(data, axisPair.getFirst(), axisPair.getLast()), linePaint);
		}
		linePaint.setColor(Color.BLACK);
	}

	private void fill(float[] dst, int index, int step, float fill, int n) {
		for (int i = index; i < step*n; i += step) {
			dst[i] = fill;
		}
	}
	
	private void generateLabels(String[] labels, float[] values, int n, int index, int step) {
		int i,j;
		for (j = 0, i = index; i < n*step; i += step, j++) {
			if ((values[i] + 0.0) == 0)
				labels[j] = "";
			else
				labels[j] = FMT.format(0.0 + values[i]);
		}
	}

	public void drawAxes(Canvas c, Matrix ctm, Axis x, Axis y) {
		Path p = new Path();
		float[] markers = new float[40];
		String[] labels = new String[20];
		int i, n;
		
		float xaxisy = 0,
			  xlabeloff = linePaint.ascent();
		if (y.min >= 0) {
			xaxisy = y.min;
		}
		else if (y.max <= 0) {
			xlabeloff = 10 -linePaint.ascent();
			xaxisy = y.max;
		} 
		
		float yaxisx = 0,
			  ylabeloff = 15;
		boolean alignleft = true;
		if (x.min >= 0) {
			yaxisx = x.min;
		}
		else if (x.max <= 0) {
			alignleft = false;
			ylabeloff = -15;
			yaxisx = x.max;
		}
		
		// Draw axes
		p.moveTo(x.min, xaxisy);
		p.lineTo(x.max, xaxisy);
		p.moveTo(yaxisx, y.min);
		p.lineTo(yaxisx, y.max);
		p.transform(ctm);
		c.drawPath(p, linePaint);
		p.rewind();
		
		// Draw markers on x axis
		n = x.generateMarkers(markers, 0, 2);
		generateLabels(labels, markers, n, 0, 2);
		fill(markers, 1, 2, xaxisy, n);
		Axis.modelToView(markers, x, y);
		ctm.mapPoints(markers);
		for (i = 0; i < n; ++i) {
			p.moveTo(markers[2*i], markers[2*i+1] + 5);
			p.rLineTo(0, -10);
		}
		c.drawPath(p, linePaint);
		p.rewind();
		
		// Draw labels on x axis
		linePaint.setTextAlign(Paint.Align.CENTER);
		for (i = 0; i < n; ++i) {
			c.drawText(labels[i], markers[2*i], markers[2*i+1] + xlabeloff, linePaint);
		}
		
		// Draw markers on y axis
		n = y.generateMarkers(markers, 1, 2);
		generateLabels(labels, markers, n, 1, 2);
		fill(markers, 0, 2, yaxisx, n);
		Axis.modelToView(markers, x, y);
		ctm.mapPoints(markers);
		for (i = 0; i < n; ++i) {
			p.moveTo(markers[2*i] - 5, markers[2*i+1]);
			p.rLineTo(10, 0);
		}
		c.drawPath(p, linePaint);
		p.rewind();

		// Draw labels on y axis
		if (alignleft)
			linePaint.setTextAlign(Paint.Align.LEFT);
		else
			linePaint.setTextAlign(Paint.Align.RIGHT);
		for (i = 0; i < n; ++i) {
			c.drawText(labels[i], markers[2*i] + ylabeloff, markers[2*i+1], linePaint);
		}
	}
	
	private static boolean ok(float x, float y) {
		return !(Float.isNaN(x) || Float.isNaN(y) ||
				 Float.isInfinite(x) || Float.isInfinite(y));
	}
	
	private static final int DRAW = 0,
							 SKIP = 1;
	
	private Path trace(float[] data, Axis xaxis, Axis yaxis) {
		Path path = new Path();
		float x, y, lastx, lasty;
		
		int state = SKIP;
		x = data[0]; y = data[1];
		if (ok(x, y) && xaxis.contains(x) && yaxis.contains(y)) {
			path.moveTo(x, y);
			state = DRAW;
		}
		lastx = x; lasty = y;
		
		for (int i = 2; i < data.length; i+=2) {
			x = data[i]; y = data[i+1];
			switch (state) {
			
			case SKIP:
				
				if (ok(x, y) && xaxis.contains(x) && yaxis.contains(y)) {
					if (lasty == Float.POSITIVE_INFINITY) {
						path.moveTo(x, yaxis.max);
						path.lineTo(x, y);
					}
					else if (lasty == Float.NEGATIVE_INFINITY) {
						path.moveTo(x, yaxis.min);
						path.lineTo(x, y);
					}
					else if (Float.isNaN(lasty)) {
						path.moveTo(x, y);
					}
					else {
						path.moveTo(lastx, lasty);
						path.lineTo(x, y);
					}
					state = DRAW;
				}
				break;
				
			case DRAW:
				
				if (ok(x, y)) {
					if (!(xaxis.contains(x) && yaxis.contains(y))) {
						state = SKIP;
					}
					path.lineTo(x, y);
				}
				else {
					state = SKIP;
				}
				break;
				
			default:
				return path;
			}
			
			lastx = x; lasty = y;
		}
		
		return path;
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}

}
