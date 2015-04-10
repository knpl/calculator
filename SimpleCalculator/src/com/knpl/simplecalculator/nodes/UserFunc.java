package com.knpl.simplecalculator.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.knpl.simplecalculator.nodes.Expr;
import com.knpl.simplecalculator.nodes.Func;
import com.knpl.simplecalculator.util.UserFuncDef;
import com.knpl.simplecalculator.visitors.ComplexEvaluate;
import com.knpl.simplecalculator.visitors.Visitor;

public class UserFunc extends Func {
	private final List<Expr> arguments;
	
	public UserFunc(UserFuncDef definition, List<Expr> arguments) {
		this.definition = definition;
		this.arguments = new ArrayList<Expr>(arguments);
	}

	@Override
	public List<Expr> getArguments() {
		return arguments;
	}
	
	@Override
	public UserFuncDef getDefinition() {
		return (UserFuncDef) definition;
	}
	
	public Complex complexEvaluate(List<Complex> args) throws Exception {
		List<Var> params = definition.getSignature().getParameters();
		if (params.size() != args.size()) {
			throw new Exception("Argument mismatch while evaluating function " +
								definition.getSignature().getName());
		}
		
		Map<String, Complex> map = new HashMap<String, Complex>(params.size());
		Iterator<Complex> itArg  = args.iterator();
		Iterator<Var>  	  itParam = params.iterator();
		while (itParam.hasNext()) {
			map.put(itParam.next().getName(), itArg.next());
		}
		ComplexEvaluate eval = new ComplexEvaluate(map);
		return ((UserFuncDef) definition).getExpression().accept(eval, null);
	}
	
	@Override
	public <O, I> O accept(Visitor<O, I> v, I info) throws Exception {
		return v.visit(this, info);
	}

	@Override
	public Expr getArg(int i) {
		return arguments.get(i);
	}

}
