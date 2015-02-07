package com.knpl.simplecalculator;


import java.util.ArrayList;

import com.knpl.simplecalculator.PlotMenuFragment.PlotEntry;
import com.knpl.simplecalculator.nodes.Node;
import com.knpl.simplecalculator.parser.Lexer;
import com.knpl.simplecalculator.parser.Parser;
import com.knpl.simplecalculator.plot.Axis;
import com.knpl.simplecalculator.plot.Mapper;
import com.knpl.simplecalculator.util.Pair;

import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class SimpleCalculatorActivity extends ActionBarActivity
	implements PlotOptionsFragment.OptionsListener,
			   PlotMenuFragment.PlotListener,
			   PlotEntryDialog.PlotEntryDialogListener
{
	
	public static final int MAIN_FRAGMENT_POSITION = 0,
							OPTIONS_FRAGMENT_POSITION = 1,
							PLOT_FRAGMENT_POSITION = 2,
							PLOTMENU_FRAGMENT_POSITION = 3;
	
	public static final String EXTRA_MESSAGE = "com.knpl.simplecalculator.EXTRA_MESSAGE";
	public static final Axis DEFAULT_AXIS = new Axis(-5, 5);
	
	private static MainFragment mainFragment = new MainFragment();
	private static PlotOptionsFragment optionsFragment = new PlotOptionsFragment();
	private static PlotMenuFragment plotmenuFragment = new PlotMenuFragment();
	private static Axis xaxis = DEFAULT_AXIS,
						yaxis = DEFAULT_AXIS;
	
	private static PlotFragment plotFragment = 
			PlotFragment.createPlotFragment(new ArrayList<Pair<Mapper, Integer>>(), xaxis, yaxis);
	
	private static int currentFragmentPosition;
	
	
	private String[] items;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private ListView drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
        		getSupportActionBar().setTitle(items[currentFragmentPosition]);
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
    			drawerListClick(parent, view, position, id);
    		}
        });
        
        if (savedInstanceState == null) {
	        GlobalDefinitions.getInstance();
	        setFragment(MAIN_FRAGMENT_POSITION, false);
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_MENU) {
    		if (drawerLayout.isDrawerOpen(drawerList)) {
    			drawerLayout.closeDrawer(drawerList);
    		}
    		else {
    			drawerLayout.openDrawer(drawerList);
    		}
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

	private void drawerListClick(AdapterView<?> parent, View view, int position, long id) {
    	setFragment(position, true);
		drawerList.setItemChecked(position, true);
		drawerLayout.closeDrawer(drawerList);
    }
    
    private void setFragment(int position, boolean addToBackStack) {
    	Fragment fragment;
		switch (position) {
			case MAIN_FRAGMENT_POSITION:
				fragment = mainFragment;
				break;
			case OPTIONS_FRAGMENT_POSITION:
				fragment = optionsFragment;
				break;
			case PLOT_FRAGMENT_POSITION:
				fragment = plotFragment;
				break;
			case PLOTMENU_FRAGMENT_POSITION:
				fragment = plotmenuFragment;
				break;
			default:
				fragment = mainFragment;
				position = MAIN_FRAGMENT_POSITION;
		}
		currentFragmentPosition = position;
		getSupportActionBar().setTitle(items[position]);
		
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
    	plotFragment = PlotFragment.createPlotFragment(mappers, xaxis, yaxis);
    	setFragment(PLOT_FRAGMENT_POSITION, true);
    }
   
    public void print(String s) {
    	TextView output = (TextView) findViewById(R.id.output);
    	output.setText(s);
    }

	@Override
	public void setXAxis(Axis x) {
		xaxis = x;
	}

	@Override
	public void setYAxis(Axis y) {
		yaxis = y;
	}
	
	@Override 
	public Axis getXAxis() {
		return xaxis;
	}
	
	@Override
	public Axis getYAxis() {
		return yaxis;
	}

	@Override
	public void addPlotEntry(PlotEntry entry) {
		plotmenuFragment.addPlotEntry(entry);
	}
	
	@Override
	public void setPlotEntry(int position, PlotEntry entry) {
		plotmenuFragment.setPlotEntry(position, entry);
	}

	@Override
	public void removePlotEntry(int position) {
		plotmenuFragment.removePlotEntry(position);
	}
}
