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
	public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
		return v.visit(this, info);
	}
}
