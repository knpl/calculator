package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;

public abstract class ConstDef extends Expr {
	
	public abstract String getName();
	public abstract double getDouble();
	public abstract Complex getComplex();
	public abstract String getDescription();
	
	@Override
	public String toString() {
		return getDescription();
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
