package com.knpl.simplecalculator.plot;

import java.io.Serializable;

public interface Mapper extends Serializable {
	public void map(float[] data, Axis x, Axis y);
}
