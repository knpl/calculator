package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;


public class Div extends BinOp {
	
	public Div(Expr lhs, Expr rhs) {
		super(lhs, rhs);
	}
	
	@Override
	public String getOpString() {
		return "/";
	}
	
	@Override
	public Object accept(Visitor v, Object info) throws Exception {
		return v.visit(this, info);
	}
}
