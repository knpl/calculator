package com.knpl.calc.nodes;

import com.knpl.calc.visitors.Visitor;

public abstract class Num extends Expr {
	public Num add(Num a) {
		throw new ArithmeticException("Not implemented");
	}
	public Num sub(Num a) {
		throw new ArithmeticException("Not implemented");
	}
	public Num mul(Num a) {
		throw new ArithmeticException("Not implemented");
	}
	public Num div(Num a) {
		throw new ArithmeticException("Not implemented");
	}
	public Num mod(Num a) {
		throw new ArithmeticException("Not implemented");
	}
	public Num pow(Num a) {
		throw new ArithmeticException("Not implemented");
	}
	public Num neg() {
		throw new ArithmeticException("Not implemented");
	}
	public Num deg2rad() {
		throw new ArithmeticException("Not implemented");
	}
	public Num factorial() {
		throw new ArithmeticException("Not implemented");
	}
	public Num max(Num a) {
		throw new ArithmeticException("Not implemented");
	}
	public Num min(Num a) {
		throw new ArithmeticException("Not implemented");
	}
	public Num floor() {
		throw new ArithmeticException("Not implemented");
	}
	public Num ceil() {
		throw new ArithmeticException("Not implemented");
	}
	public Num sqrt() {
		throw new ArithmeticException("Not implemented");
	}
	public Num abs() {
		throw new ArithmeticException("Not implemented");
	}
	public Num exp() {
		throw new ArithmeticException("Not implemented");
	}
	public Num log() {
		throw new ArithmeticException("Not implemented");
	}
	public Num sin() {
		throw new ArithmeticException("Not implemented");
	}
	public Num cos() {
		throw new ArithmeticException("Not implemented");
	}
	public Num tan() {
		throw new ArithmeticException("Not implemented");
	}
	public Num asin() {
		throw new ArithmeticException("Not implemented");
	}
	public Num acos() {
		throw new ArithmeticException("Not implemented");
	}
	public Num atan() {
		throw new ArithmeticException("Not implemented");
	}
	public Num sinh() {
		throw new ArithmeticException("Not implemented");
	}
	public Num cosh() {
		throw new ArithmeticException("Not implemented");
	}
	public Num tanh() {
		throw new ArithmeticException("Not implemented");
	}
	public Num erf() {
		throw new ArithmeticException("Not implemented");
	}
	public Num gamma() {
		throw new ArithmeticException("Not implemented");
	}
	public Num loggamma() {
		throw new ArithmeticException("Not implemented");
	}
	public Num re() {
		throw new ArithmeticException("Not implemented");
	}
	public Num im() {
		throw new ArithmeticException("Not implemented");
	}
	public Num mod() {
		throw new ArithmeticException("Not implemented");
	}
	public Num arg() {
		throw new ArithmeticException("Not implemented");
	}
	public Num conj() {
		throw new ArithmeticException("Not implemented");
	}
	
	public abstract Complex toComplex();
	public abstract Num copy();
	public abstract String format(int decimalcount, boolean polar);
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
