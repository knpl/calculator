package com.knpl.simplecalculator.plot;

import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.View;
import android.view.View.OnTouchListener;

public class PlotStates {
	
	public static final StaticState staticState = new StaticState();
	public static final ExploreState exploreState = new ExploreState();
	public static final DragState dragState = new DragState();
	public static final ZoomState zoomState = new ZoomState();
	public static final InvalidState invalidState = new InvalidState();
	
	public static final long DOUBLE_TAP_MILLIS = 500;
	
	public static class StaticState implements OnTouchListener {
		
		private long then;
		
		public StaticState() {
			then = 0;
		}
	
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			final PlotView plotView = (PlotView) v;
			int action = event.getActionMasked();
			long now;
			
			switch (action) {
			
			case MotionEvent.ACTION_UP:
				v.performClick();
				
				// Check for double tap
				now = System.currentTimeMillis();
				if (now - then <= DOUBLE_TAP_MILLIS) {
					android.util.Log.d("mytag", "(static) double tap");
					plotView.setOnTouchListener(exploreState);
					break;
				}
				then = now;
				
				// Single tap
				
				break;
				
			default:
			}
			
			return true;
		}
	}
	
	public static class ExploreState implements OnTouchListener {
		
		public ExploreState() {
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			final PlotView plotView = (PlotView) v;
			int action = event.getActionMasked();
			switch (action) {
			
			case MotionEvent.ACTION_DOWN:
				downAction(plotView, event);
				break;
			
			case MotionEvent.ACTION_UP:
				v.performClick();
				break;
				
			case MotionEvent.ACTION_CANCEL:
				android.util.Log.d("mytag", "(explore) cancel (explore)");
				break;
				
			default:
				android.util.Log.d("mytag", "(explore) -> (invalid)");
				plotView.setOnTouchListener(invalidState);
			}
			
			return true;
		}
		
		public void downAction(PlotView plotView, MotionEvent event) {
			android.util.Log.d("mytag", "(explore) down (drag)");
			dragState.setStart(event.getX(0), event.getY(0));
			plotView.setOnTouchListener(dragState);
		}
	}
	
	public static class DragState implements OnTouchListener {
		
		private long then;
		private float x0, y0;
		
		public DragState() {
			then = 0;
			x0 = y0 = 0f;
		}
		
		public void setStart(float x0, float y0) {
			this.x0 = x0;
			this.y0 = y0;
		}
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			final PlotView plotView = (PlotView) v;
			int action = event.getActionMasked();
			long now;
			
			switch (action) {
				
			case MotionEvent.ACTION_POINTER_DOWN:
				actionPointerDown(plotView, event);
				plotView.setOnTouchListener(zoomState);
				break;
			
			case MotionEvent.ACTION_UP:
				v.performClick();
				
				actionUp(plotView, event);
				
				now = System.currentTimeMillis();
				if (now - then <= DOUBLE_TAP_MILLIS) {
					android.util.Log.d("mytag", "(explore) double tap (static)");
					plotView.setOnTouchListener(staticState);
				}
				else {
					then = now;
					android.util.Log.d("mytag", "(drag) up (explore)");
					plotView.setOnTouchListener(exploreState);
				}
				break;
				
			case MotionEvent.ACTION_MOVE:
				actionMove(plotView, event);
				break;
				
			case MotionEvent.ACTION_CANCEL:
				android.util.Log.d("mytag", "(drag) cancel (explore)");
				plotView.setOnTouchListener(exploreState);
				break;
				
			default:
				android.util.Log.d("mytag", "(drag) -> (invalid)");
				plotView.setOnTouchListener(invalidState);
			}
			
			return true;
		}
		
		public void actionMove(PlotView plotView, MotionEvent event) {
			float x1 = event.getX(0),
				  y1 = event.getY(0);
			plotView.setTranslatePreview(x1-x0, y1-y0);
			plotView.invalidate();
		}
		
		public void actionUp(PlotView plotView, MotionEvent event) {
			float x1 = event.getX(0),
				  y1 = event.getY(0);
			plotView.setTranslatePreview(0, 0);
			plotView.translate(x1-x0, y1-y0);
			plotView.invalidate();
		}
		
		public void actionPointerDown(PlotView plotView, MotionEvent event) {
			PointerCoords p0 = new PointerCoords(),
						  p1 = new PointerCoords();
			event.getPointerCoords(0, p0);
			event.getPointerCoords(1, p1);
			
			plotView.setTranslatePreview(0, 0);
			plotView.translate(p0.x-x0, p0.y-y0);
			x0 = y0 = 0f;
			
			float dx = (p1.x - p0.x),
				  dy = (p1.y - p0.y);
			
			zoomState.setStartCircle(p0.x + .5f*dx, p0.y + .5f*dy,
										(float)(Math.sqrt(dx*dx + dy*dy)/2.0));
		}
	}
	
	public static class ZoomState implements OnTouchListener {

		float cx, cy, cr;
		
		public ZoomState() {
			cr = 1;
			cx = 0;
			cy = 0;
		}
		
		public void setStartCircle(float cx, float cy, float cr) {
			this.cx = cx;
			this.cy = cy;
			this.cr = cr;
			
			android.util.Log.d("mytag", "c: "+cx+", "+cy+" r:"+cr);
		}
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			final PlotView plotView = (PlotView) v;
			int action = event.getActionMasked();
			
			switch (action) {
			
			case MotionEvent.ACTION_UP:
				v.performClick();
				break;
				
			case MotionEvent.ACTION_POINTER_UP:
				actionPointerUp(plotView, event);
				android.util.Log.d("mytag", "(zoom) pointer up (drag)");
				plotView.setOnTouchListener(dragState);
				break;
				
			case MotionEvent.ACTION_MOVE:
				actionMove(plotView, event);
				break;
				
			case MotionEvent.ACTION_CANCEL:
				android.util.Log.d("mytag", "(zoom) cancel (explore)");
				plotView.setOnTouchListener(exploreState);
				break;
				
			default:
				android.util.Log.d("mytag", "(zoom) -> (invalid)");
				plotView.setOnTouchListener(invalidState);
			}
			
			return true;
		}

		private void actionPointerUp(PlotView plotView, MotionEvent event) {
			PointerCoords p0 = new PointerCoords(),
						  p1 = new PointerCoords();
			event.getPointerCoords(0, p0);
			event.getPointerCoords(1, p1);
		
			float dx = (p1.x - p0.x),
				  dy = (p1.y - p0.y),
				  r  = (float)Math.sqrt(dx*dx+dy*dy)/2f;
			
			plotView.setScalePreview(0, 0, 1);
			plotView.scale(cx, cy, r/cr);
			
			PointerCoords notReleased =  (1 - event.getActionIndex() == 0) ? p0 : p1;
			dragState.setStart(notReleased.x, notReleased.y);
			
			plotView.invalidate();
		}

		private void actionMove(PlotView plotView, MotionEvent event) {
			PointerCoords p0 = new PointerCoords(),
						  p1 = new PointerCoords();
			event.getPointerCoords(0, p0);
			event.getPointerCoords(1, p1);
		
			float dx = (p1.x - p0.x),
				  dy = (p1.y - p0.y),
				  r  = (float)Math.sqrt(dx*dx+dy*dy)/2f;
			
			plotView.setScalePreview(cx, cy, r/cr);
			plotView.invalidate();
		}
	}
	
	public static class InvalidState implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			android.util.Log.d("mytag", "(invalid)");
			return true;
		}
	}
}
