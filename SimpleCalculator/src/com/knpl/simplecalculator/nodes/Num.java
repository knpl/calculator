package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;

public class Num extends Expr {
	private double val;
	
	public Num() {
		val = Double.NaN;
	}
	
	public Num(double val) {
		this.val = val;
	}

	public double getValue() {
		return val;
	}

	public void setValue(double newVal) {
		val = newVal;
	}

	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
	
}
