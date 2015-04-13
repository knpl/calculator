package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;

public class DegToRad extends MonOp {

	public DegToRad(Expr op) {
		super(op);
	}

	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
