package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.SimpleCalculatorActivity;
import com.knpl.simplecalculator.util.Globals;
import com.knpl.simplecalculator.visitors.Visitor;

public class FuncDefNode extends Node {

	private Expr expression;
	private Signature sig;
	
	public FuncDefNode(Signature sig, Expr expression) {
		this.sig = sig;
		this.expression = expression;
	}
	
	@Override
	public void execute(SimpleCalculatorActivity calculator) throws Exception {
		Globals defs = Globals.getInstance();
		if (defs.getFuncDef(sig.getName()) != null) {
			throw new Exception("Function \""+sig+"\" already defined");
		}
		
		UserFuncDef ufd = new UserFuncDef(this);
		ufd.compile();
		
		defs.putFuncDef(ufd);
		
		calculator.print(""+ufd.getProgram());
	}
	
	public Signature getSignature() {
		return sig;
	}
	
	public Expr getExpression() {
		return expression;
	}
	
	public void setExpression(Expr expression) {
		this.expression = expression;
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
