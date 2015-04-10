package com.knpl.simplecalculator.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knpl.simplecalculator.nodes.*;

public class Evaluate extends Visitor<Double, Void> {
	
	private Map<String, Double> map;
	
	public Evaluate() {
		map = new HashMap<String, Double>();
	}
	
	public Evaluate(Map<String, Double> map) {
		this.map = map;
	}
	
	@Override
	public Double visit(Add node, Void info) throws Exception {
		return 	(Double) node.getLHS().accept(this, info) 
				+ (Double) node.getRHS().accept(this, info);
	}

	@Override
	public Double visit(Sub node, Void info) throws Exception {
		return 	(Double) node.getLHS().accept(this, info) 
				- (Double) node.getRHS().accept(this, info);
	}

	@Override
	public Double visit(Mul node, Void info) throws Exception {
		return 	(Double) node.getLHS().accept(this, info) 
				* (Double) node.getRHS().accept(this, info);
	}

	@Override
	public Double visit(Div node, Void info) throws Exception {
		return 	(Double) node.getLHS().accept(this, info) 
				/ (Double) node.getRHS().accept(this, info);
	}

	@Override
	public Double visit(Pow node, Void info) throws Exception {
		return Math.pow((Double) node.getLHS().accept(this, info),
				 		(Double) node.getRHS().accept(this, info));
	}
	
	@Override
	public Double visit(Minus node, Void info) throws Exception {
		return 	-((Double) node.getOp().accept(this, info));
	}

	@Override
	public Double visit(Num node, Void info) throws Exception {
		return node.getDouble();
	}
	
	@Override
	public Double visit(Var node, Void info) throws Exception {
		Double result = map.get(node.getName());
		if (result == null) {
			throw new Exception("Undeclared variable "+node.getName());
		}
		return result;
	}

	@Override
	public Double visit(Func node, Void info) throws Exception {
		List<Double> args = new ArrayList<Double>();
		for (Expr e : node.getArguments()) {
			args.add((Double)e.accept(this, info));
		}
		return node.getDefinition().evaluate(args);
	}
	
	@Override
	public Double visit(ConstDef node, Void info) throws Exception {
		return node.getDouble();
	}
	
	@Override
	public Double visit(Complex node, Void info) throws Exception {
		return node.im() == 0.0 ? node.re() : Double.NaN;
	} 
}
