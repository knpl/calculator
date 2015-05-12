package com.knpl.calc;

import java.util.ArrayList;
import com.knpl.calc.R;
import com.knpl.calc.keyboard.CalculatorKeyboard;
import com.knpl.calc.plot.Mapper;
import com.knpl.calc.plot.Range;
import com.knpl.calc.storage.CalculatorDb;

import android.app.Activity;
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
import android.widget.ListView;

public class SimpleCalculatorActivity extends ActionBarActivity {
	
	public static final int MAIN_FRAGMENT_POSITION = 0,
							OPTIONS_FRAGMENT_POSITION = 1,
							PLOTMENU_FRAGMENT_POSITION = 2,
							FUNCDEF_FRAGMENT_POSITION = 3,
							CONSTDEF_FRAGMENT_POSITION = 4,
							PREFERENCES_FRAGMENT_POSITION = 5;
	
	public String[] fragmentNames = {
		"com.knpl.calc.MainFragment",
		"com.knpl.calc.PlotOptionsFragment",
		"com.knpl.calc.PlotMenuFragment",
		"com.knpl.calc.FuncDefFragment",
		"com.knpl.calc.ConstDefFragment",
		"com.knpl.calc.CalculatorPreferenceFragment"
	};
	
	private static Range xaxis, yaxis;
	
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
        
        keyboard = new CalculatorKeyboard(this, R.id.keyboard, 
        		R.xml.qwerty_keyboard, R.xml.greek_keyboard, R.xml.calculator_keyboard);
		
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
        		setFragment(fragmentNames[position]);
        		drawerList.setItemChecked(position, true);
        		drawerLayout.closeDrawer(drawerList);
    		}
        });
        
        if (savedInstanceState == null) {
        	setFragment(fragmentNames[MAIN_FRAGMENT_POSITION]);
        	CalculatorDb.loadUserDefs();
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
	        if (drawerLayout.isDrawerOpen(drawerList)) {
				drawerLayout.closeDrawer(drawerList);
				return true;
	        }
    		if (keyboard.isKeyboardVisible()) {
    	        keyboard.hideKeyboard();
    	        return true;
    		}
    	}
    	
    	return super.onKeyDown(keyCode, event);
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.simple_calculator_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}
    
    private void setFragment(String fragmentName) {
    	FragmentManager fm = getSupportFragmentManager();
    	FragmentTransaction ft = fm.beginTransaction();
    	Fragment frag = fm.findFragmentByTag(fragmentName);
    	
    	if (frag == null)  {
    		frag = Fragment.instantiate(this, fragmentName);
    	}
    	
    	ft.replace(R.id.content_frame, frag);
    	ft.commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
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
    
    public void plot(ArrayList<Mapper> mappers) {
    	PlotFragment fragment = PlotFragment.createPlotFragment(mappers, xaxis, yaxis);
    	FragmentManager fm = getSupportFragmentManager();
    	FragmentTransaction ft = fm.beginTransaction();
    	ft.replace(R.id.content_frame, fragment);
    	ft.commit();
    }
    
    public void plot3d(ArrayList<Mapper> mappers) {
    	GLPlotFragment fragment = GLPlotFragment.createGLPlotFragment(mappers, xaxis, yaxis);
    	FragmentManager fm = getSupportFragmentManager();
    	FragmentTransaction ft = fm.beginTransaction();
    	ft.replace(R.id.content_frame, fragment);
    	ft.commit();
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
