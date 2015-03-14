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
	
	private Range xaxis,
				 yaxis;
	
	private float[] translate,
					scalecenter;
	private float   scalefactor;
	
	private PlotState plotState;
	
	private Matrix ctm,
				   viewToNormal,
				   normalToView,
				   normalToScreen,
				   screenToNormal;
	
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
		
		viewToNormal = new Matrix();
		normalToView = new Matrix();
		
		normalToScreen = new Matrix();
		screenToNormal = new Matrix();
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
		
		normalToScreen.reset();
		normalToScreen.preTranslate(leftPadding, topPadding);
		normalToScreen.preTranslate(0, plotheight);
		normalToScreen.preScale(plotwidth, -plotheight);
		normalToScreen.invert(screenToNormal);
		
		viewToNormal.reset();
		viewToNormal.preScale(1/xaxis.len(), 1/yaxis.len());
		viewToNormal.preTranslate(-xaxis.min, -yaxis.min);
		viewToNormal.invert(normalToView);
	}

	public PlotView init(List<Pair<Mapper, Integer>> paths, Range x, Range y) {
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
	
	public void setWindow(Range xrange, Range yrange) {
		xaxis = xrange;
		yaxis = yrange;
		
		viewToNormal.reset();
		viewToNormal.preScale(1/xaxis.len(), 1/yaxis.len());
		viewToNormal.preTranslate(-xaxis.min, -yaxis.min);
		viewToNormal.invert(normalToView);
	}
	
	public void translate(float x, float y) {
		float[] dv = new float[]{x,y};
		screenToNormal.mapVectors(dv);
		normalToView.mapVectors(dv);
		
		setWindow(xaxis.create(xaxis.viewToModel(xaxis.min - dv[0]),
				  			   xaxis.viewToModel(xaxis.max - dv[0])),
				  yaxis.create(yaxis.viewToModel(yaxis.min - dv[1]),
						  	   yaxis.viewToModel(yaxis.max - dv[1])));
		
		invalidate();
	}
	
	public void setScalePreview(float cx, float cy, float f) {
		scalecenter[0] = cx;
		scalecenter[1] = cy;
		scalefactor = f;
		
		invalidate();
	}
	
	public void scale(float cx, float cy, float f) {
		float[] center = new float[]{cx,cy};
		screenToNormal.mapPoints(center);
		normalToView.mapPoints(center);
		
		setWindow(xaxis.create(xaxis.viewToModel(center[0] + (xaxis.min - center[0])/f), 
							   xaxis.viewToModel(center[0] + (xaxis.max - center[0])/f)),
				  yaxis.create(yaxis.viewToModel(center[1] + (yaxis.min - center[1])/f), 
							   yaxis.viewToModel(center[1] + (yaxis.max - center[1])/f)));
		
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas c) {
		setBackgroundColor(Color.WHITE);
		super.onDraw(c);
		
		ctm.reset();
		ctm.preConcat(normalToScreen);
		
		Range xrange, yrange;
		float[] viewTranslate;
		switch (plotState) {

		case DRAGGING:
			viewTranslate = new float[2];
			screenToNormal.mapVectors(viewTranslate, translate);
			normalToView.mapVectors(viewTranslate);
			
			xrange = xaxis.create(xaxis.viewToModel(xaxis.min - viewTranslate[0]),
								  xaxis.viewToModel(xaxis.max - viewTranslate[0]));
			yrange = yaxis.create(yaxis.viewToModel(yaxis.min - viewTranslate[1]),
								  yaxis.viewToModel(yaxis.max - viewTranslate[1]));
			
			ctm.preScale(1/xrange.len(), 1/yrange.len());
			ctm.preTranslate(-xrange.min, -yrange.min);
			break;
			
		case ZOOMING:
			viewTranslate = new float[2];
			screenToNormal.mapPoints(viewTranslate, scalecenter);
			normalToView.mapPoints(viewTranslate);
			
			xrange = xaxis.create(xaxis.viewToModel(viewTranslate[0] + (xaxis.min - viewTranslate[0])/scalefactor), 
								  xaxis.viewToModel(viewTranslate[0] + (xaxis.max - viewTranslate[0])/scalefactor));
			yrange = yaxis.create(yaxis.viewToModel(viewTranslate[1] + (yaxis.min - viewTranslate[1])/scalefactor), 
								  yaxis.viewToModel(viewTranslate[1] + (yaxis.max - viewTranslate[1])/scalefactor));
			
			ctm.preScale(1/xrange.len(), 1/yrange.len());
			ctm.preTranslate(-xrange.min, -yrange.min);
			break;
			
		default:
			xrange = xaxis;
			yrange = yaxis;
			ctm.preConcat(viewToNormal);
		}
		
		drawAxes(c, ctm, xrange, yrange);

		for (Pair<Mapper, Integer> pair : mappers) {
			Mapper mapper = pair.getFirst();
			int color = pair.getLast();

			linePaint.setColor(color);
			c.drawPath(mapper.map(ctm, xrange, yrange), linePaint);
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

	public void drawAxes(Canvas c, Matrix ctm, Range x, Range y) {
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
		Range.modelToView(markers, x, y);
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
		Range.modelToView(markers, x, y);
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

	@Override
	public boolean performClick() {
		return super.performClick();
	}

}
