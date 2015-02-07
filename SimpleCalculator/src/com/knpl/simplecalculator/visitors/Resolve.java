package com.knpl.simplecalculator.visitors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.util.GlobalDefinitions;

public class Resolve extends Visitor {
	
	private Map<String,Var> freeVarMap;
	private Map<String,Var> boundedVarMap;
	
	public Resolve() {
		freeVarMap = new HashMap<String,Var>();
		boundedVarMap = new HashMap<String,Var>();
	}
	
	public Resolve(Map<String, Var> boundedVarMap) {
		freeVarMap = new HashMap<String, Var>();
		this.boundedVarMap = boundedVarMap;
	}
	
	
	public Map<String, Var> getFreeVarMap()  {
		return freeVarMap;
	}
	
	public Map<String, Var> getBoundedVarMap() {
		return boundedVarMap;
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
			boundedVarMap.put(param.getName(), param);
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
		
		Var variable = boundedVarMap.get(name);
		if (variable != null)
			return variable;
		
		variable = freeVarMap.get(name);
		if (variable != null)
			return variable;
		
		Num constant = GlobalDefinitions.getInstance().getConstant(name);
		if (constant != null)
			return constant;
		
		freeVarMap.put(name, node);
		
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
