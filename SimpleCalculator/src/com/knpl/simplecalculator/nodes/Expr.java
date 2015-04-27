package com.knpl.simplecalculator.nodes;

import java.util.Map;

import com.knpl.simplecalculator.SimpleCalculatorActivity;
import com.knpl.simplecalculator.visitors.NumEvaluate;
import com.knpl.simplecalculator.visitors.Resolve;
import com.knpl.simplecalculator.visitors.Visitor;

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
			calculator.print("Can't plot expression with free variables");
		}
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
