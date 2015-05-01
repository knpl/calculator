package com.knpl.calc.nodes.defs;

import com.knpl.calc.nodes.Expr;
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
		return v.visitConst(this);
	}
}
