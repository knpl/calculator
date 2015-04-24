package com.knpl.simplecalculator.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.special.Gamma;

import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.parser.Lexer;
import com.knpl.simplecalculator.parser.Parser;
import com.knpl.simplecalculator.parser.TokenType;

public class Evaluate extends Visitor {
	
	private Map<String, Double> map;
	
	public Evaluate() {
		map = new HashMap<String, Double>();
	}
	
	public Evaluate(Map<String, Double> map) {
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
			Evaluate evaluate = new Evaluate();
			return (Double) expr.accept(evaluate);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return Double.NaN;
		}
	}
	
	@Override
	public Double visit(Add node) throws Exception {
		return 	(Double) node.getLHS().accept(this) 
				+ (Double) node.getRHS().accept(this);
	}

	@Override
	public Double visit(Sub node) throws Exception {
		return 	(Double) node.getLHS().accept(this) 
				- (Double) node.getRHS().accept(this);
	}

	@Override
	public Double visit(Mul node) throws Exception {
		return 	(Double) node.getLHS().accept(this) 
				* (Double) node.getRHS().accept(this);
	}

	@Override
	public Double visit(Div node) throws Exception {
		return 	(Double) node.getLHS().accept(this) 
				/ (Double) node.getRHS().accept(this);
	}
	
	@Override
	public Double visit(Mod node) throws Exception {
		double a = (Double) node.getLHS().accept(this);
		double b = (Double) node.getRHS().accept(this);	
		return a - b * Math.floor(a/b);
	}

	@Override
	public Double visit(Pow node) throws Exception {
		return Math.pow((Double) node.getLHS().accept(this),
				 		(Double) node.getRHS().accept(this));
	}
	
	@Override
	public Double visit(Minus node) throws Exception {
		return 	-((Double) node.getOp().accept(this));
	}
	
	@Override
	public Double visit(Factorial node) throws Exception {
		return Gamma.gamma(1 + (Double) node.getOp().accept(this));
	}
	
	@Override
	public Double visit(DegToRad node) throws Exception {
		return (Math.PI/180) * (Double) node.getOp().accept(this);
	}

	@Override
	public Double visit(NumTok node) throws Exception {
		return node.getDouble();
	}
	
	@Override
	public Double visit(Var node) throws Exception {
		Double result = map.get(node.getName());
		if (result == null) {
			throw new Exception("Undeclared variable "+node.getName());
		}
		return result;
	}
	
	@Override
	public Double visit(SVFunc node) throws Exception {
		return node.getSVFuncDef().evaluate((Double) node.getArgument().accept(this));
	}

	@Override
	public Double visit(MVFunc node) throws Exception {
		List<Double> args = new ArrayList<Double>();
		for (Expr e : node.getArguments()) {
			args.add((Double)e.accept(this));
		}
		return node.getMVFuncDef().evaluate(args);
	}
	
	@Override
	public Double visit(ConstDef node) throws Exception {
		return node.getDouble();
	}
	
	@Override
	public Double visit(Complex node) throws Exception {
		return node.im() == 0.0 ? node.re() : Double.NaN;
	} 
}
