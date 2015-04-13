package com.knpl.simplecalculator.nodes;

import java.util.List;

import com.knpl.simplecalculator.visitors.Visitor;

public class MVFunc extends Func {
	protected final List<Expr> arguments;
	
	public MVFunc(FuncDef definition, List<Expr> arguments) {
		super(definition);
		this.arguments = arguments;
	}
	
	public List<Expr> getArguments() {
		return arguments;
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
