package com.knpl.calc.nodes;

import com.knpl.calc.visitors.Visitor;

public class Var extends Expr {
	private final String name;

	public Var(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
