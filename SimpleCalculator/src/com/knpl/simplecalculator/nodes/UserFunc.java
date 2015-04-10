package com.knpl.simplecalculator.nodes;

import java.util.ArrayList;
import java.util.List;
import com.knpl.simplecalculator.nodes.Expr;
import com.knpl.simplecalculator.nodes.Func;
import com.knpl.simplecalculator.util.UserFuncDef;
import com.knpl.simplecalculator.visitors.Visitor;

public class UserFunc extends Func {
	private final List<Expr> arguments;
	
	public UserFunc(UserFuncDef definition, List<Expr> arguments) {
		this.definition = definition;
		this.arguments = new ArrayList<Expr>(arguments);
	}

	@Override
	public List<Expr> getArguments() {
		return arguments;
	}
	
	@Override
	public UserFuncDef getDefinition() {
		return (UserFuncDef) definition;
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
