package com.knpl.calc.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knpl.calc.nodes.operators.*;
import com.knpl.calc.nodes.Expr;
import com.knpl.calc.nodes.NumTok;
import com.knpl.calc.nodes.Var;
import com.knpl.calc.nodes.defs.Const;
import com.knpl.calc.nodes.defs.Func;
import com.knpl.calc.nodes.numbers.Num;
import com.knpl.calc.nodes.numbers.RealDouble;
import com.knpl.calc.parser.Lexer;
import com.knpl.calc.parser.Parser;
import com.knpl.calc.parser.TokenType;

public class NumEvaluate extends Visitor {
	private Map<String, Num> map;
	
	public NumEvaluate() {
		map = new HashMap<String, Num>();
	}
	
	public NumEvaluate(Map<String, Num> map) {
		this.map = map;
	}
	
	public static double fromString(String input) {
		Parser parser = new Parser(new Lexer(input));
		if (!(parser.expr() && parser.token(TokenType.EOF))) {
			return Double.NaN;
		}
		
		Expr expr = (Expr) parser.getResult();
		try {
			Resolve resolve = new Resolve();
			expr = (Expr) expr.accept(resolve);
			NumEvaluate evaluate = new NumEvaluate();
			
			Num num = (Num) expr.accept(evaluate);
			if (!(num instanceof RealDouble))
				return Double.NaN;
			else 
				return ((RealDouble) num).getValue();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return Double.NaN;
		}
	}
	
	@Override
	public Num visitBinOp(BinOp node) throws Exception {
		Num a = (Num) node.getLHS().accept(this);
		Num b = (Num) node.getRHS().accept(this);
		return node.numEvaluate(a, b);
	}
	
	@Override
	public Num visitMonOp(MonOp node) throws Exception {
		return node.numEvaluate((Num) node.getOp().accept(this));
	}
	
	@Override
	public Num visitNumTok(NumTok node) throws Exception {
		return node.getRealDouble();
	}
	
	@Override
	public Num visitVar(Var node) throws Exception {
		String name = node.getName();
		
		Num result = map.get(name);
		if (result == null) {
			throw new Exception("Unable to evaluate free variable: "+node.getName());
		}
		
		return result.copy();
	}
	
	@Override
	public Num visitFunc(Func node) throws Exception {
		List<Num> args = new ArrayList<Num>();
		for (Expr e : node.getArguments()) {
			args.add((Num)e.accept(this));
		}
		return node.getFuncDef().getNum(args);
	}
	
	@Override
	public Num visitConst(Const node) throws Exception {
		return node.getConstDef().getNum();
	}
	
	@Override
	public Num visitNum(Num node) throws Exception {
		return node.copy();
	}
}
