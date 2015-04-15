package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.Visitor;

public class SVFunc extends Func {
	protected Expr argument;
	
	public SVFunc(SVFuncDef definition, Expr argument) {
		super(definition);
		this.argument = argument;
	}
	
	public Expr getArgument() {
		return argument;
	}
	
	public SVFunc setArgument(Expr argument) {
		this.argument = argument;
		return this;
	}
	
	public SVFuncDef getSVFuncDef() {
		return (SVFuncDef) definition;
	}

	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
