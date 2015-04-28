package com.knpl.calc.nodes;

import com.knpl.calc.visitors.Visitor;

public class Factorial extends MonOp {

	public Factorial(Expr op) {
		super(op);
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
