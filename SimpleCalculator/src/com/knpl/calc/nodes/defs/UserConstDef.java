package com.knpl.calc.nodes.defs;

import java.util.Map;

import com.knpl.calc.nodes.Expr;
import com.knpl.calc.nodes.numbers.Num;
import com.knpl.calc.parser.Lexer;
import com.knpl.calc.parser.Parser;
import com.knpl.calc.visitors.Fold;
import com.knpl.calc.visitors.PrettyPrint;
import com.knpl.calc.visitors.Resolve;
import com.knpl.calc.visitors.Visitor;

public class UserConstDef extends ConstDef {
	private Expr expression;
	private String source;
	private Num val;
	private boolean resolved;

	public UserConstDef(String name, Expr expression) {
		super(name);
		this.expression = expression;
		
		source = "";
		val = null;
		resolved = false;
	}
	
	public static UserConstDef fromSource(String source) throws Exception {
		Parser parser = new Parser(new Lexer(source));
		if (!parser.constDef()) {
			throw new Exception("Syntax error");
		}
		return (UserConstDef) parser.getResult();
	}
	
	public Expr getExpression() {
		return expression;
	}
	
	public void resolve(Map<String, UserFuncDef> ufdMap,
						Map<String, UserConstDef> ucdMap) throws Exception {
		if (resolved) {
			return;
		}
		source = PrettyPrint.printUserConstDef(this);
		expression = Resolve.resolveUserConstDef(this, ufdMap, ucdMap);
		resolved = true;
	}
	
	public void resolve() throws Exception {
		if (resolved) {
			return;
		}
		source = PrettyPrint.printUserConstDef(this);
		expression = Resolve.resolveUserConstDef(this);
		resolved = true;
	}
	
	private void eval() throws Exception {
		resolve();
		expression = (Expr) expression.accept(new Fold());
		if (!(expression instanceof Num)) {
			throw new Exception("expression is not constant.");
		}
		val = (Num) expression;
	}
	
	@Override
	public Num getNum() throws Exception {
		if (val == null) {
			eval();
		}
		return val.copy();
	}
	
	public String getSource() throws Exception {
		resolve();
		return source;
	}
	
	@Override
	public String toString() {
		try {
			resolve();
			return source;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return name+" = invalid (Reason: "+ex.getMessage()+")";
		}
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visitUserConstDef(this);
	}
}
