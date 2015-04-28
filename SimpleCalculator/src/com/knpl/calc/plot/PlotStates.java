package com.knpl.calc.plot;

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
	
	public static enum PlotState {STATIC, EXPLORE, DRAGGING, ZOOMING, INVALID};
	
	public static final long DOUBLE_TAP_MILLIS = 250;
	
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
				
				now = System.currentTimeMillis();
				if (now - then <= DOUBLE_TAP_MILLIS) {
					plotView.setState(PlotState.EXPLORE);
					break;
				}
				then = now;
				break;
				
			default:
			}
			
			return true;
		}
	}
	
	public static class ExploreState implements OnTouchListener {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			final PlotView plotView = (PlotView) v;
			int action = event.getActionMasked();
			switch (action) {
			
			case MotionEvent.ACTION_DOWN:
				downAction(plotView, event);
				plotView.setState(PlotState.DRAGGING);
				break;
			
			case MotionEvent.ACTION_UP:
				v.performClick();
				break;
				
			case MotionEvent.ACTION_CANCEL:
				break;
				
			default:
				plotView.setState(PlotState.INVALID);
			}
			
			return true;
		}
		
		public void downAction(PlotView plotView, MotionEvent event) {
			dragState.setStart(event.getX(0), event.getY(0));
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
				plotView.setState(PlotState.ZOOMING);
				break;
			
			case MotionEvent.ACTION_UP:
				v.performClick();
				
				actionUp(plotView, event);
				
				now = System.currentTimeMillis();
				if (now - then <= DOUBLE_TAP_MILLIS) {
					plotView.setState(PlotState.STATIC);
				}
				else {
					then = now;
					plotView.setState(PlotState.EXPLORE);
				}
				break;
				
			case MotionEvent.ACTION_MOVE:
				actionMove(plotView, event);
				break;
				
			case MotionEvent.ACTION_CANCEL:
				plotView.setState(PlotState.EXPLORE);
				break;
				
			default:
				plotView.setState(PlotState.INVALID);
			}
			
			return true;
		}
		
		public void actionMove(PlotView plotView, MotionEvent event) {
			float x1 = event.getX(0),
				  y1 = event.getY(0);
			plotView.setTranslatePreview(x1-x0, y1-y0);
		}
		
		public void actionUp(PlotView plotView, MotionEvent event) {
			float x1 = event.getX(0),
				  y1 = event.getY(0);
			plotView.setTranslatePreview(0, 0);
			plotView.translate(x1-x0, y1-y0);
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
				plotView.setState(PlotState.DRAGGING);
				break;
				
			case MotionEvent.ACTION_MOVE:
				actionMove(plotView, event);
				break;
				
			case MotionEvent.ACTION_CANCEL:
				plotView.setState(PlotState.EXPLORE);
				break;
				
			default:
				plotView.setState(PlotState.INVALID);
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
		}
	}
	
	public static class InvalidState implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			v.performClick();
			return true;
		}
	}
}
