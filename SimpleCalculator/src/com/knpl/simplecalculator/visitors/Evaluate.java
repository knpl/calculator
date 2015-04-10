package com.knpl.simplecalculator.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knpl.simplecalculator.nodes.*;

public class Evaluate extends Visitor {
	
	private Map<String, Double> map;
	
	public Evaluate() {
		map = new HashMap<String, Double>();
	}
	
	public Evaluate(Map<String, Double> map) {
		this.map = map;
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
	public Double visit(Pow node) throws Exception {
		return Math.pow((Double) node.getLHS().accept(this),
				 		(Double) node.getRHS().accept(this));
	}
	
	@Override
	public Double visit(Minus node) throws Exception {
		return 	-((Double) node.getOp().accept(this));
	}

	@Override
	public Double visit(Num node) throws Exception {
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
	public Double visit(Func node) throws Exception {
		List<Double> args = new ArrayList<Double>();
		for (Expr e : node.getArguments()) {
			args.add((Double)e.accept(this));
		}
		return node.getDefinition().evaluate(args);
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
