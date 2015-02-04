package com.knpl.simplecalculator.visitors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knpl.simplecalculator.GlobalDefinitions;
import com.knpl.simplecalculator.nodes.*;

public class Resolve extends Visitor {
	
	private Map<String,Var> freeVariableMap;
	private Map<String,Var> boundedVariableMap;
	
	public Resolve() {
		freeVariableMap = new HashMap<String,Var>();
		boundedVariableMap = new HashMap<String,Var>();
	}
	
	public Map<String, Var> getFreeVariableMap()  {
		return freeVariableMap;
	}
	
	public Map<String, Var> getBoundedVariableMap() {
		return boundedVariableMap;
	}
	
	@Override
	public Node visit(FuncDefNode node) throws Exception {
		node.getSignature().accept(this);
		node.setExpression((Expr) node.getExpression().accept(this));
		
		return node;
	}
	
	@Override
	public Node visit(Signature node) throws Exception {
		for (Var param : node.getParameters()) {
			boundedVariableMap.put(param.getName(), param);
		}
		return node;
	}
	
	@Override
	public Node visit(BinOp node) throws Exception {
		node.setLHS((Expr) node.getLHS().accept(this));
		node.setRHS((Expr) node.getRHS().accept(this));
		return node;
	}

	@Override
	public Node visit(MonOp node) throws Exception {
		node.setOp((Expr) node.getOp().accept(this));
		return node;
	}
	
	@Override
	public Node visit(Num node) throws Exception {
		return node;
	}
	
	@Override
	public Node visit(Var node) throws Exception {
		String name = node.getName();
		
		Var variable = boundedVariableMap.get(name);
		if (variable != null)
			return variable;
		
		variable = freeVariableMap.get(name);
		if (variable != null)
			return variable;
		
		Num constant = GlobalDefinitions.getInstance().getConstant(name);
		if (constant != null)
			return constant;
		
		freeVariableMap.put(name, node);
		
		return node;
	}
	
	@Override
	public Node visit(Call node) throws Exception {
		List<Expr> arguments = node.getArguments();
		for (int i = 0; i < arguments.size(); ++i) {
			arguments.set(i, (Expr) arguments.get(i).accept(this));
		}
		return GlobalDefinitions.getInstance().createFunction(node);
	}
	
	@Override
	public Node visit(Func node) throws Exception {
		List<Expr> arguments = node.getArguments();
		for (int i = 0; i < arguments.size(); ++i) {
			arguments.set(i, (Expr) arguments.get(i).accept(this));
		}
		return node;
	}
}
