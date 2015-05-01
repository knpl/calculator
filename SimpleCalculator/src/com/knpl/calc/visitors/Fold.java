package com.knpl.calc.visitors;

import java.util.ArrayList;
import java.util.List;

import com.knpl.calc.nodes.*;
import com.knpl.calc.nodes.defs.Const;
import com.knpl.calc.nodes.defs.Func;
import com.knpl.calc.nodes.numbers.Num;
import com.knpl.calc.nodes.operators.*;

public class Fold extends Visitor {
	
	private boolean isConstant;
	
	public Fold() {
		isConstant = false;
	}
	
	@Override
	public Expr visitBinOp(BinOp node) throws Exception {
		boolean constant = true;
		
		Expr lhs = (Expr) node.getLHS().accept(this);
		constant = constant && isConstant;
		Expr rhs = (Expr) node.getRHS().accept(this);
		constant = constant && isConstant;
		
		node.setLHS(lhs);
		node.setRHS(rhs);
		
		isConstant = constant;
		return constant ? node.numEvaluate((Num)lhs, (Num)rhs) : node;
	}
	
	@Override
	public Expr visitMonOp(MonOp node) throws Exception {
		Expr op = (Expr) node.getOp().accept(this);
		node.setOp(op);
		
		return isConstant ? node.numEvaluate((Num)op) : node;
	}
	
	@Override
	public Expr visitFunc(Func node) throws Exception {
		boolean constant = true;
		
		List<Expr> args = node.getArguments();
		for (int i = 0; i < args.size(); ++i) {
			args.set(i, (Expr) args.get(i).accept(this));
			constant = constant && isConstant;
		}
		
		isConstant = constant;
		if (constant) {
			List<Num> numArgs = new ArrayList<Num>(args.size());
			for (int i = 0; i < args.size(); ++i) {
				numArgs.add((Num) args.get(i));
			}
			return node.getFuncDef().getNum(numArgs);
		}
		else {
			return node;
		}
	}
	
	@Override
	public Expr visitNum(Num node) {
		isConstant = true;
		return node.copy();
	}
	
	@Override
	public Expr visitNumTok(NumTok node) {
		isConstant = true;
		return node.getRealDouble();
	}
	
	@Override
	public Expr visitConst(Const node) throws Exception {
		isConstant = true;
		return node.getConstDef().getNum();
	}
	
	@Override
	public Expr visitVar(Var node) {
		isConstant = false;
		return node;
	}
}
