package com.knpl.calc.nodes;

import com.knpl.calc.visitors.Visitor;


public class Mul extends BinOp {
	public Mul(Expr lhs, Expr rhs) {
		super(lhs, rhs);
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}