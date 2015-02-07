package com.knpl.simplecalculator.plot;

import com.knpl.simplecalculator.util.Program;

public class ProgramParametricMapper implements Mapper {

	private static final long serialVersionUID = 2595871216194630358L;
	
	private Program xMapper;
	private Program yMapper;
	private Axis t;
	
	public ProgramParametricMapper(Program xMapper, Program yMapper, Axis t) {
		this.xMapper = xMapper;
		this.yMapper = yMapper;
		this.t = t;
	}
	
	@Override
	public void map(float[] data, Axis xaxis, Axis yaxis) {
		t.generate(data, 0, 1);
		xMapper.evaluate(data, 0, 2, data, 0, 2);
		yMapper.evaluate(data, 1, 2, data, 1, 2);
		
	}

}
