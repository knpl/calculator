package com.knpl.calc.plot;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;

public class PlotRenderer implements Renderer {

	private final String vsource, fsource;
	
	private float width, height;
	private Range xrange, yrange;
	
	private int modelh, viewh, projh;
	private float[] model;
	private float[] view;
	private float[] proj;
	
	private int posh;
	
	private static final int SIZEOF_FLOAT = 4;
	
	private int[] vbos;
	
	public PlotRenderer(String vsource, String fsource) {
		this.vsource = vsource;
		this.fsource = fsource;
		
		width = height = 0;
		xrange = yrange = null;

		model = new float[16];
		view = new float[16];
		proj = new float[16];
		
		vbos = null;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(.2f, .2f, .2f, 1f);
		GLES20.glLineWidth(3);
		
		int vsh = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		if (vsh == 0) {
			throw new RuntimeException("Could not create vertex shader");
		}
		GLES20.glShaderSource(vsh, vsource);
		GLES20.glCompileShader(vsh);
		int[] status = new int[1];
		GLES20.glGetShaderiv(vsh, GLES20.GL_COMPILE_STATUS, status, 0);
		if (status[0] == 0) {
			String message = GLES20.glGetShaderInfoLog(vsh);
			GLES20.glDeleteShader(vsh);
			throw new RuntimeException("Could not compile vertex shader: "+message);
		}
		
		int fsh = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		if (fsh == 0) {
			throw new RuntimeException("Could not create fragment shader");
		}
		GLES20.glShaderSource(fsh, fsource);
		GLES20.glCompileShader(fsh);
		GLES20.glGetShaderiv(fsh, GLES20.GL_COMPILE_STATUS, status, 0);
		if (status[0] == 0) {
			String message = GLES20.glGetShaderInfoLog(fsh);
			GLES20.glDeleteShader(fsh);
			throw new RuntimeException("Could not compile fragment shader: "+message);
		}
		
		int ph = GLES20.glCreateProgram();
		if (ph == 0) {
			throw new RuntimeException("Could not create program");
		}
		GLES20.glAttachShader(ph, vsh);
		GLES20.glAttachShader(ph, fsh);
		GLES20.glBindAttribLocation(ph, 0, "pos");
		GLES20.glLinkProgram(ph);
		GLES20.glGetProgramiv(ph, GLES20.GL_LINK_STATUS, status, 0);
		if (status[0] == 0) {
			GLES20.glDeleteProgram(ph);
			throw new RuntimeException("Could not link program");
		}
		posh = GLES20.glGetAttribLocation(ph, "pos");
		
		modelh = GLES20.glGetUniformLocation(ph, "model");
		viewh = GLES20.glGetUniformLocation(ph, "view");
		projh = GLES20.glGetUniformLocation(ph, "proj");
		
		GLES20.glUseProgram(ph);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.width = width;
		this.height = height;
		
		GLES20.glViewport(0, 0, width, height);
		
		float left, right, bottom, top;
		float ar = (float)width/height;
		float padding;
		if (width > height) {
			padding = .5f*(ar-1);
			left = -padding;
			right = 1 + padding;
			bottom = 0;
			top = 1;
		}
		else {
			padding = .5f*(1/ar - 1);
			left = 0;
			right = 1;
			bottom = -padding;
			top = 1 + padding;
		}
		Matrix.orthoM(proj, 0, left, right, bottom, top, 0, 10);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		
		Matrix.setIdentityM(model, 0);
		Matrix.setLookAtM(view, 0, 
						  0, 0, 1,
						  0, 0, 0,
						  0, 1, 0);
		
		GLES20.glUniformMatrix4fv(modelh, 1, false, model, 0);
		GLES20.glUniformMatrix4fv(viewh, 1, false, view, 0);
		GLES20.glUniformMatrix4fv(projh, 1, false, proj, 0);
	}
	
	public void initVBOs(int n) {
		vbos = new int[n];
		GLES20.glGenBuffers(n, vbos, 0);
	}
	
	public void bufferData(int index, Buffer data) {
		data.position(0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbos[index]);
		GLES20.glBufferData(vbos[index], data.capacity() * SIZEOF_FLOAT, data, GLES20.GL_STATIC_DRAW);
	}
	
	public void setView(Range xrange, Range yrange) {
		this.xrange = xrange;
		this.yrange = yrange;
	}
	
//	private static class VBOInfo {
//		public final int vbo;
//		public final int color;
//		
//		public VBOInfo(int vbo, int color) {
//			this.vbo = vbo;
//			this.color = color;
//		}
//	}
}
