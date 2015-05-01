package com.knpl.calc.nodes.defs;

import java.util.List;

import com.knpl.calc.nodes.Expr;
import com.knpl.calc.visitors.Visitor;

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
		return v.visitFunc(this);
	}
}
