package com.knpl.calc.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.Keyboard.Key;
import android.util.AttributeSet;

public class MyKeyboardView extends KeyboardView {
	
	public static final int FUNCTION_BLOCK_START = 61440,
							ACTION_BLOCK_START = 57344;
	
	public static final String[] functionLabels = 
		{"sqrt", "abs", 			// 61440 - 61441
		 "exp", "log",				// 61442 - 61443
		 "sin", "cos", "tan", 		// 61444 - 61446
		 "asin", "acos", "atan", 	// 61447 - 61449
		 "sinh", "cosh", "tanh"}; 	// 61450 - 61452
	
	public static final String[] actionLabels = 
		{"abc", "\u03B1\u03B2\u03B3", "123", "OK"}; // 57344 - 57347
	
	private Paint bgPaint,
				  pressedbgPaint,
				  labelPaint,
				  smallLabelPaint;
	
	private Rect rect;
	
	private int textYoffset;

	public MyKeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPreviewEnabled(false);
		
		rect = new Rect();
		bgPaint = new Paint();
		pressedbgPaint = new Paint();
		labelPaint = new Paint();
		smallLabelPaint = new Paint();
		
		bgPaint.setStyle(Paint.Style.FILL);
		bgPaint.setColor(Color.DKGRAY);
		
		pressedbgPaint.setStyle(Paint.Style.FILL);
		pressedbgPaint.setColor(Color.rgb(128, 128, 255));
		
		labelPaint.setTextAlign(Paint.Align.CENTER);
		labelPaint.setStyle(Paint.Style.STROKE);
		labelPaint.setTextSize(22);
		labelPaint.setColor(Color.WHITE);
		
		textYoffset = (int) (-labelPaint.ascent()/2f);
		
		smallLabelPaint.setTextAlign(Paint.Align.RIGHT);
		smallLabelPaint.setStyle(Paint.Style.STROKE);
		smallLabelPaint.setTextSize(16);
		smallLabelPaint.setColor(Color.LTGRAY);
	}
	
	public static boolean isFunction(int code) {
		return FUNCTION_BLOCK_START <= code && 
			   code <= FUNCTION_BLOCK_START + functionLabels.length;
	}
	
	public static boolean isAction(int code) {
		return ACTION_BLOCK_START <= code &&
			   code <= ACTION_BLOCK_START + actionLabels.length;
	}

	public String getLabelFromCode(int primaryCode) {
		String result;
		
		if (isFunction(primaryCode)) {
			result = functionLabels[primaryCode - FUNCTION_BLOCK_START];
		}
		else if (isAction(primaryCode)) {
			result = actionLabels[primaryCode - ACTION_BLOCK_START];
		}
		else {
			result = isShifted() ? "" + (char) Character.toUpperCase(primaryCode) 
								 : "" + (char) primaryCode; 
		}
		return result;
	}
	
	public void drawIcon(Drawable icon, Canvas canvas, Rect r, int xc, int yc) {
		int w = icon.getIntrinsicWidth();
		int h = icon.getIntrinsicHeight();
		rect.left = xc - w/2;
		rect.right = rect.left + w;
		rect.top = yc - h/2;
		rect.bottom = rect.top + h;
		icon.setBounds(rect);
		icon.draw(canvas);
	}
	
	private void drawKey(Canvas canvas, Key key) {
		rect.left = key.x + 1;
		rect.right = key.x + key.width - 1;
		rect.top = key.y + 1;
		rect.bottom = key.y + key.height - 1;
		
		int xc = key.x + key.width/2;
		int yc = key.y + key.height/2;
		Drawable icon = key.icon;

		Paint paint = key.pressed ? pressedbgPaint : bgPaint;
		canvas.drawRect(rect, paint);
		
		// Draw center label
		if (icon != null) {
			drawIcon(icon, canvas, rect, xc, yc);
			return;
		}
		
		char code = (char) key.codes[0];
		canvas.drawText(getLabelFromCode(code), xc, yc + textYoffset, labelPaint);
		
		if (key.popupResId == 0 || key.popupCharacters == null) { // No popup
			return;
		}
		
		String text;
		CharSequence chars = key.popupCharacters;
		if (chars.length() <= 1) {
			return;
		}
		text = getLabelFromCode(chars.charAt(1));
		canvas.drawText(text, rect.right - 2,
				rect.top - smallLabelPaint.ascent() + 2, smallLabelPaint);
		
		if (chars.length() <= 2) {
			return;
		}
		text = getLabelFromCode(chars.charAt(2));
		canvas.drawText(text, rect.right - 2,
				rect.bottom - smallLabelPaint.descent() - 2, smallLabelPaint);
	}

	@Override
	public void onDraw(Canvas canvas) {
		for (Key key : getKeyboard().getKeys()) {
			drawKey(canvas, key);
		}
	}
	
}