package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;


public class Minus extends MonOp {

	public Minus(Expr op) {
		super(op);
	}
	
	@Override
	public String getOpString() {
		return "-";
	}
	
	@Override
	public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
		return v.visit(this, info);
	}
	
}
