package com.knpl.calc.nodes;

import java.util.Map;

import com.knpl.calc.MainInterface;
import com.knpl.calc.nodes.numbers.Num;
import com.knpl.calc.visitors.NumEvaluate;
import com.knpl.calc.visitors.Resolve;
import com.knpl.calc.visitors.Visitor;

public abstract class Expr extends Node {
	
	@Override
	public void execute(MainInterface interfaze) throws Exception {
		Resolve resolve = new Resolve();
		Expr node = (Expr) accept(resolve);
		Map<String, Var> freeVariables = resolve.getFreeVarMap();
		
		int n = freeVariables.size();
		if (n != 0) {
			throw new Exception("Can't evaluate expression with free variables.");
		}
		interfaze.print((Num) node.accept(new NumEvaluate()));
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visitExpr(this);
	}
}
