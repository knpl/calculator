package com.knpl.calc.nodes.defs;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.knpl.calc.MainInterface;
import com.knpl.calc.nodes.Expr;
import com.knpl.calc.nodes.Var;
import com.knpl.calc.nodes.numbers.Num;
import com.knpl.calc.parser.Lexer;
import com.knpl.calc.parser.Parser;
import com.knpl.calc.storage.CalculatorDb;
import com.knpl.calc.util.Globals;
import com.knpl.calc.util.Program;
import com.knpl.calc.visitors.Compile;
import com.knpl.calc.visitors.Fold;
import com.knpl.calc.visitors.NumEvaluate;
import com.knpl.calc.visitors.PrettyPrint;
import com.knpl.calc.visitors.Resolve;
import com.knpl.calc.visitors.Visitor;

public class UserFuncDef extends FuncDef {
	private Expr expression;
	private String source;
	private Program program;
	private boolean resolved;
	
	public UserFuncDef(Signature sig, Expr expr) {
		super(sig);
		this.expression = expr;
		this.source = "";
		this.program = null;
		resolved = false;
	}
	
	public static UserFuncDef fromSource(String source) throws Exception {
		Parser parser = new Parser(new Lexer(source));
		if (!parser.funcDef()) {
			throw new Exception("Syntax error");
		}
		return (UserFuncDef) parser.getResult();
	}
	
	public Expr getExpression() {
		return expression;
	}
	
	public UserFuncDef setExpression(Expr expression) {
		this.expression = expression;
		return this;
	}
	
	public void resolve(Map<String, UserFuncDef> ufdMap,
						Map<String, UserConstDef> ucdMap) throws Exception {
		if (resolved) {
			return;
		}
		source = PrettyPrint.printUserFuncDef(this);
		expression = Resolve.resolveUserFuncDef(this, ufdMap, ucdMap);
		resolved = true;
	}
	
	public void resolve() throws Exception {
		if (resolved) {
			return;
		}
		source = PrettyPrint.printUserFuncDef(this);
		expression = Resolve.resolveUserFuncDef(this);
		resolved = true;
	}
	
	private void compile() throws Exception {
		if (program != null) {
			return;
		}
		resolve();
		expression = (Expr) expression.accept(new Fold());
		program = Compile.compileUserFuncDef(this);
	}
	
	public Program getProgram() throws Exception {
		compile();
		return program;
	}

	@Override
	public Num getNum(List<Num> args) throws Exception {
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
	
	public String getSource() throws Exception {
		resolve();
		return source;
	}
	
	@Override
	public void execute(MainInterface interfaze) throws Exception {
		Globals globals = Globals.getInstance();
		if (globals.getFuncDef(sig.getName()) != null) {
			throw new Exception("Function "+sig.getName()+" exists already.");
		}
		Program program = getProgram();
		globals.putUserFuncDef(this);
		CalculatorDb.insertUFD(this);
		interfaze.print(program.toString(), false);
	}

	@Override
	public String toString() {
		try {
			resolve();
			return source;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return sig+" = invalid (Reason: "+ex.getMessage()+")";
		}
	}

	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visitUserFuncDef(this);
	}
}
