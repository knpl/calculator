package com.knpl.simplecalculator.keyboard;

import java.util.List;

import android.R;
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
	
	public static final int FUNCTION_BLOCK_START = 61440;
	
	public static final String[] codeToText = {"sqrt(", "abs(", 			// 61440 - 61441
											   "exp(", "log(",				// 61442 - 61443
											   "sin(", "cos(", "tan(", 		// 61444 - 61446
											   "asin(", "acos(", "atan(", 	// 61447 - 61449
											   "sinh(", "cosh(", "tanh("}; 	// 61450 - 61452
	
	private Paint backgroundPaint,
				  pressedBackgroundPaint,
				  labelPaint,
				  smallLabelPaint;
	
	private Rect rect;

	public MyKeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPreviewEnabled(false);
		
		rect = new Rect();
		backgroundPaint = new Paint();
		pressedBackgroundPaint = new Paint();
		labelPaint = new Paint();
		smallLabelPaint = new Paint();
		
		backgroundPaint.setStyle(Paint.Style.FILL);
		backgroundPaint.setColor(Color.DKGRAY);
		
		pressedBackgroundPaint.setStyle(Paint.Style.FILL);
		pressedBackgroundPaint.setColor(Color.rgb(128, 128, 255));
		
		labelPaint.setTextAlign(Paint.Align.CENTER);
		labelPaint.setStyle(Paint.Style.STROKE);
		labelPaint.setTextSize(20);
		labelPaint.setColor(Color.WHITE);
		
		smallLabelPaint.setTextAlign(Paint.Align.RIGHT);
		smallLabelPaint.setStyle(Paint.Style.STROKE);
		smallLabelPaint.setTextSize(12);
		smallLabelPaint.setColor(Color.LTGRAY);
		
	}
	
	public String getTextFromCode(int primaryCode) {
		String result;
		
		if (FUNCTION_BLOCK_START <= primaryCode &&
			primaryCode <= FUNCTION_BLOCK_START + codeToText.length) {
			primaryCode -= FUNCTION_BLOCK_START;
			result = codeToText[primaryCode];
		}
		else {
			if (isShifted())
				result = "" + (char) Character.toUpperCase(primaryCode);
			else
				result = "" + (char) primaryCode;
		}
		return result;
	}

	@Override
	public void onDraw(Canvas canvas) {
		List<Key> keys = getKeyboard().getKeys();
		int yoffset = (int) (-labelPaint.ascent()/2f);
		Paint paint;
		
		for (Key key : keys) {
			rect.left = key.x + 1;
			rect.right = key.x + key.width - 1;
			rect.top = key.y + 1;
			rect.bottom = key.y + key.height - 1;
			int xc = key.x + key.width/2;
			int yc = key.y + key.height/2;
			Drawable icon = key.icon;

			paint = key.pressed ? pressedBackgroundPaint : backgroundPaint;
			canvas.drawRect(rect, paint);
			
			// Draw center label
			if (icon != null) {
				int w = icon.getIntrinsicWidth();
				int h = icon.getIntrinsicHeight();
				rect.left = xc - w/2;
				rect.right = rect.left + w;
				rect.top = yc - h/2;
				rect.bottom = rect.top + h;
				icon.setBounds(rect);
				icon.draw(canvas);
			}
			else if (key.label != null) {
				canvas.drawText(key.label.toString(), xc, yc  + yoffset, labelPaint);
			}
			else {
				char code = (char) key.codes[0];
				if (isShifted())
					code = Character.isLetter(code) ? Character.toUpperCase(code) : (char) code;
				canvas.drawText(""+code, xc, yc + yoffset, labelPaint);	
			}
			
			// Draw corner labels
			String upperleftLabel = null,
				   lowerleftLabel = null;
			if (key.codes.length > 1) {
				upperleftLabel = getTextFromCode(key.codes[1]);
				canvas.drawText(upperleftLabel, rect.right - 2,
						rect.top - smallLabelPaint.ascent() + 2, smallLabelPaint);
				if (key.codes.length > 2) {
					upperleftLabel = getTextFromCode(key.codes[2]);
					canvas.drawText(lowerleftLabel, rect.right - 2, 
							rect.bottom - smallLabelPaint.descent() - 2, smallLabelPaint);
				}
				continue;
			}
			
			if (key.popupResId == 0) { // No popup
				continue;
			}
			else if (key.popupResId != com.knpl.simplecalculator.R.xml.popup) {
				canvas.drawText("...", rect.right - 2, rect.bottom - smallLabelPaint.descent() - 2, smallLabelPaint);
				continue;
			}
			
			if (key.popupCharacters == null)
				continue;
			
			if (key.popupCharacters.length() > 1) {
				upperleftLabel = getTextFromCode(key.popupCharacters.charAt(1));
				canvas.drawText(upperleftLabel, rect.right - 2,
						rect.top - smallLabelPaint.ascent() + 2, smallLabelPaint);
				if (key.popupCharacters.length() > 2) {
					lowerleftLabel = getTextFromCode(key.popupCharacters.charAt(2));
					canvas.drawText(lowerleftLabel, rect.right - 2, 
							rect.bottom - smallLabelPaint.descent() - 2, smallLabelPaint);
				}
			}
			
		}
	}
	
}