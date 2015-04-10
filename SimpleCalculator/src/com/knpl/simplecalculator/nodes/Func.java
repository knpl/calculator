package com.knpl.simplecalculator.nodes;

import java.util.List;

import com.knpl.simplecalculator.util.FuncDef;
import com.knpl.simplecalculator.visitors.Visitor;

public abstract class Func extends Expr {
	protected FuncDef definition;
	
	public abstract List<Expr> getArguments();

	public String getName() {
		return definition.getSignature().getName();
	}
	
	public FuncDef getDefinition() {
		return definition;
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
