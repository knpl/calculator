package com.knpl.calc.plot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.knpl.calc.plot.XtoYComputerMapper.MapperListener;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class PlotGLSurfaceView extends GLSurfaceView implements OnTouchListener, MapperListener {
	
	private PlotRenderer renderer;
	
	private List<Mapper> mappers;
	private Range xrange,
				  yrange;

	public PlotGLSurfaceView(Context context) {
		super(context);
		ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        ConfigurationInfo ci = am.getDeviceConfigurationInfo();
        if (ci.reqGlEsVersion < 0x20000) {
        	throw new RuntimeException("OpenGL ES 2.0 not supported");
        }
        setEGLContextClientVersion(2);
        
        AssetManager asm = context.getAssets();
    	try {
	        String vsource = readFile(asm.open("vertex.vs"));
	        String fsource = readFile(asm.open("fragment.fs"));
	        renderer = new PlotRenderer(vsource, fsource);
    	}
    	catch (IOException ex) {
    		ex.printStackTrace();
    		throw new RuntimeException("Failed to read shaders");
    	}
		setRenderer(renderer);
	}
	
	private static String readFile(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        
        while (true) {
        	int bytes = is.read(buffer);
        	if (bytes == -1) {
        		break;
        	}
        	baos.write(buffer, 0, bytes);
        }
        
		return baos.toString("UTF-8");
	}
	
	public void setValues(List<Mapper> paths, Range xaxis, Range yaxis) {
		this.mappers = (paths == null) ? new ArrayList<Mapper>() : paths;
		this.xrange = (xaxis == null) ? new Range(-5, 5) : xaxis;
		this.yrange = (yaxis == null) ? new Range(-5, 5) : yaxis;
		renderer.initVBOs(mappers.size());
		
		int i = 0;
		Iterator<Mapper> iter = paths.iterator();
		while (iter.hasNext()) {
			Mapper mapper = iter.next();
			if (mapper instanceof XtoYComputerMapper) {
				((XtoYComputerMapper) mapper).setListener(this, i);
			}
			i++;
		}
		
	}
	
	@Override
	public void update(float[] buffer, int id) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
			v.performClick();
		}
		return true;
	}
	
	@Override
	public boolean performClick() {
		return super.performClick();
	}
}
