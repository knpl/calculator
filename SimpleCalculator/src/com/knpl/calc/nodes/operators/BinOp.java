package com.knpl.calc.nodes.operators;

import com.knpl.calc.nodes.Expr;
import com.knpl.calc.nodes.numbers.Num;
import com.knpl.calc.visitors.Visitor;

public abstract class BinOp extends Expr {
	protected Expr lhs, rhs;
	
	public BinOp(Expr lhs, Expr rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}
	
	public Expr getLHS() {
		return lhs;
	}
	
	public Expr getRHS() {
		return rhs;
	}
	
	public BinOp setLHS(Expr lhs) {
		this.lhs = lhs;
		return this;
	}
	
	public BinOp setRHS(Expr rhs) {
		this.rhs = rhs;
		return this;
	}
	
	public abstract Num numEvaluate(Num lhs, Num rhs);
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visitBinOp(this);
	}
}


