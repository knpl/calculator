package com.knpl.calc.nodes;

import java.util.Map;

import com.knpl.calc.SimpleCalculatorActivity;
import com.knpl.calc.visitors.NumEvaluate;
import com.knpl.calc.visitors.Resolve;
import com.knpl.calc.visitors.Visitor;

public abstract class Expr extends Node {
	
	@Override
	public void execute(SimpleCalculatorActivity calculator) throws Exception {
		Resolve resolve = new Resolve();
		Expr node = (Expr) accept(resolve);
		Map<String, Var> freeVariables = resolve.getFreeVarMap();
		
		int n = freeVariables.size();
		if (n == 0) {
			calculator.print((Num) node.accept(new NumEvaluate()));
		}
		else {
			throw new Exception("Can't evaluate expression with free variables");
		}
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
