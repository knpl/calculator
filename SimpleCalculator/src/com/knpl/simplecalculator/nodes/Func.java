package com.knpl.simplecalculator.nodes;

import java.util.List;

import com.knpl.simplecalculator.visitors.Visitor;

public class Func extends Expr {
	final FuncDef definition;
	final List<Expr> arguments;
	
	public Func(FuncDef definition, List<Expr> arguments) {
		this.definition = definition;
		this.arguments = arguments;
	}
	
	public FuncDef getFuncDef() {
		return definition;
	}
	
	public List<Expr> getArguments() {
		return arguments;
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
