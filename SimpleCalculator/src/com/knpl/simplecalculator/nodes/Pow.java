package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;


public class Pow extends BinOp {
	public Pow(Expr lhs, Expr rhs) {
		super(lhs, rhs);
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}