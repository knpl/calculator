package com.knpl.simplecalculator.keyboard;

import com.knpl.simplecalculator.SimpleCalculatorActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;


public class CalculatorKeyboard {
	
	public static final int QWERTY_TOGGLE_KEY = 57344,
							GREEK_TOGGLE_KEY  = 57345,
							ACTION_KEY = 57346;
	
	
	private SimpleCalculatorActivity activity;

	private final Keyboard qwertyKbd;
	private final Keyboard greekKbd;
	private final Keyboard calculatorKbd;
	
	private final MyKeyboardView kbdv;
	
	public CalculatorKeyboard(SimpleCalculatorActivity activity, int kbdvResId, int qwertyResId, int greekResId, int calcResId) {
		this.activity = activity;

		qwertyKbd = new Keyboard(activity, qwertyResId);
		greekKbd = new Keyboard(activity, greekResId);
		calculatorKbd = new Keyboard(activity, calcResId);
		
		kbdv = (MyKeyboardView) activity.findViewById(kbdvResId);
		kbdv.setOnKeyboardActionListener(new CalculatorOnKeyboardActionListener());
		
		kbdv.setKeyboard(calculatorKbd);
	}

	private class CalculatorOnKeyboardActionListener implements OnKeyboardActionListener {
		
		@Override public void onPress(int primaryCode) {}

		@Override public void onRelease(int primaryCode) {}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			switch (primaryCode) {
			case QWERTY_TOGGLE_KEY:
				handleQwerty();
				break;
			case GREEK_TOGGLE_KEY:
				handleGreek();
				break;
			case ACTION_KEY:
				handleAction();
				break;
			case Keyboard.KEYCODE_SHIFT:
				handleShift();
				break;
			case Keyboard.KEYCODE_DELETE:
				handleDelete();
				break;
			default:
				handleCode(primaryCode, keyCodes);
			}
		}

		@Override public void onText(CharSequence text) {}

		@Override public void swipeLeft() {}

		@Override public void swipeRight() {}

		@Override public void swipeDown() {}

		@Override public void swipeUp() {}
	}
	
	private void handleShift() {
		if (kbdv.getKeyboard() == calculatorKbd) {
			return;
		}
		kbdv.setShifted(!kbdv.isShifted());
	}
	
	private void handleQwerty() {
		if (kbdv.getKeyboard() == qwertyKbd) {
			kbdv.setKeyboard(calculatorKbd);
		}
		else {
			kbdv.setKeyboard(qwertyKbd);
		}
		kbdv.setShifted(false);
	}
	
	public void handleGreek() {
		if (kbdv.getKeyboard() == greekKbd) {
			kbdv.setKeyboard(calculatorKbd);
		}
		else {
			kbdv.setKeyboard(greekKbd);
		}
		kbdv.setShifted(false);
	}
	
	private void handleAction() {
		View focus = activity.getWindow().getCurrentFocus();
		if (focus == null || !(focus instanceof EditText)) {
			return;
		}
		EditText editText = (EditText) focus;
		editText.onEditorAction(0);
	}
	
	private void handleDelete() {
		View focus = activity.getWindow().getCurrentFocus();
		if (focus == null || !(focus instanceof EditText)) {
			return;
		}
		EditText editText = (EditText) focus;
		Editable editable = editText.getText();
		
		int start = editText.getSelectionStart();
		int end   = editText.getSelectionEnd();
		
		if (start == end && start != 0) {
			start -= 1;
		}
		editable.delete(start, end);
	}
	
	private void handleCode(int primaryCode, int[] keyCodes) {
		View focus = activity.getWindow().getCurrentFocus();
		if (focus == null || !(focus instanceof EditText)) {
			return;
		}
		EditText editText = (EditText) focus;
		Editable editable = editText.getText();
		
		int start = editText.getSelectionStart();
		int end   = editText.getSelectionEnd();
		
		editable.replace(start, end, kbdv.getTextFromCode(primaryCode));
		
		if (kbdv.isShifted()) {
			kbdv.setShifted(false);
		}
	}
	
	public boolean isKeyboardVisible() {
    	return kbdv.getVisibility() == View.VISIBLE;
    }
    
    public void showKeyboard(View v) {
    	activity.hideSoftKeyboard(v);
    	if (isKeyboardVisible()) {
    		return;
    	}
    	
		kbdv.setVisibility(View.VISIBLE);
		kbdv.setEnabled(true);
		
		kbdv.animate()
			.translationY(0)
			.alpha(1)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
				}
			});
    }
    
    public void hideKeyboard() {
    	if (!isKeyboardVisible()) {
    		return;
    	}
		kbdv.animate()
			.translationY(kbdv.getHeight())
			.alpha(0)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					kbdv.setVisibility(View.GONE);
	    			kbdv.setEnabled(false);
				}
			});
    }
    
    public void registerEditText(EditText input) {
    	
    	input.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getActionMasked() == MotionEvent.ACTION_UP) {
					v.performClick();
				}
				EditText edit = (EditText) v;
				
				edit.setSelection(edit.getOffsetForPosition(event.getX(), event.getY()));
				return true;
			}
		});
		
		input.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showKeyboard(v);
			}
		});
		
		input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					showKeyboard(v);
				else
					hideKeyboard();
			}
		});
    }
}
