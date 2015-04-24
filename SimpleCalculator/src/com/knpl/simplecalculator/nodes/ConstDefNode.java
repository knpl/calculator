package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.SimpleCalculatorActivity;
import com.knpl.simplecalculator.util.Globals;
import com.knpl.simplecalculator.visitors.Visitor;

public class ConstDefNode extends Node {
	private String name;
	private Expr expression;
	
	public ConstDefNode(String name, Expr expression) {
		this.name = name;
		this.expression = expression;
	}

	public String getName() {
		return name;
	}
	
	public Expr getExpression() {
		return expression;
	}
	
	public ConstDefNode setExpression(Expr expression) {
		this.expression = expression;
		return this;
	}
	
	@Override
	public void execute(SimpleCalculatorActivity calculator) throws Exception {
		Globals defs = Globals.getInstance();
		if (defs.getConstDef(name) != null) {
			throw new Exception("Constant "+name+" already defined");
		}
		
		defs.putUserConstDef(UserConstDef.fromConstDefNode(this));
	}

	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
