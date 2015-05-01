package com.knpl.calc.nodes.operators;

import com.knpl.calc.nodes.Expr;
import com.knpl.calc.nodes.numbers.Num;
import com.knpl.calc.visitors.Visitor;

public class Mul extends BinOp {
	public Mul(Expr lhs, Expr rhs) {
		super(lhs, rhs);
	}

	@Override
	public Num numEvaluate(Num lhs, Num rhs) {
		return lhs.mul(rhs);
	}

	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visitMul(this);
	}
}