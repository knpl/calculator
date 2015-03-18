package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.numbers.Complex;
import com.knpl.simplecalculator.visitors.ComplexEvaluate;

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
			value = expression.accept(evaluate, null);
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
		return value;
	}
}
