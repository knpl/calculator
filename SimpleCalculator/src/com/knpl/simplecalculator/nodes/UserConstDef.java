package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.NumEvaluate;
import com.knpl.simplecalculator.visitors.PrettyPrint;
import com.knpl.simplecalculator.visitors.Resolve;
import com.knpl.simplecalculator.visitors.Visitor;

public class UserConstDef extends ConstDef {
	
	private Expr expression;
	private Num val;

	private UserConstDef(String name, Expr expression, String description) {
		super(name, description);
		this.expression = expression;
		this.val = null;
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
	
	@Override
	public Num getNum() {
		if (val == null) {
			try {
				NumEvaluate evaluate = new NumEvaluate();
				val = (Num) expression.accept(evaluate);
			}
			catch (Exception ex) {
				val = new RealDouble(Double.NaN);
			}
		}
		return val;
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
