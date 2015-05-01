package com.knpl.calc.nodes.operators;

import com.knpl.calc.nodes.Expr;
import com.knpl.calc.nodes.numbers.Num;
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
	
	public abstract Num numEvaluate(Num op);

	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visitMonOp(this);
	}
}
