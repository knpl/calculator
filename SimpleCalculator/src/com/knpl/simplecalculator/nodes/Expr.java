package com.knpl.simplecalculator.nodes;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.knpl.simplecalculator.SimpleCalculatorActivity;
import com.knpl.simplecalculator.plot.Mapper;
import com.knpl.simplecalculator.plot.ProgramXtoYMapper;
import com.knpl.simplecalculator.util.Pair;
import com.knpl.simplecalculator.visitors.Compile;
import com.knpl.simplecalculator.visitors.Evaluate;
import com.knpl.simplecalculator.visitors.Resolve;
import com.knpl.simplecalculator.visitors.Visitor;

public abstract class Expr extends Node {
	@Override
	public void execute(SimpleCalculatorActivity calculator) throws Exception {
		Resolve resolve = new Resolve();
		Expr node = (Expr) accept(resolve);
		
		Collection<Var> freeVariables = resolve.getFreeVarMap().values();
		
		int n = freeVariables.size();
		if (n == 0) {
			Evaluate v = new Evaluate();
			calculator.print(Double.toString((Double)node.accept(v)));
		}
		else if (n == 1) {
			List<Var> params = new ArrayList<Var>(1);
			params.add(freeVariables.iterator().next());
			FuncDefNode def = new FuncDefNode("expression", params, node);
			Compile compile = new Compile();
			def.accept(compile);
			
			ArrayList<Pair<Mapper, Integer>> mappers = new ArrayList<Pair<Mapper, Integer>>(1);
			mappers.add(
				new Pair<Mapper, Integer>(
					new ProgramXtoYMapper(compile.getProgram()),
					0xFF0000FF
				)
			);
			
			calculator.plot(mappers);
		}
		else {
			calculator.print("Can't plot expression with more than one free variable");
		}
	}
	
	@Override
	public Object accept(Visitor v) throws Exception {
		return v.visit(this);
	}
}
