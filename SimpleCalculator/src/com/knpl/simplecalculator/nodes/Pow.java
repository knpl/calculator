package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;


public class Pow extends BinOp {
	public Pow(Expr lhs, Expr rhs) {
		super(lhs, rhs);
	}

	@Override
	public String getOpString() {
		return "^";
	}
	
	@Override
	public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
		return v.visit(this, info);
	}
}
