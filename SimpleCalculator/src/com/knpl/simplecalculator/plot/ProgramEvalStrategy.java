package com.knpl.simplecalculator.plot;

import com.knpl.simplecalculator.Program;

public class ProgramEvalStrategy implements EvalStrategy {

	private static final long serialVersionUID = 2595871216194630358L;
	
	private Program program;
	
	public ProgramEvalStrategy(Program program) {
		this.program = program;
	}
	
	@Override
	public void execute(float[] data, Axis a) {
		a.generate(data, 0, 2);
		program.evaluate(data, 1, 2, data, 0, 2);
	}

}
