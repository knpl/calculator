package com.knpl.simplecalculator;

import android.app.Application;
import android.content.Context;

public class CalculatorApplication extends Application {
	
	private static Context context;
	
    public void onCreate(){
    	context = getApplicationContext();
    }

    public static Context getCalculatorApplicationContext() {
    	return context;
    } 
}
