package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;


public class Mul extends BinOp {
	public Mul(Expr lhs, Expr rhs) {
		super(lhs, rhs);
	}

	@Override
	public String getOpString() {
		return "*";
	}
	
	@Override
	public Object accept(Visitor v, Object info) throws Exception {
		return v.visit(this, info);
	}
}