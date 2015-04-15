package com.knpl.simplecalculator.nodes;

import java.util.List;

import com.knpl.simplecalculator.visitors.Visitor;

public class MVFunc extends Func {
	protected final List<Expr> arguments;
	
	public MVFunc(MVFuncDef definition, List<Expr> arguments) {
		super(definition);
		this.arguments = arguments;
	}
	
	public List<Expr> getArguments() {
		return arguments;
	}
	
	public MVFuncDef getMVFuncDef() {
		return (MVFuncDef) definition;
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
