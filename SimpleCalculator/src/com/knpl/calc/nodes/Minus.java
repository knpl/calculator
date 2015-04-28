package com.knpl.calc.nodes;

import com.knpl.calc.visitors.Visitor;


public class Minus extends MonOp {

	public Minus(Expr op) {
		super(op);
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
	
}
