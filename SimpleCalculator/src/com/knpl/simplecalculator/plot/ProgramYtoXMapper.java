package com.knpl.simplecalculator.plot;

import com.knpl.simplecalculator.util.Program;

public class ProgramYtoXMapper implements Mapper {
	private static final long serialVersionUID = 2595871216194630358L;
	
	private Program program;
	
	public ProgramYtoXMapper(Program program) {
		this.program = program;
	}
	
	@Override
	public void map(float[] data, Axis x, Axis y) {
		y.generate(data, 1, 2);
		program.evaluate(data, 0, 2, data, 1, 2);
	}
}
