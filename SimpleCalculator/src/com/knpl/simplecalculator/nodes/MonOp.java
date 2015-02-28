package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;


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
	
	public abstract String getOpString();
	
	@Override
	public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
		return v.visit(this, info);
	}
}
