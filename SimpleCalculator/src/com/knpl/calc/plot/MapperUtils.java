package com.knpl.calc.plot;

import android.graphics.Path;

public class MapperUtils {

	public static Path pathInWindow(Path path, float[] src, int cnt,
						float xmin, float xmax, float ymin, float ymax)
	{
		float x, y, lastx = 0, lasty = 0;

		final int DRAW = 0,
				  SKIP = 1;
		
		int n = cnt > src.length ? src.length : cnt;
		if (n < 2) {
			return path;
		}
		
		int state = SKIP;
		x = src[0]; y = src[1];
		if (ok(x) && ok(y) && (xmin <= x && x <= xmax) && (ymin <= y && y <= ymax)) {
			path.moveTo(x, y);
			state = DRAW;
		}
		lastx = x; lasty = y;
		
		for (int i = 2; i + 1 < n; i += 2) {
			x = src[i]; y = src[i+1];
			switch (state) {
			
			case SKIP:
				if (!ok(x) || !ok(y) || !(xmin <= x && x <= xmax) || !(ymin <= y && y <= ymax)) {
					break;
				}
				
				if (!ok(lastx) || !ok(lasty)) {
					path.moveTo(x, y);
				}
				else {
					if (lastx > xmax) {
						lasty = getIntersectY(lastx, lasty, x, y, xmax);
						lastx = xmax;
					}
					else if (lastx < xmin) {
						lasty = getIntersectY(lastx, lasty, x, y, xmin);
						lastx = xmin;
					}
					else if (lasty > ymax) {
						lastx = getIntersectX(lastx, lasty, x, y, ymax);
						lasty = ymax;
					}
					else if (lasty < ymin) {
						lastx = getIntersectX(lastx, lasty, x, y, ymin);
						lasty = ymin;
					}
					
					path.moveTo(lastx, lasty);
					path.lineTo(x, y);
				}
				state = DRAW;
				break;
				
			case DRAW:
				if (!ok(x) && !ok(y)) {
					state = SKIP;
					break;
				}
				
				if (x > xmax) {
					y = getIntersectY(lastx, lasty, x, y, xmax);
					x = xmax;
					state = SKIP;
				}
				else if (x < xmin) {
					y = getIntersectY(lastx, lasty, x, y, xmin);
					x = xmin;
					state = SKIP;
				}
				else if (y > ymax) {
					x = getIntersectX(lastx, lasty, x, y, ymax);
					y = ymax;
					state = SKIP;
				}
				else if (y < ymin) {
					x = getIntersectX(lastx, lasty, x, y, ymin);
					y = ymin;
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
	
	public static Path pathInRangeX(Path path, float[] src, int cnt, float xmin, float xmax)
	{
		float x, y, lastx = 0, lasty = 0;

		final int DRAW = 0,
				  SKIP = 1;
		
		int n = cnt > src.length ? src.length : cnt;
		if (n < 2) {
			return path;
		}
		
		int state = SKIP;
		x = src[0]; y = src[1];
		if (ok(x) && xmin <= x && x <= xmax) {
			path.moveTo(x, y);
			state = DRAW;
		}
		lastx = x; lasty = y;
		
		for (int i = 2; i + 1 < cnt; i += 2) {
			x = src[i]; y = src[i+1];
			switch (state) {
			
			case SKIP:
				if (!ok(y) || !(xmin <= x && x <= xmax))
					break;
				
				if (Float.isNaN(lastx)) {
					path.moveTo(x, y);
				}
				else if (Float.isInfinite(lastx)) {
					path.moveTo(x, (lastx < 0) ? xmin : xmax);
					path.lineTo(x, y);
				}
				else {
					if (lastx > xmax) {
						lasty = getIntersectY(lastx, lasty, x, y, xmax);
						lastx = xmax;
					}
					else if (lastx < xmin) {
						lasty = getIntersectY(lastx, lasty, x, y, xmin);
						lastx = xmin;
					}
					
					path.moveTo(lastx, lasty);
					path.lineTo(x, y);
				}
				state = DRAW;
				
				break;
				
			case DRAW:
				if (!ok(x)) {
					state = SKIP;
					break;
				}
				
				if (!(xmin <= x && x <= xmax)) {
					if (x > xmax) {
						y = getIntersectY(lastx, lasty, x, y, xmax);
						x = xmax;
					}
					else if (x < xmin) {
						y = getIntersectY(lastx, lasty, x, y, xmin);
						x = xmin;
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
	
	public static Path pathInRangeY(Path path, float[] src, int cnt, float ymin, float ymax)
	{
		float x, y, lastx = 0, lasty = 0;

		final int DRAW = 0,
				  SKIP = 1;
		
		int n = cnt > src.length ? src.length : cnt;
		if (n < 2) {
			return path;
		}
		
		int state = SKIP;
		x = src[0]; y = src[1];
		if (ok(y) && ymin <= y && y <= ymax) {
			path.moveTo(x, y);
			state = DRAW;
		}
		lastx = x; lasty = y;
		
		for (int i = 2; i + 1 < cnt; i += 2) {
			x = src[i]; y = src[i+1];
			switch (state) {
			
			case SKIP:
				if (!ok(y) || !(ymin <= y && y <= ymax))
					break;
				
				if (Float.isNaN(lasty)) {
					path.moveTo(x, y);
				}
				else if (Float.isInfinite(lasty)) {
					path.moveTo(x, (lasty < 0) ? ymin : ymax);
					path.lineTo(x, y);
				}
				else {
					if (lasty > ymax) {
						lastx = getIntersectX(lastx, lasty, x, y, ymax);
						lasty = ymax;
					}
					else if (lasty < ymin) {
						lastx = getIntersectX(lastx, lasty, x, y, ymin);
						lasty = ymin;
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
				
				if (!(ymin <= y && y <= ymax)) {
					if (y > ymax) {
						x = getIntersectX(lastx, lasty, x, y, ymax);
						y = ymax;
					}
					else if (y < ymin) {
						x = getIntersectX(lastx, lasty, x, y, ymin);
						y = ymin;
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

	public static boolean ok(float f) {
		return !(Float.isNaN(f) || Float.isInfinite(f));
	}
	
	public static float getIntersectX(float x0, float y0, float x1, float y1, float y) {
		float dx = x1 - x0,
			  dy = y1 - y0;
		return x0 + (dx/dy)*(y - y0);
	}
	
	private static float getIntersectY(float x0, float y0, float x1, float y1, float x) {
		float dx = x1 - x0,
			  dy = y1 - y0;
		return y0 + (dy/dx)*(x - x0);
	}	
}
