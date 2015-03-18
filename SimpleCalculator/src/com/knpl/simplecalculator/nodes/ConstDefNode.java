package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.SimpleCalculatorActivity;
import com.knpl.simplecalculator.util.Globals;
import com.knpl.simplecalculator.visitors.PrettyPrint;
import com.knpl.simplecalculator.visitors.Resolve;
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
		if (defs.getConstant(name) != null) {
			throw new Exception("Constant \""+name+"\" already defined");
		}
		
		Resolve resolve = new Resolve();
		accept(resolve, null);
		
		if (!resolve.getFreeVarMap().isEmpty()) {
			throw new Exception("Constant expression contains free variables");
		}
		
		PrettyPrint pp = new PrettyPrint();
		accept(pp, null);
		
		if (defs.putConstDef(new UserConstDef(name, expression, pp.toString())))
			calculator.print("defined constant "+name);
		else
			calculator.print("constant "+name+" already defined");
	}

	@Override
	public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
		return v.visit(this, info);
	}
}
