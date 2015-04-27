package com.knpl.simplecalculator.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.knpl.simplecalculator.nodes.Add;
import com.knpl.simplecalculator.nodes.Const;
import com.knpl.simplecalculator.nodes.DegToRad;
import com.knpl.simplecalculator.nodes.Div;
import com.knpl.simplecalculator.nodes.Expr;
import com.knpl.simplecalculator.nodes.Factorial;
import com.knpl.simplecalculator.nodes.Func;
import com.knpl.simplecalculator.nodes.Minus;
import com.knpl.simplecalculator.nodes.Mod;
import com.knpl.simplecalculator.nodes.Mul;
import com.knpl.simplecalculator.nodes.Num;
import com.knpl.simplecalculator.nodes.NumTok;
import com.knpl.simplecalculator.nodes.Pow;
import com.knpl.simplecalculator.nodes.RealDouble;
import com.knpl.simplecalculator.nodes.Sub;
import com.knpl.simplecalculator.nodes.Var;
import com.knpl.simplecalculator.parser.Lexer;
import com.knpl.simplecalculator.parser.Parser;
import com.knpl.simplecalculator.parser.TokenType;

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
	public Num visit(Add node) throws Exception {
		Num a = (Num) node.getLHS().accept(this);
		Num b = (Num) node.getRHS().accept(this);
		return a.add(b);
	}

	@Override
	public Num visit(Sub node) throws Exception {
		Num a = (Num) node.getLHS().accept(this);
		Num b = (Num) node.getRHS().accept(this);
		return a.sub(b);
	}

	@Override
	public Num visit(Mul node) throws Exception {
		Num a = (Num) node.getLHS().accept(this);
		Num b = (Num) node.getRHS().accept(this);
		return a.mul(b);
	}

	@Override
	public Num visit(Div node) throws Exception {
		Num a = (Num) node.getLHS().accept(this);
		Num b = (Num) node.getRHS().accept(this);
		return a.div(b);
	}
	
	@Override
	public Num visit(Mod node) throws Exception {
		Num a = (Num) node.getLHS().accept(this);
		Num b = (Num) node.getRHS().accept(this);
		return a.mod(b);
	}

	@Override
	public Num visit(Pow node) throws Exception {
		Num a = (Num) node.getLHS().accept(this);
		Num b = (Num) node.getRHS().accept(this);
		return a.pow(b);
	}
	
	@Override
	public Num visit(Minus node) throws Exception {
		return ((Num) node.getOp().accept(this)).neg();
	}
	
	@Override
	public Num visit(Factorial node) throws Exception {
		return ((Num) node.getOp().accept(this)).factorial();
	}
	
	@Override
	public Num visit(DegToRad node) throws Exception {
		return ((Num) node.getOp().accept(this)).deg2rad();
	}

	@Override
	public Num visit(NumTok node) throws Exception {
		return node.getRealDouble();
	}
	
	@Override
	public Num visit(Var node) throws Exception {
		String name = node.getName();
		
		Num result = map.get(name);
		if (result == null) {
			throw new Exception("Unable to evaluate free variable: "+node.getName());
		}
		
		return result.copy();
	}
	
	@Override
	public Num visit(Func node) throws Exception {
		List<Num> args = new ArrayList<Num>();
		for (Expr e : node.getArguments()) {
			args.add((Num)e.accept(this));
		}
		return node.getFuncDef().numEvaluate(args);
	}
	
	@Override
	public Num visit(Const node) throws Exception {
		return node.getConstDef().getNum();
	}
	
	@Override
	public Num visit(Num node) throws Exception {
		return node.copy();
	}
}
