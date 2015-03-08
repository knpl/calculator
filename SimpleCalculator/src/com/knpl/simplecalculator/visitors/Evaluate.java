package com.knpl.simplecalculator.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knpl.simplecalculator.nodes.Builtins.*;
import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.util.Program;
import com.knpl.simplecalculator.util.UserFuncDef;

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
		return node.getValue();
	}
	
	@Override
	public Double visit(Var node, Void info) throws Exception {
		Double result = map.get(node.getName());
		if (result == null) {
			throw new Exception("No mapping for variable "+node.getName());
		}
		return result;
	}

	@Override
	public Double visit(UserFunc node, Void info) throws Exception {
		Program p = ((UserFuncDef) node.getDefinition()).getProgram();
		List<Double> arguments = new ArrayList<Double>();
		for (Expr e : node.getArguments()) {
			arguments.add((Double)e.accept(this, info));
		}
		return p.evaluate(arguments);
	}
	
	@Override
	public Double visit(Min node, Void info) throws Exception {
		return Math.min((Double)node.getArg(0).accept(this, info),
						(Double)node.getArg(1).accept(this, info));
	}
	
	@Override
	public Double visit(Max node, Void info) throws Exception {
		return Math.max((Double)node.getArg(0).accept(this, info),
						(Double)node.getArg(1).accept(this, info));
	}
	
	@Override
	public Double visit(Sqrt node, Void info) throws Exception {
		return Math.sqrt((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Abs node, Void info) throws Exception {
		return Math.abs((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Log node, Void info) throws Exception {
		return Math.log((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Exp node, Void info) throws Exception {
		return Math.exp((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Sinh node, Void info) throws Exception {
		return Math.sinh((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Cosh node, Void info) throws Exception {
		return Math.cosh((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Sin node, Void info) throws Exception {
		return Math.sin((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Cos node, Void info) throws Exception {
		return Math.cos((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Tan node, Void info) throws Exception {
		return Math.tan((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Asin node, Void info) throws Exception {
		return Math.asin((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Acos node, Void info) throws Exception {
		return Math.acos((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Atan node, Void info) throws Exception {
		return Math.atan((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Constant node, Void info) throws Exception {
		return node.getDouble();
	}
}
