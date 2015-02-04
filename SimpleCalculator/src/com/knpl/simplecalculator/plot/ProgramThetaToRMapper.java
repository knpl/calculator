package com.knpl.simplecalculator.plot;

import com.knpl.simplecalculator.Program;

public class ProgramThetaToRMapper implements Mapper {

	private static final long serialVersionUID = 2595871216194630358L;
	
	private Program program;
	private Axis t;
	
	public ProgramThetaToRMapper(Program program, Axis t) {
		this.program = program;
		this.t = t;
	}
	
	@Override
	public void map(float[] data, Axis xaxis, Axis yaxis) {
		t.generate(data, 0, 2);
		program.evaluate(data, 1, 2, data, 0, 2);
		
		double theta, r;
		for (int i = 0; i + 1 < data.length; i += 2) {
			theta = data[i];
			r = data[i+1];
			data[i] = (float)(r*Math.cos(theta));
			data[i+1] = (float)(r*Math.sin(theta));
		}
	}

}
