package com.knpl.calc.nodes.numbers;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.special.Gamma;

import com.knpl.calc.util.FormatUtils;
import com.knpl.calc.visitors.Visitor;

public class RealDouble extends Num {

	private double val;
	
	public RealDouble(double val) {
		this.val = val;
	}
	
	@Override
	public Num copy() {
		return new RealDouble(val);
	}
	
	@Override
	public Num add(Num a) {
		if (!(a instanceof RealDouble)) {
			return toComplex().add(a);
		}
		val += ((RealDouble)a).val;
		return this;
	}

	@Override
	public Num sub(Num a) {
		if (!(a instanceof RealDouble)) {
			return toComplex().sub(a);
		}
		val -= ((RealDouble)a).val;
		return this;
	}

	@Override
	public Num mul(Num a) {
		if (!(a instanceof RealDouble)) {
			return toComplex().mul(a);
		}
		val *= ((RealDouble)a).val;
		return this;
	}

	@Override
	public Num div(Num a) {
		if (!(a instanceof RealDouble)) {
			return toComplex().div(a);
		}
		val /= ((RealDouble)a).val;
		return this;
	}

	@Override
	public Num mod(Num a) {
		if (!(a instanceof RealDouble)) {
			return toComplex().mod(a);
		}
		double b = ((RealDouble) a).val;
		val -=  b * Math.floor(val / b);
		return this;
	}

	@Override
	public Num pow(Num a) {
		if (!(a instanceof RealDouble)) {
			return toComplex().pow(a);
		}
		if (val < 0) {
			return toComplex().pow(a);
		}
		
		val = Math.pow(val, ((RealDouble)a).val);
		return this;
	}

	@Override
	public Num neg() {
		val = -val;
		return this;
	}

	@Override
	public Num deg2rad() {
		val *= Math.PI/180.0;
		return this;
	}
	
	@Override
	public Num factorial() {
		val = Gamma.gamma(val + 1);
		return this;
	}

	@Override
	public Num max(Num a) {
		if (!(a instanceof RealDouble)) {
			return toComplex().max(a);
		}
		double b = ((RealDouble) a).val;
		val = (val >= b) ? val : b; 
		return this;
	}

	@Override
	public Num min(Num a) {
		if (!(a instanceof RealDouble)) {
			return toComplex().min(a);
		}
		double b = ((RealDouble) a).val;
		val = (val <= b) ? val : b; 
		return this;
	}

	@Override
	public Num floor() {
		val = Math.floor(val);
		return this;
	}

	@Override
	public Num ceil() {
		val = Math.ceil(val);
		return this;
	}

	@Override
	public Num sqrt() {
		if (val < 0) {
			return new Complex(0, Math.sqrt(-val));
		}
		val = Math.sqrt(val);
		return this;
	}

	@Override
	public Num abs() {
		val = Math.abs(val);
		return this;
	}

	@Override
	public Num exp() {
		val = Math.exp(val);
		return this;
	}

	@Override
	public Num log() {
		if (val < 0) {
			return new Complex(Math.log(-val), Math.PI);
		}
		val = Math.log(val);
		return this;
	}

	@Override
	public Num sin() {
		val = Math.sin(val);
		return this;
	}

	@Override
	public Num cos() {
		val = Math.cos(val);
		return this;
	}

	@Override
	public Num tan() {
		val = Math.tan(val);
		return this;
	}

	@Override
	public Num asin() {
		val = Math.asin(val);
		return this;
	}

	@Override
	public Num acos() {
		val = Math.acos(val);
		return this;
	}

	@Override
	public Num atan() {
		val = Math.atan(val);
		return this;
	}

	@Override
	public Num sinh() {
		val = Math.sinh(val);
		return this;
	}

	@Override
	public Num cosh() {
		val = Math.cosh(val);
		return this;
	}

	@Override
	public Num tanh() {
		val = Math.tanh(val);
		return this;
	}

	@Override
	public Num erf() {	
		val = Erf.erf(val);
		return this;
	}

	@Override
	public RealDouble gamma() {
		val = Gamma.gamma(val);
		return this;
	}

	@Override
	public RealDouble loggamma() {
		val = Gamma.logGamma(val);
		return this;
	}
	
	@Override
	public RealDouble re() {
		return this;
	}
	
	@Override
	public RealDouble im() {
		val = 0;
		return this;
	}
	
	@Override
	public RealDouble mod() {
		val = Math.abs(val);
		return this;
	}
	
	@Override
	public RealDouble arg() {
		if (val == 0)
			val = Double.NaN;
		else if (val < 0)
			val = Math.PI;
		else
			val = 0;
		return this;
	}
	
	@Override
	public RealDouble conj() {
		return this;
	}

	@Override
	public Complex toComplex() {
		return new Complex(val, 0);
	}
	
	public double getValue() {
		return val;
	}

	@Override
	public float toFloat() {
		return (float) val;
	}

	@Override
	public double toDouble() {
		return val;
	}

	@Override
	public String toString() {
		return Double.toString(val);
	}
	
	@Override
	public String format(int decimalcount, boolean polar) {
		return FormatUtils.format(val, decimalcount);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof RealDouble)) {
			return false;
		}
		else if (this == o) {
			return true;
		}
		else {
			RealDouble that = (RealDouble) o;
			return val == that.val;
		}
	}
	
	@Override
	public int hashCode() {
		long vallong = Double.doubleToLongBits(val);
		int result = 13;
		result = 37 * result + (int) (vallong ^ (vallong >>> 32));
		return result;
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visitRealDouble(this);
	}
}
