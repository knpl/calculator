package com.knpl.simplecalculator.plot;

import java.io.Serializable;

import android.graphics.Path;

public interface PathGenerator extends Serializable {
	public Path generatePath(Axis x, Axis y);
}
