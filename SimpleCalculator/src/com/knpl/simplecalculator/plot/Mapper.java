package com.knpl.simplecalculator.plot;

import java.io.Serializable;

import android.graphics.Matrix;
import android.graphics.Path;

public interface Mapper extends Serializable {
	public Path map(Matrix ctm, Range xrange, Range yrange);
	public void initialize();
}
