package com.knpl.simplecalculator.nodes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.knpl.simplecalculator.util.Program;
import com.knpl.simplecalculator.visitors.Compile;
import com.knpl.simplecalculator.visitors.ComplexEvaluate;
import com.knpl.simplecalculator.visitors.PrettyPrint;
import com.knpl.simplecalculator.visitors.Resolve;
import com.knpl.simplecalculator.visitors.Visitor;

public class UserFuncDef extends MVFuncDef {
	
	private final String description;
	private final Expr expression;
	private Program program;
	
	public UserFuncDef(FuncDefNode funcDefNode) throws Exception {
		super(funcDefNode.getSignature());
		
		Resolve resolve = new Resolve();
		funcDefNode.accept(resolve);
		
		Map<String, Var> freeVars = resolve.getFreeVarMap();
		if (!freeVars.isEmpty()) {
			throw new Exception("Resolve error: undeclared variables");
		}
		
		PrettyPrint prettyPrint = new PrettyPrint();
		funcDefNode.accept(prettyPrint);
		this.description = prettyPrint.toString();
		this.expression = funcDefNode.getExpression();
		this.program = null;
	}
	
	public Expr getExpression() {
		return expression;
	}
	
	@Override
	public String getDescription() {
		return description;
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

	@Override
	public Complex complexEvaluate(List<Complex> args) throws Exception {
		List<Var> params = sig.getParameters();
		if (params.size() != args.size()) {
			throw new Exception("Argument mismatch while evaluating function " + sig.getName());
		}
		
		Map<String, Complex> map  = new HashMap<String, Complex>(params.size());
		Iterator<Complex> itArg   = args.iterator();
		Iterator<Var>  	  itParam = params.iterator();
		while (itParam.hasNext()) {
			map.put(itParam.next().getName(), itArg.next());
		}
		
		return (Complex) expression.accept(new ComplexEvaluate(map));
	}

	@Override
	public Double evaluate(List<Double> args) throws Exception {
		return getProgram().evaluate(args);
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
