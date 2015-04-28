package com.knpl.calc.nodes;

import com.knpl.calc.visitors.Visitor;

public class Const extends Expr {
	private final ConstDef def;
	
	public Const(ConstDef def) {
		this.def = def;
	}
	
	public ConstDef getConstDef() {
		return def;
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
