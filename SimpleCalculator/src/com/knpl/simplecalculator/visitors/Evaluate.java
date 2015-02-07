package com.knpl.simplecalculator.visitors;

import java.util.ArrayList;
import java.util.List;

import com.knpl.simplecalculator.nodes.Builtins.*;
import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.util.Program;
import com.knpl.simplecalculator.util.UserFuncDef;

public class Evaluate extends Visitor {
	
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
		return node.getValue();
	}

	@Override
	public Double visit(UserFunc node) throws Exception {
		Program p = ((UserFuncDef) node.getDefinition()).getProgram();
		List<Double> arguments = new ArrayList<Double>();
		for (Expr e : node.getArguments()) {
			arguments.add((Double)e.accept(this));
		}
		return p.evaluate(arguments);
	}
	
	@Override
	public Double visit(Min node) throws Exception {
		return Math.min((Double)node.getArg(0).accept(this),
						(Double)node.getArg(1).accept(this));
	}
	
	@Override
	public Double visit(Max node) throws Exception {
		return Math.max((Double)node.getArg(0).accept(this),
						(Double)node.getArg(1).accept(this));
	}
	
	@Override
	public Double visit(Sqrt node) throws Exception {
		return Math.sqrt((Double)node.getArg(0).accept(this));
	}
	
	@Override
	public Double visit(Abs node) throws Exception {
		return Math.abs((Double)node.getArg(0).accept(this));
	}
	
	@Override
	public Double visit(Log node) throws Exception {
		return Math.log((Double)node.getArg(0).accept(this));
	}
	
	@Override
	public Double visit(Exp node) throws Exception {
		return Math.exp((Double)node.getArg(0).accept(this));
	}
	
	@Override
	public Double visit(Sinh node) throws Exception {
		return Math.sinh((Double)node.getArg(0).accept(this));
	}
	
	@Override
	public Double visit(Cosh node) throws Exception {
		return Math.cosh((Double)node.getArg(0).accept(this));
	}
	
	@Override
	public Double visit(Sin node) throws Exception {
		return Math.sin((Double)node.getArg(0).accept(this));
	}
	
	@Override
	public Double visit(Cos node) throws Exception {
		return Math.cos((Double)node.getArg(0).accept(this));
	}
	
	@Override
	public Double visit(Tan node) throws Exception {
		return Math.tan((Double)node.getArg(0).accept(this));
	}
	
	@Override
	public Double visit(Asin node) throws Exception {
		return Math.asin((Double)node.getArg(0).accept(this));
	}
	
	@Override
	public Double visit(Acos node) throws Exception {
		return Math.acos((Double)node.getArg(0).accept(this));
	}
	
	@Override
	public Double visit(Atan node) throws Exception {
		return Math.atan((Double)node.getArg(0).accept(this));
	}
}
