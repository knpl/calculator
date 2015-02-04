package com.knpl.simplecalculator;

import java.util.List;

import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.visitors.Compile;

public class UserFuncDef implements FunctionDefinition {
	
	private final Signature sig;
	private final Expr expression;
	private Program program;
	
	public UserFuncDef(String name, List<Var> parameters, Expr expression) {
		this.sig = new Signature(name, parameters);
		this.expression = expression;
		this.program = null;
	}
	
	public UserFuncDef(Signature sig, Expr expression) {
		this.sig = sig;
		this.expression = expression;
		this.program = null;
	}

	@Override
	public Func createFunction(Call call) throws Exception {
		if (!call.getName().equals(sig.getName())) {
			throw new Exception("Function name mismatch");
		}
		if (call.getArguments().size() != sig.getParameters().size()) {
			throw new Exception("Argument mismatch");
		}
		return new UserFunc(this, call.getArguments());
	}
	
	public Expr getExpression() {
		return expression;
	}

	@Override
	public Signature getSignature() {
		return sig;
	}
	
	public Program compile() throws Exception {
		if (program == null) {
			Compile c = new Compile();
			(new FuncDefNode(sig, expression)).accept(c);
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
