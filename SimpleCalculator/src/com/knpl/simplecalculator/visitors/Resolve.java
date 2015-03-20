package com.knpl.simplecalculator.visitors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.util.Globals;

public class Resolve extends Visitor<Node, Void> {
	
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
	public Node visit(FuncDefNode node, Void info) throws Exception {
		node.getSignature().accept(this, info);
		node.setExpression((Expr) node.getExpression().accept(this, info));
		
		return node;
	}
	
	@Override
	public Node visit(ConstDefNode node, Void info) throws Exception {
		node.setExpression((Expr) node.getExpression().accept(this, info));
		return node;
	}
	
	@Override
	public Node visit(Signature node, Void info) throws Exception {
		for (Var param : node.getParameters()) {
			boundedVarMap.put(param.getName(), param);
		}
		return node;
	}
	
	@Override
	public Node visit(BinOp node, Void info) throws Exception {
		node.setLHS((Expr) node.getLHS().accept(this, info));
		node.setRHS((Expr) node.getRHS().accept(this, info));
		return node;
	}

	@Override
	public Node visit(MonOp node, Void info) throws Exception {
		node.setOp((Expr) node.getOp().accept(this, info));
		return node;
	}
	
	@Override
	public Node visit(Num node, Void info) throws Exception {
		return node;
	}
	
	@Override
	public Node visit(Var node, Void info) throws Exception {
		String name = node.getName();
		
		Var variable = boundedVarMap.get(name);
		if (variable != null)
			return variable;
		
		variable = freeVarMap.get(name);
		if (variable != null)
			return variable;
		
		ConstDef constant = Globals.getInstance().getConstDef(name);
		if (constant != null)
			return constant;
		
		freeVarMap.put(name, node);
		
		return node;
	}
	
	@Override
	public Node visit(Call node, Void info) throws Exception {
		List<Expr> arguments = node.getArguments();
		for (int i = 0; i < arguments.size(); ++i) {
			arguments.set(i, (Expr) arguments.get(i).accept(this, info));
		}
		return Globals.getInstance().createFunction(node);
	}
	
	@Override
	public Node visit(Func node, Void info) throws Exception {
		List<Expr> arguments = node.getArguments();
		for (int i = 0; i < arguments.size(); ++i) {
			arguments.set(i, (Expr) arguments.get(i).accept(this, info));
		}
		return node;
	}
}
