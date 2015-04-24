package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.util.MyNumber;
import com.knpl.simplecalculator.visitors.ComplexEvaluate;
import com.knpl.simplecalculator.visitors.PrettyPrint;
import com.knpl.simplecalculator.visitors.Resolve;
import com.knpl.simplecalculator.visitors.Visitor;

public class UserConstDef extends ConstDef {
	
	private Expr expression;
	private Complex value;

	private UserConstDef(String name, Expr expression, String description) {
		super(name, description);
		this.expression = expression;
		this.value = null;
	}
	
	public static UserConstDef fromConstDefNode(ConstDefNode constDefNode) throws Exception {
		Resolve resolve = new Resolve();
		constDefNode = (ConstDefNode) constDefNode.accept(resolve);
		
		if (!resolve.getFreeVarMap().isEmpty()) {
			throw new Exception("Constant expression contains free variables");
		}
		
		PrettyPrint pp = new PrettyPrint();
		Expr expr = constDefNode.getExpression();
		expr.accept(pp);
		
		return new UserConstDef(constDefNode.getName(), expr, pp.toString());
	}
	
	public Expr getExpression() {
		return expression;
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
	public MyNumber getNumber() {
		return getComplex();
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
