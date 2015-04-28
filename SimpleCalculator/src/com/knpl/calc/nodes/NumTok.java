package com.knpl.calc.nodes;

import com.knpl.calc.visitors.Visitor;

public class NumTok extends Expr {
	private String token;
	
	public NumTok(String token) {
		this.token = token;
	}

	public RealDouble getRealDouble() {
		return new RealDouble(Double.parseDouble(token));
	}
	
	@Override
	public String toString() {
		return token;
	}

	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
