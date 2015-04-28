package com.knpl.calc.nodes;

import com.knpl.calc.visitors.Visitor;


public class Add extends BinOp {
	
	public Add(Expr lhs, Expr rhs) {
		super(lhs, rhs);
	}

	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
