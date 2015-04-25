package com.knpl.simplecalculator;

import java.util.ArrayList;

import com.knpl.simplecalculator.keyboard.CalculatorKeyboard;
import com.knpl.simplecalculator.keyboard.MyKeyboardView;
import com.knpl.simplecalculator.nodes.Node;
import com.knpl.simplecalculator.nodes.Num;
import com.knpl.simplecalculator.parser.Lexer;
import com.knpl.simplecalculator.parser.Parser;
import com.knpl.simplecalculator.plot.Range;
import com.knpl.simplecalculator.plot.Mapper;
import com.knpl.simplecalculator.util.Globals;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SimpleCalculatorActivity extends ActionBarActivity {
	
	public static final String packagePrefix = "com.knpl.simplecalculator.";
	
	public static final int MAIN_FRAGMENT_POSITION = 0,
							OPTIONS_FRAGMENT_POSITION = 1,
							PLOTMENU_FRAGMENT_POSITION = 2,
							FUNCDEF_FRAGMENT_POSITION = 3,
							CONSTDEF_FRAGMENT_POSITION = 4,
							PREFERENCES_FRAGMENT_POSITION = 5;
	
	public static final int N_DECIMALS = 10;
	
	public static final String EXTRA_MESSAGE = packagePrefix+"EXTRA_MESSAGE";
	public static final Range DEFAULT_AXIS = new Range(-5, 5);
	
	private static Range xaxis = DEFAULT_AXIS,
						 yaxis = DEFAULT_AXIS;
	
	private Fragment[] drawerFragments;
	private String[] items;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private ListView drawerList;
	
	private CalculatorKeyboard keyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_calculator);
        
        final LinearLayout container = (LinearLayout) findViewById(R.id.container);
        
        keyboard = new CalculatorKeyboard(this, R.id.keyboard, 
        		R.xml.qwerty_keyboard, R.xml.greek_keyboard, R.xml.calculator_keyboard);
        
        final MyKeyboardView kbdv = keyboard.getKeyboardView();
        LayoutTransition trans = container.getLayoutTransition();
		if (trans != null) {
			trans.disableTransitionType(LayoutTransition.APPEARING);
			trans.setAnimator(LayoutTransition.DISAPPEARING, 
					ObjectAnimator.ofFloat(kbdv, "alpha", 1f, 0f));
			trans.disableTransitionType(LayoutTransition.CHANGE_APPEARING);
			trans.disableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
	        container.setLayoutTransition(trans);
		}
		
        items = getResources().getStringArray(R.array.listitems);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
        		this, drawerLayout, R.string.drawerOpen, R.string.drawerClose) {
        	@Override
        	public void onDrawerClosed(View v) {
        		super.onDrawerClosed(v);
        		getSupportActionBar().setTitle("Calculator");
        	}
        	
        	@Override
        	public void onDrawerOpened(View v) {
        		super.onDrawerOpened(v);
        		getSupportActionBar().setTitle("Select activity");
        	}
        };
        drawerLayout.setDrawerListener(drawerToggle);
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        
        drawerList = (ListView) findViewById(R.id.left_drawer);
        
        drawerList.setAdapter(new ArrayAdapter<String>(this,
        		android.R.layout.simple_list_item_1, items));
        drawerList.setOnItemClickListener( new ListView.OnItemClickListener() {
        	@Override
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		setFragment(drawerFragments[position], false);
        		drawerList.setItemChecked(position, true);
        		drawerLayout.closeDrawer(drawerList);
    		}
        });
        
        drawerFragments = new Fragment[] {
    		new MainFragment(),
    		new PlotOptionsFragment(),
    		new PlotMenuFragment(),
    		new FuncDefFragment(),
    		new ConstDefFragment(),
    		new CalculatorPreferenceFragment()
        };
        
        if (savedInstanceState == null) {
        	setFragment(drawerFragments[MAIN_FRAGMENT_POSITION], false);
        	Globals.getInstance();
        }
    }
    
	public boolean isKeyboardVisible() {
    	return keyboard.isKeyboardVisible();
    }
    
    public void showKeyboard(View v) {
		keyboard.showKeyboard(v);
    }
    
    public void hideKeyboard() {
		keyboard.hideKeyboard();
    }
    
    public void hideSoftKeyboard(View v) {
    	if (v != null) {
    		((InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(v.getWindowToken(), 0);
    	}
    }
    
    public void registerEditTextToKeyboard(EditText editText) {
    	keyboard.registerEditText(editText);
    }
   
	@Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    }
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		if (keyboard.isKeyboardVisible()) 
    	        keyboard.hideKeyboard();
	        if (drawerLayout.isDrawerOpen(drawerList))
				drawerLayout.closeDrawer(drawerList);
	        return true;
    	}
    	
    	return super.onKeyDown(keyCode, event);
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.simple_calculator_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}
    
    private void setFragment(Fragment fragment, boolean addToBackStack) {
		FragmentManager fm = getSupportFragmentManager();
    	FragmentTransaction ft = fm.beginTransaction();
    	ft.replace(R.id.content_frame, fragment);
    	if (addToBackStack)
    		ft.addToBackStack(null);
    	ft.commit();
    }    
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void enter(String inputString) {
    	try {
    		Parser parser = new Parser(new Lexer(inputString));
        	if (!parser.start()) {
        		print("Syntax error");
        		return;
        	}
        	
    		Node parseResult = parser.getResult();
	    	parseResult.execute(this);
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    		print("Caught exception: "+ex.getMessage());
    	}
    }
    
    public void plot(ArrayList<Mapper> mappers) {
    	PlotFragment fragment = PlotFragment.createPlotFragment(mappers, xaxis, yaxis);
    	setFragment(fragment, false);
    }
   
    public void print(String s) {
    	TextView output = (TextView) findViewById(R.id.output);
    	output.setText(s);
    }
    
    public void print(Num n) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	int ndecimals = prefs.getInt(CalculatorPreferenceFragment.PREF_KEY_PRECISION, 10);
    	boolean polar = prefs.getBoolean("pref_key_complex_polar", false);
    	
    	TextView output = (TextView) findViewById(R.id.output);
    	output.setText(n.format(ndecimals, polar));
    }
    
	public void setXAxis(Range x) {
		xaxis = x;
	}

	public void setYAxis(Range y) {
		yaxis = y;
	}
	 
	public Range getXAxis() {
		return xaxis;
	}
	
	public Range getYAxis() {
		return yaxis;
	}
}
