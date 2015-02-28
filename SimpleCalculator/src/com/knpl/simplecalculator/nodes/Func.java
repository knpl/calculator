package com.knpl.simplecalculator.nodes;

import java.util.List;

import com.knpl.simplecalculator.util.FunctionDefinition;
import com.knpl.simplecalculator.visitors.Visitor;

public abstract class Func extends Expr {
	protected FunctionDefinition definition;
	
	public abstract List<Expr> getArguments();

	public String getName() {
		return definition.getSignature().getName();
	}
	
	public abstract Expr getArg(int i);
	
	public FunctionDefinition getDefinition() {
		return definition;
	}
	
	@Override
	public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
		return v.visit(this, info);
	}
}
