package com.knpl.simplecalculator.plot;

import com.knpl.simplecalculator.Program;

public class ProgramXtoYMapper implements Mapper {

	private static final long serialVersionUID = 2595871216194630358L;
	
	private Program program;
	
	public ProgramXtoYMapper(Program program) {
		this.program = program;
	}
	
	@Override
	public void map(float[] data, Axis x, Axis y) {
		x.generate(data, 0, 2);
		program.evaluate(data, 1, 2, data, 0, 2);
	}

}
