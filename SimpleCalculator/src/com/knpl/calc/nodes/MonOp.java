package com.knpl.calc.nodes;

import com.knpl.calc.visitors.Visitor;


public abstract class MonOp extends Expr {
	protected Expr op;
	
	public MonOp(Expr op) {
		this.op  = op;
	}

	public Expr getOp() {
		return op;
	}
	
	public void setOp(Expr op) {
		this.op = op; 
	}

	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
