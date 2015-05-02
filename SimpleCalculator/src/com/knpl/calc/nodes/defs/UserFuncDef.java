package com.knpl.calc.nodes.defs;


import java.util.List;
import java.util.Map;

import com.knpl.calc.MainInterface;
import com.knpl.calc.nodes.Expr;
import com.knpl.calc.nodes.numbers.Num;
import com.knpl.calc.parser.Lexer;
import com.knpl.calc.parser.Parser;
import com.knpl.calc.storage.CalculatorDb;
import com.knpl.calc.util.Globals;
import com.knpl.calc.util.NumComputer;
import com.knpl.calc.util.Program;
import com.knpl.calc.visitors.Compile;
import com.knpl.calc.visitors.Fold;
import com.knpl.calc.visitors.PrettyPrint;
import com.knpl.calc.visitors.Resolve;
import com.knpl.calc.visitors.Visitor;

public class UserFuncDef extends FuncDef {
	private Expr expression;
	private String source;
	private Program program;
	private boolean resolved;
	private boolean folded;
	
	public UserFuncDef(Signature sig, Expr expr) {
		super(sig);
		this.expression = expr;
		this.source = "";
		this.program = null;
		resolved = false;
		folded = false;
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
	
	public void fold() throws Exception {
		if (folded) {
			return;
		}
		resolve();
		expression = (Expr) expression.accept(new Fold());
		folded = true;
	}
	
	private void compile() throws Exception {
		if (program != null) {
			return;
		}
		fold();
		program = Compile.compileUserFuncDef(this);
	}
	
	public Program getProgram() throws Exception {
		compile();
		return program;
	}

	@Override
	public Num getNum(List<Num> args) throws Exception {
		NumComputer comp = new NumComputer(getProgram());
		return comp.execute(args);
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
