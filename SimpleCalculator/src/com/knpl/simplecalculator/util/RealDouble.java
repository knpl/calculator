package com.knpl.simplecalculator.util;

import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.special.Gamma;

import com.knpl.simplecalculator.nodes.Complex;

public class RealDouble implements MyNumber {

	private double val;
	
	public RealDouble(double val) {
		this.val = val;
	}
	
	@Override
	public MyNumber add(MyNumber a) {
		if (!(a instanceof RealDouble))
			return toComplex().add(a);
		val += ((RealDouble)a).val;
		return this;
	}

	@Override
	public MyNumber sub(MyNumber a) {
		if (!(a instanceof RealDouble))
			return toComplex().sub(a);
		val -= ((RealDouble)a).val;
		return this;
	}

	@Override
	public MyNumber mul(MyNumber a) {
		if (!(a instanceof RealDouble))
			return toComplex().mul(a);
		val *= ((RealDouble)a).val;
		return this;
	}

	@Override
	public MyNumber div(MyNumber a) {
		if (!(a instanceof RealDouble))
			return toComplex().div(a);
		val /= ((RealDouble)a).val;
		return this;
	}

	@Override
	public MyNumber mod(MyNumber a) {
		if (!(a instanceof RealDouble))
			return toComplex().mod(a);
		double b = ((RealDouble) a).val;
		val -=  b * Math.floor(val / b);
		return this;
	}

	@Override
	public MyNumber pow(MyNumber a) {
		if (!(a instanceof RealDouble))
			return toComplex().pow(a);
		
		if (val < 0)
			return toComplex().pow(a);
		
		val = Math.pow(val, ((RealDouble)a).val);
		return this;
	}

	@Override
	public MyNumber neg() {
		val = -val;
		return this;
	}

	@Override
	public MyNumber deg2rad() {
		val *= 180.0/Math.PI;
		return this;
	}
	
	@Override
	public MyNumber factorial() {
		val = Gamma.gamma(val + 1);
		return this;
	}

	@Override
	public MyNumber max(MyNumber a) {
		if (!(a instanceof RealDouble))
			return toComplex().max(a);
		double b = ((RealDouble) a).val;
		val = val >= b ? val : b; 
		return this;
	}

	@Override
	public MyNumber min(MyNumber a) {
		if (!(a instanceof RealDouble))
			return toComplex().min(a);
		double b = ((RealDouble) a).val;
		val = val >= b ? val : b; 
		return this;
	}

	@Override
	public MyNumber floor() {
		val = Math.floor(val);
		return this;
	}

	@Override
	public MyNumber ceil() {
		val = Math.ceil(val);
		return this;
	}

	@Override
	public MyNumber sqrt() {
		if (val < 0)
			return toComplex().sqrt();
		val = Math.sqrt(val);
		return this;
	}

	@Override
	public MyNumber abs() {
		val = Math.abs(val);
		return this;
	}

	@Override
	public MyNumber exp() {
		val = Math.exp(val);
		return this;
	}

	@Override
	public MyNumber log() {
		if (val < 0)
			return toComplex().log();
		val = Math.log(val);
		return this;
	}

	@Override
	public MyNumber sin() {
		val = Math.sin(val);
		return this;
	}

	@Override
	public MyNumber cos() {
		val = Math.cos(val);
		return this;
	}

	@Override
	public MyNumber tan() {
		val = Math.tan(val);
		return this;
	}

	@Override
	public MyNumber asin() {
		val = Math.asin(val);
		return this;
	}

	@Override
	public MyNumber acos() {
		val = Math.acos(val);
		return this;
	}

	@Override
	public MyNumber atan() {
		val = Math.atan(val);
		return this;
	}

	@Override
	public MyNumber sinh() {
		val = Math.sinh(val);
		return this;
	}

	@Override
	public MyNumber cosh() {
		val = Math.cosh(val);
		return this;
	}

	@Override
	public MyNumber tanh() {
		val = Math.tanh(val);
		return this;
	}

	@Override
	public MyNumber erf() {
		val = Erf.erf(val);
		return this;
	}

	@Override
	public MyNumber gamma() {
		val = Gamma.gamma(val);
		return this;
	}

	@Override
	public MyNumber loggamma() {
		val = Gamma.logGamma(val);
		return this;
	}

	@Override
	public Complex toComplex() {
		return new Complex(val, 0);
	}
	
	public double getValue() {
		return val;
	}
}
