package com.knpl.calc.nodes.operators;

import com.knpl.calc.nodes.Expr;
import com.knpl.calc.nodes.numbers.Num;
import com.knpl.calc.visitors.Visitor;

public class DegToRad extends MonOp {

	public DegToRad(Expr op) {
		super(op);
	}

	@Override
	public Num numEvaluate(Num op) {
		return op.deg2rad();
	}

	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visitDegToRad(this);
	}
}
