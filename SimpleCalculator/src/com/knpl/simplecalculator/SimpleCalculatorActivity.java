package com.knpl.simplecalculator;


import java.util.ArrayList;

import com.knpl.simplecalculator.nodes.Node;
import com.knpl.simplecalculator.numbers.Complex;
import com.knpl.simplecalculator.parser.Lexer;
import com.knpl.simplecalculator.parser.Parser;
import com.knpl.simplecalculator.plot.Range;
import com.knpl.simplecalculator.plot.Mapper;
import com.knpl.simplecalculator.util.FormatUtils;
import com.knpl.simplecalculator.util.Pair;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class SimpleCalculatorActivity extends ActionBarActivity
	implements PlotOptionsFragment.OptionsListener,
			   PlotMenuFragment.PlotListener
{
	
	public static final String packagePrefix = "com.knpl.simplecalculator.";
	
	public static final int MAIN_FRAGMENT_POSITION = 0,
							OPTIONS_FRAGMENT_POSITION = 1,
							PLOTMENU_FRAGMENT_POSITION = 2,
							FUNCDEF_FRAGMENT_POSITION = 3,
							PREFERENCES_FRAGMENT_POSITION = 4;
	
	public static final int N_DECIMALS = 10,
							BASE = 10;
	
	public static final String EXTRA_MESSAGE = packagePrefix+"EXTRA_MESSAGE";
	public static final Range DEFAULT_AXIS = new Range(-5, 5);
	
	private static Range xaxis = DEFAULT_AXIS,
						yaxis = DEFAULT_AXIS;
	
	private Fragment[] drawerFragments;
	private String[] items;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private ListView drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        setContentView(R.layout.activity_calculator);
        
        items = getResources().getStringArray(R.array.listitems);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
        		this, drawerLayout,
        		R.string.drawerOpen, R.string.drawerClose
        		) {
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
        		new CalculatorPreferenceFragment()
        };
        
        if (savedInstanceState == null) {	
        	setFragment(drawerFragments[MAIN_FRAGMENT_POSITION], false);
        }
    }

	@Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_MENU) {
    		closeSoftInput();
    		if (drawerLayout.isDrawerOpen(drawerList)) {
    			drawerLayout.closeDrawer(drawerList);
    		}
    		else {
    			drawerLayout.openDrawer(drawerList);
    		}
    		return true;
    	}
    	else if (keyCode == KeyEvent.KEYCODE_BACK) {
    		if (drawerLayout.isDrawerOpen(drawerList)) {
    			drawerLayout.closeDrawer(drawerList);
    			return true;
    		}
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
    public void closeSoftInput() {
    	View v = getCurrentFocus();
		if (v != null) {
			InputMethodManager input = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			input.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
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
    
    public void enter(View view) {
    	EditText input = (EditText) findViewById(R.id.input);
    	Parser parser = new Parser(new Lexer(input.getText().toString()));
    	if (!parser.start()) {
    		print("Syntax error");
    		return;
    	}
    	
    	Node parseResult = parser.getResult();
    	
    	try {
	    	parseResult.execute(this);
    	}
    	catch (Exception e) {
    		print("Caught exception: "+e.getMessage());
    		e.printStackTrace();
    	}
    }
    
    public void plot(ArrayList<Pair<Mapper, Integer>> mappers) {
    	PlotFragment fragment = PlotFragment.createPlotFragment(mappers, xaxis, yaxis);
    	setFragment(fragment, false);
    }
   
    public void print(String s) {
    	TextView output = (TextView) findViewById(R.id.output);
    	output.setText(s);
    }
    
    public void print(Complex z) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	int ndecimals = prefs.getInt(CalculatorPreferenceFragment.PREF_KEY_PRECISION, 10);
    	boolean polar = prefs.getBoolean("pref_key_complex_polar", false);
    	
    	TextView output = (TextView) findViewById(R.id.output);
    	output.setText(FormatUtils.format(z, ndecimals, BASE, polar));
    }
    
    public void print(double d) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	int ndecimals = prefs.getInt(CalculatorPreferenceFragment.PREF_KEY_PRECISION, 10);
    	
    	TextView output = (TextView) findViewById(R.id.output);
    	output.setText(FormatUtils.format(d, ndecimals, BASE));
    }
    
	@Override
	public void setXAxis(Range x) {
		xaxis = x;
	}

	@Override
	public void setYAxis(Range y) {
		yaxis = y;
	}
	
	@Override 
	public Range getXAxis() {
		return xaxis;
	}
	
	@Override
	public Range getYAxis() {
		return yaxis;
	}
}
