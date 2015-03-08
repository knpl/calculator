package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.numbers.Complex;
import com.knpl.simplecalculator.visitors.Visitor;

public abstract class Constant extends Expr {
	
	public abstract String getName();
	public abstract double getDouble();
	public abstract Complex getComplex();
	
	@Override
	public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
		return v.visit(this, info);
	}

}
