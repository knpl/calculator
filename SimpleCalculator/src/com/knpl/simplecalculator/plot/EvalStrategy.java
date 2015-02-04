package com.knpl.simplecalculator.plot;

import java.io.Serializable;

public interface EvalStrategy extends Serializable {
	public void execute(float[] data, Axis a);
}
