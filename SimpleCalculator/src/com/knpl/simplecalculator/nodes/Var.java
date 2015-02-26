package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;

public class Var extends Expr {
	private final String name;

	public Var(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public Object accept(Visitor v, Object info) throws Exception {
		return v.visit(this, info);
	}
}
