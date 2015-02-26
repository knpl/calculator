package com.knpl.simplecalculator.nodes;

import com.knpl.simplecalculator.SimpleCalculatorActivity;
import com.knpl.simplecalculator.util.GlobalDefinitions;
import com.knpl.simplecalculator.util.UserFuncDef;
import com.knpl.simplecalculator.visitors.PrettyPrint;
import com.knpl.simplecalculator.visitors.Resolve;
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
		GlobalDefinitions defs = GlobalDefinitions.getInstance();
		if (defs.getFunctionDefinition(sig.getName()) != null) {
			throw new Exception("Function \""+sig+"\" already defined");
		}
		
		Resolve resolve = new Resolve();
		accept(resolve, null);
		
		PrettyPrint prettyPrint = new PrettyPrint();
		expression.accept(prettyPrint, null);
		
		UserFuncDef ufd = new UserFuncDef(sig, prettyPrint.toString(), expression);
		ufd.compile();
		
		defs.putUserFuncDef(ufd);
		
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
	public Object accept(Visitor v, Object info) throws Exception {
		return v.visit(this, info);
	}
}
