package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;

public abstract class Num extends Expr {
	public abstract Num add(Num a);
	public abstract Num sub(Num a);
	public abstract Num mul(Num a);
	public abstract Num div(Num a);
	public abstract Num mod(Num a);
	public abstract Num pow(Num a);
	public abstract Num neg();
	public abstract Num deg2rad();
	public abstract Num factorial();
	public abstract Num max(Num a);
	public abstract Num min(Num a);
	public abstract Num floor();
	public abstract Num ceil();
	public abstract Num sqrt();
	public abstract Num abs();
	public abstract Num exp();
	public abstract Num log();
	public abstract Num sin();
	public abstract Num cos();
	public abstract Num tan();
	public abstract Num asin();
	public abstract Num acos();
	public abstract Num atan();
	public abstract Num sinh();
	public abstract Num cosh();
	public abstract Num tanh();
	public abstract Num erf();
	public abstract Num gamma();
	public abstract Num loggamma();
	public abstract Complex toComplex();
	
	public abstract Num copy();
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
