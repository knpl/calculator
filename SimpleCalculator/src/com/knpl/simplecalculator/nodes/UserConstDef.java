package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.ComplexEvaluate;
import com.knpl.simplecalculator.visitors.PrettyPrint;
import com.knpl.simplecalculator.visitors.Resolve;
import com.knpl.simplecalculator.visitors.Visitor;

public class UserConstDef extends ConstDef {
	
	private String name;
	private Expr expression;
	private String description;
	private Complex value;

	public UserConstDef(String name, Expr expression, String description) {
		this.name = name;
		this.expression = expression;
		this.description = description;
		this.value = null;
	}
	
	public UserConstDef(ConstDefNode constDefNode) throws Exception {
		Resolve resolve = new Resolve();
		constDefNode.accept(resolve);
		
		if (!resolve.getFreeVarMap().isEmpty()) {
			throw new Exception("Constant expression contains free variables");
		}
		
		PrettyPrint pp = new PrettyPrint();
		constDefNode.accept(pp);
		
		this.name = constDefNode.getName();
		this.expression = constDefNode.getExpression();
		this.description = pp.toString();
		this.value = null;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public Expr getExpression() {
		return expression;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	private void eval() {
		try {
			ComplexEvaluate evaluate = new ComplexEvaluate();
			value = (Complex) expression.accept(evaluate);
		}
		catch (Exception ex) {
			value = new Complex(Double.NaN, Double.NaN);
		}
	}

	@Override
	public double getDouble() {
		if (value == null)
			eval();
		return (value.im() == 0.0) ? value.re() : Double.NaN;
	}

	@Override
	public Complex getComplex() {
		if (value == null)
			eval();
		return new Complex(value);
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
