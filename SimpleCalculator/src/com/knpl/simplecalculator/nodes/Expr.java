package com.knpl.simplecalculator.nodes;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import android.graphics.Color;
import com.knpl.simplecalculator.SimpleCalculatorActivity;
import com.knpl.simplecalculator.plot.Mapper;
import com.knpl.simplecalculator.plot.ProgramMapper;
import com.knpl.simplecalculator.util.Pair;
import com.knpl.simplecalculator.visitors.Compile;
import com.knpl.simplecalculator.visitors.Evaluate;
import com.knpl.simplecalculator.visitors.Resolve;
import com.knpl.simplecalculator.visitors.Visitor;

public abstract class Expr extends Node {
	
	@Override
	public void execute(SimpleCalculatorActivity calculator) throws Exception {
		Resolve resolve = new Resolve();
		Expr node = (Expr) accept(resolve, null);
		
		Map<String, Var> freeVariables = resolve.getFreeVarMap();
		
		int n = freeVariables.size();
		if (n == 0) {
			Evaluate v = new Evaluate();
			calculator.print(Double.toString((Double)node.accept(v, null)));
		}
		else if (n == 1) {
			Var var = freeVariables.values().iterator().next();
			FuncDefNode def = new FuncDefNode(new Signature("expression", Arrays.asList(var)), node);
			Compile compile = new Compile();
			def.accept(compile, null);
			
			ArrayList<Pair<Mapper, Integer>> mappers = new ArrayList<Pair<Mapper, Integer>>(1);
			mappers.add(
				new Pair<Mapper, Integer>(
					new ProgramMapper(compile.getProgram()), Color.BLUE
				)
			);
			
			calculator.plot(mappers);
		}
		else {
			calculator.print("Can't plot expression with more than one free variable");
		}
	}
	
	@Override
	public Object accept(Visitor v, Object info) throws Exception {
		return v.visit(this, null);
	}
}
