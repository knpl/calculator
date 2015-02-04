package com.knpl.simplecalculator.nodes;

import java.util.List;

import com.knpl.simplecalculator.GlobalDefinitions;
import com.knpl.simplecalculator.SimpleCalculatorActivity;
import com.knpl.simplecalculator.UserFuncDef;
import com.knpl.simplecalculator.visitors.Resolve;
import com.knpl.simplecalculator.visitors.Visitor;

public class FuncDefNode extends Node {

	private Expr expression;
	
	private Signature sig;
	
	public FuncDefNode(String name, List<Var> parameters, Expr expression) {
		this.sig = new Signature(name, parameters);
		this.expression = expression;
	}
	
	public FuncDefNode(Signature sig, Expr expression) {
		this.sig = sig;
		this.expression = expression;
	}
	
	@Override
	public void execute(SimpleCalculatorActivity calculator) throws Exception {
		Resolve resolve = new Resolve();
		accept(resolve);
		
		UserFuncDef ufd = new UserFuncDef(sig, expression);
		ufd.compile();
		
		GlobalDefinitions defs = GlobalDefinitions.getInstance();
		if(!defs.putUserFuncDef(ufd)) {
			throw new Exception("Function \""+sig+"\" already defined");
		}
		
		calculator.print("Defined function \""+sig+"\"");
	}
	
//	public String getName() {
//		return sig.getName();
//	}
//	
//	public List<Var> getParameters() {
//		return sig.getParameters();
//	}
	
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
