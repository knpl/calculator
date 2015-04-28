package com.knpl.calc.nodes;

import com.knpl.calc.visitors.Visitor;

public class Mod extends BinOp {

	public Mod(Expr lhs, Expr rhs) {
		super(lhs, rhs);
	}

	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
