package com.knpl.simplecalculator.nodes;

import java.util.List;
import com.knpl.simplecalculator.nodes.Expr;
import com.knpl.simplecalculator.nodes.MVFunc;
import com.knpl.simplecalculator.util.UserFuncDef;
import com.knpl.simplecalculator.visitors.Visitor;

public class UserFunc extends MVFunc {

	public UserFunc(UserFuncDef definition, List<Expr> arguments) {
		super(definition, arguments);
	}

	public UserFuncDef getUserFuncDef() {
		return (UserFuncDef) definition;
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
