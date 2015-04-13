package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;


public class Sub extends BinOp {
	public Sub(Expr lhs, Expr rhs) {
		super(lhs, rhs);
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
