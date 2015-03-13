package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;

public class Num extends Expr {
	private String token;
	
	public Num(String token) {
		this.token = token;
	}

	public double getDouble() {
		return Double.parseDouble(token);
	}

	@Override
	public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
		return v.visit(this, info);
	}
}
