package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;

public abstract class Func extends Expr {
	protected final FuncDef definition;
	
	public Func(FuncDef definition) {
		this.definition = definition;
	}
	
	public FuncDef getFuncDef() {
		return definition;
	}
	
	public String getName() {
		return definition.getSignature().getName();
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
