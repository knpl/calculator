package com.knpl.simplecalculator.util;

import java.util.List;

import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.visitors.Compile;

public class UserFuncDef extends FuncDef {
	
	private final Signature sig;
	private final String description;
	private final Expr expression;
	private Program program;
	
	public UserFuncDef(Signature sig, Expr expression, String description) {
		this.sig = sig;
		this.description = description;
		this.expression = expression;
		this.program = null;
	}

	@Override
	public Func createFunction(Call call) throws Exception {
		if (!call.match(sig))
			throw new Exception("Signature mismatch");
		return new UserFunc(this, call.getArguments());
	}
	
	@Override
	public Func createFunction(Signature sig, List<Expr> args) throws Exception {
		if (!this.sig.match(sig))
			throw new Exception("Signature mismatch");
		return new UserFunc(this, args);
	}
	
	public Expr getExpression() {
		return expression;
	}
	
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Signature getSignature() {
		return sig;
	}
	
	public Program compile() throws Exception {
		if (program == null) {
			Compile c = new Compile();
			(new FuncDefNode(sig, expression)).accept(c, null);
			program = c.getProgram();
		}
		return program;
	}
	
	public Program getProgram() throws Exception {
		if (program == null) {
			program = compile();
		}
		return program;
	}
}
