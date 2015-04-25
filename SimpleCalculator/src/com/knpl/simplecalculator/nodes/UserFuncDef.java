package com.knpl.simplecalculator.nodes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.knpl.simplecalculator.parser.Lexer;
import com.knpl.simplecalculator.parser.Parser;
import com.knpl.simplecalculator.util.Program;
import com.knpl.simplecalculator.visitors.Compile;
import com.knpl.simplecalculator.visitors.NumEvaluate;
import com.knpl.simplecalculator.visitors.PrettyPrint;
import com.knpl.simplecalculator.visitors.Resolve;
import com.knpl.simplecalculator.visitors.Visitor;

public class UserFuncDef extends FuncDef {
	private Expr expression;
	private Program program;
	private boolean resolved;
	
	private UserFuncDef(Signature sig, Expr expr, String description) throws Exception {
		super(sig, description);
		this.expression = expr;
		this.program = null;
		resolved = false;
	}
	
	public static UserFuncDef fromSource(String source) throws Exception {
		Parser parser = new Parser(new Lexer(source));
		if (!parser.funcDef())
			throw new Exception("Syntax error");
		return fromFuncDefNode((FuncDefNode) parser.getResult());
	}
	
	public static UserFuncDef fromFuncDefNode(FuncDefNode fdn) throws Exception {
		PrettyPrint prettyPrint = new PrettyPrint();
		fdn.accept(prettyPrint);
		return new UserFuncDef(
				fdn.getSignature(), fdn.getExpression(), prettyPrint.toString());
	}
	
	public Expr getExpression() {
		return expression;
	}
	
	private void compile() throws Exception {
		if (program != null) {
			return;
		}
		
		if (!resolved) {
			android.util.Log.d("mytag", "resolving now");
			 
			Resolve resolve = new Resolve();
			sig.accept(resolve);
			expression = (Expr) expression.accept(resolve);
			if (resolve.getFreeVarMap().size() != 0) {
				throw new Exception("Unable to compile function. Free variables not allowed.");
			}
			resolved = true;
		}
		
		Compile compile = new Compile();
		(new FuncDefNode(sig, expression)).accept(compile);
		program = compile.getProgram();
	}
	
	public Program getProgram() throws Exception {
		if (program == null) {
			compile();
		}
		return program;
	}

	@Override
	public Num numEvaluate(List<Num> args) throws Exception {
		List<Var> params = sig.getParameters();
		if (params.size() != args.size()) {
			throw new Exception("Argument mismatch while evaluating function " + sig.getName());
		}
		
		Map<String, Num> map  = new HashMap<String, Num>(params.size());
		Iterator<Num> itArg   = args.iterator();
		Iterator<Var> itParam = params.iterator();
		while (itParam.hasNext()) {
			map.put(itParam.next().getName(), itArg.next());
		}
		
		return (Num) expression.accept(new NumEvaluate(map));
	}

	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
