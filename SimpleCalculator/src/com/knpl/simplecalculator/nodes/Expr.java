package com.knpl.simplecalculator.nodes;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.knpl.simplecalculator.SimpleCalculatorActivity;
import com.knpl.simplecalculator.plot.PathGenerator;
import com.knpl.simplecalculator.plot.ProgramXtoYMapper;
import com.knpl.simplecalculator.plot.RegularPathGenerator;
import com.knpl.simplecalculator.visitors.Compile;
import com.knpl.simplecalculator.visitors.Evaluate;
import com.knpl.simplecalculator.visitors.Resolve;
import com.knpl.simplecalculator.visitors.Visitor;

public abstract class Expr extends Node {
	@Override
	public void execute(SimpleCalculatorActivity calculator) throws Exception {
		Resolve resolve = new Resolve();
		Expr node = (Expr) accept(resolve);
		
		Collection<Var> freeVariables = resolve.getFreeVariableMap().values();
		
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
			
			ArrayList<PathGenerator> pglist = new ArrayList<PathGenerator>(1);
			pglist.add(
				new RegularPathGenerator(
					new ProgramXtoYMapper(compile.getProgram())
				)
			);
			
			calculator.plot(pglist);
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
