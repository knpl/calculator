package com.knpl.simplecalculator.util;

import java.util.List;
import java.util.Map;

import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.visitors.Compile;
import com.knpl.simplecalculator.visitors.PrettyPrint;
import com.knpl.simplecalculator.visitors.Resolve;

public class UserFuncDef extends FuncDef {
	
	private final Signature sig;
	private final String description;
	private final Expr expression;
	private Program program;
	
	public UserFuncDef(FuncDefNode funcDefNode, boolean userDefsAllowed) throws Exception {
		Resolve resolve = new Resolve();
		funcDefNode.accept(resolve, null);
		
		Map<String, Var> freeVars = resolve.getFreeVarMap();
		if (!freeVars.isEmpty()) {
			throw new Exception("Resolve error: undeclared variables");
		}
		
		if (!userDefsAllowed && (
				resolve.getUFDDependencies().size() != 0 ||
				resolve.getUCDDependencies().size() != 0)) {
				throw new Exception("Resolve error: user-defined definitions not allowed");
		}
		
		PrettyPrint prettyPrint = new PrettyPrint();
		funcDefNode.accept(prettyPrint, null);
		this.description = prettyPrint.toString();
		
		
		this.sig = funcDefNode.getSignature();
		this.expression = funcDefNode.getExpression();
		this.program = null;
	}
	
	public UserFuncDef(FuncDefNode funcDefNode) throws Exception {
		this(funcDefNode, true);
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
