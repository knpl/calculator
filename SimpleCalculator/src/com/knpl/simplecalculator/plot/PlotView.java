package com.knpl.simplecalculator.plot;

import java.util.List;
import com.knpl.simplecalculator.SimpleCalculatorActivity;
import com.knpl.simplecalculator.plot.PlotStates.PlotState;
import com.knpl.simplecalculator.util.FormatUtils;
import com.knpl.simplecalculator.util.Pair;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class PlotView extends View {
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
		
		if (plotwidth > plotheight)
			xaxis = xaxis.extend(plotwidth/plotheight);
		else
			yaxis = yaxis.extend(plotheight/plotwidth);
		
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
	
	public void drawAxes(Canvas c, Matrix ctm, Range x, Range y) {
		float xy, yx;
		
		if 		(y.min > 0) xy = y.min;
		else if (y.max < 0) xy = y.max;
		else 				xy = 0;
		
		if 		(x.min > 0)	yx = x.min;
		else if (x.max < 0) yx = x.max;
		else 				yx = 0;
		
		float[] viewToScreenUnits = new float[]{1,1};
		ctm.mapVectors(viewToScreenUnits);
		
		drawXMarkers(c, ctm, x, xy, 1/viewToScreenUnits[0], 1/viewToScreenUnits[1]);
		drawYMarkers(c, ctm, y, yx, 1/viewToScreenUnits[0], 1/viewToScreenUnits[1]);

		Path path = new Path();
		path.moveTo(x.min, xy);
		path.lineTo(x.max, xy);
		path.moveTo(yx, y.min);
		path.lineTo(yx, y.max);
		path.transform(ctm);
		c.drawPath(path, linePaint);
	}
	
	private void drawXMarkers(Canvas c, Matrix ctm, Range x, float xy, float xscale, float yscale) {
		Matrix init = new Matrix();
		Matrix translate = new Matrix();
		
		Path path;
		float[] info;
		float start, step, cur;
		
		info = x.getMarkerInfo();
		start = info[0];
		step  = info[1];
		path = x.getMarkerModel();
		
		init.preConcat(ctm);
		init.preTranslate(start, xy);
		init.preScale(step, -5 * yscale);
		path.transform(init);
		translate.setTranslate(step / xscale, 0);
		
		float[] labelPos = new float[] {start, xy};
		ctm.mapPoints(labelPos);
		
		linePaint.setTextAlign(Align.CENTER);
		if (getHeight() - labelPos[1] < 50)
			labelPos[1] -= 15; 
		else
			labelPos[1] -= linePaint.ascent() - 15;
		
		c.save();
		cur = start;
		do {
			c.drawPath(path, linePaint);
			if (cur >= x.min && cur != 0f) {
				c.drawText(FormatUtils.format(x.viewToModel(cur), 3, 10),
						   labelPos[0], labelPos[1], linePaint);
			}
			c.concat(translate);
			cur += step;
		}
		while (cur < x.max);
		c.restore();
	}
	
	private void drawYMarkers(Canvas c, Matrix ctm, Range y, float yx, float xscale, float yscale) {
		Matrix init = new Matrix();
		Matrix translate = new Matrix();
		
		Path path;
		float[] info;
		float start, step, cur;
		
		info = y.getMarkerInfo();
		start = info[0];
		step  = info[1];
		path = y.getMarkerModel();
		
		init.preConcat(ctm);
		init.preTranslate(yx, start);
		init.preScale(-5 * xscale, step);
		init.preRotate(90);
		path.transform(init);
		translate.setTranslate(0, step / yscale);
		
		float[] labelPos = new float[] {yx, start};
		ctm.mapPoints(labelPos);
		labelPos[1] -= linePaint.ascent() / 2;
		if (labelPos[0] <= 50) {
			linePaint.setTextAlign(Align.LEFT);
			labelPos[0] += 15;
		}
		else {
			linePaint.setTextAlign(Align.RIGHT);
			labelPos[0] -= 15;
		}
		
		c.save();
		cur = start;
		do {
			c.drawPath(path, linePaint);
			if (cur >= y.min && cur != 0f) {
				c.drawText(FormatUtils.format(y.viewToModel(cur), 3, 10),
						   labelPos[0], labelPos[1], linePaint);
			}
			c.concat(translate);
			cur += step;
		}
		while (cur < y.max);
		c.restore();
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}

}
