package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;


public abstract class BinOp extends Expr {
	protected Expr ops[];
	
	public BinOp(Expr lhs, Expr rhs) {
		ops = new Expr[2];
		ops[0] = lhs;
		ops[1] = rhs;
	}
	
	public Expr getLHS() {
		return ops[0];
	}
	
	public Expr getRHS() {
		return ops[1];
	}
	
	public BinOp setLHS(Expr lhs) {
		ops[0] = lhs;
		return this;
	}
	
	public BinOp setRHS(Expr rhs) {
		ops[1] = rhs;
		return this;
	}
	
	abstract public String getOpString();
	
	@Override
	public Object accept(Visitor v, Object info) throws Exception {
		return v.visit(this, info);
	}
}


