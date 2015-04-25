package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.visitors.NumEvaluate;
import com.knpl.simplecalculator.visitors.PrettyPrint;
import com.knpl.simplecalculator.visitors.Resolve;
import com.knpl.simplecalculator.visitors.Visitor;

public class UserConstDef extends ConstDef {
	private Expr expression;
	private Num val;
	private boolean resolved;

	private UserConstDef(String name, Expr expression, String description) {
		super(name, description);
		this.expression = expression;
		val = null;
		resolved = false;
	}
	
	public static UserConstDef fromConstDefNode(ConstDefNode cdn) throws Exception {
//		Resolve resolve = new Resolve();
//		cdn = (ConstDefNode) cdn.accept(resolve);
//		
//		if (!resolve.getFreeVarMap().isEmpty()) {
//			throw new Exception("Constant expression contains free variables");
//		}
		
		PrettyPrint pp = new PrettyPrint();
		cdn.accept(pp);
		
		return new UserConstDef(cdn.getName(), cdn.getExpression(), pp.toString());
	}
	
	public Expr getExpression() {
		return expression;
	}
	
	public UserConstDef setExpression(Expr expression) {
		this.expression = expression;
		return this;
	}
	
	private void eval() throws Exception {
		if (!resolved) {
			Resolve resolve = new Resolve();
			expression = (Expr) expression.accept(resolve);
			if (resolve.getFreeVarMap().size() != 0) {
				throw new Exception("Unable to evaluate constant. Free variables not allowed.");
			}
			resolved = true;
		}
		val = (Num) expression.accept(new NumEvaluate());
	}
	
	@Override
	public Num getNum() throws Exception {
		if (val == null) {
			eval();
		}
		return val.copy();
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
