package com.knpl.simplecalculator.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import android.graphics.Color;
import com.knpl.simplecalculator.SimpleCalculatorActivity;
import com.knpl.simplecalculator.plot.Mapper;
import com.knpl.simplecalculator.plot.ProgramMapper;
import com.knpl.simplecalculator.visitors.Compile;
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
		else if (n == 1) {
			Var var = freeVariables.values().iterator().next();
			FuncDefNode def = new FuncDefNode(new Signature("expression", Arrays.asList(var)), node);
			Compile compile = new Compile();
			def.accept(compile);
			
			ArrayList<Mapper> mappers = new ArrayList<Mapper>(1);
			mappers.add(new ProgramMapper(compile.getProgram(), Color.BLUE));
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
