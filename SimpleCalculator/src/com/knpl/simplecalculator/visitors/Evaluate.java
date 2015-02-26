package com.knpl.simplecalculator.visitors;

import java.util.ArrayList;
import java.util.List;

import com.knpl.simplecalculator.nodes.Builtins.*;
import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.util.Program;
import com.knpl.simplecalculator.util.UserFuncDef;

public class Evaluate extends Visitor {
	
	@Override
	public Double visit(Add node, Object info) throws Exception {
		return 	(Double) node.getLHS().accept(this, info) 
				+ (Double) node.getRHS().accept(this, info);
	}

	@Override
	public Double visit(Sub node, Object info) throws Exception {
		return 	(Double) node.getLHS().accept(this, info) 
				- (Double) node.getRHS().accept(this, info);
	}

	@Override
	public Double visit(Mul node, Object info) throws Exception {
		return 	(Double) node.getLHS().accept(this, info) 
				* (Double) node.getRHS().accept(this, info);
	}

	@Override
	public Double visit(Div node, Object info) throws Exception {
		return 	(Double) node.getLHS().accept(this, info) 
				/ (Double) node.getRHS().accept(this, info);
	}

	@Override
	public Double visit(Pow node, Object info) throws Exception {
		return Math.pow((Double) node.getLHS().accept(this, info),
				 		(Double) node.getRHS().accept(this, info));
	}
	
	@Override
	public Double visit(Minus node, Object info) throws Exception {
		return 	-((Double) node.getOp().accept(this, info));
	}

	@Override
	public Double visit(Num node, Object info) throws Exception {
		return node.getValue();
	}

	@Override
	public Double visit(UserFunc node, Object info) throws Exception {
		Program p = ((UserFuncDef) node.getDefinition()).getProgram();
		List<Double> arguments = new ArrayList<Double>();
		for (Expr e : node.getArguments()) {
			arguments.add((Double)e.accept(this, info));
		}
		return p.evaluate(arguments);
	}
	
	@Override
	public Double visit(Min node, Object info) throws Exception {
		return Math.min((Double)node.getArg(0).accept(this, info),
						(Double)node.getArg(1).accept(this, info));
	}
	
	@Override
	public Double visit(Max node, Object info) throws Exception {
		return Math.max((Double)node.getArg(0).accept(this, info),
						(Double)node.getArg(1).accept(this, info));
	}
	
	@Override
	public Double visit(Sqrt node, Object info) throws Exception {
		return Math.sqrt((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Abs node, Object info) throws Exception {
		return Math.abs((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Log node, Object info) throws Exception {
		return Math.log((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Exp node, Object info) throws Exception {
		return Math.exp((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Sinh node, Object info) throws Exception {
		return Math.sinh((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Cosh node, Object info) throws Exception {
		return Math.cosh((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Sin node, Object info) throws Exception {
		return Math.sin((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Cos node, Object info) throws Exception {
		return Math.cos((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Tan node, Object info) throws Exception {
		return Math.tan((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Asin node, Object info) throws Exception {
		return Math.asin((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Acos node, Object info) throws Exception {
		return Math.acos((Double)node.getArg(0).accept(this, info));
	}
	
	@Override
	public Double visit(Atan node, Object info) throws Exception {
		return Math.atan((Double)node.getArg(0).accept(this, info));
	}
}
