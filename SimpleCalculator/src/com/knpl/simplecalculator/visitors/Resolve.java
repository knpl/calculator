package com.knpl.simplecalculator.visitors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knpl.simplecalculator.nodes.*;
import com.knpl.simplecalculator.util.Globals;

public class Resolve extends Visitor {
	
	private Map<String, Var> freeVarMap;
	private Map<String, Var> boundedVarMap;
	private Map<String, UserFuncDef> ufdMap;
	private Map<String, UserConstDef> ucdMap;
	
	public Resolve() {
		freeVarMap = new HashMap<String, Var>();
		boundedVarMap = new HashMap<String, Var>();
		ufdMap = new HashMap<String, UserFuncDef>();
		ucdMap = new HashMap<String, UserConstDef>();
	}
	

	public Resolve(Map<String, UserFuncDef> ufdMap, Map<String, UserConstDef> ucdMap) {
		freeVarMap = new HashMap<String, Var>();
		boundedVarMap = new HashMap<String, Var>();
		this.ufdMap = ufdMap;
		this.ucdMap = ucdMap;
	}
	
	public Map<String, Var> getFreeVarMap()  {
		return freeVarMap;
	}
	
	public Map<String, Var> getBoundedVarMap() {
		return boundedVarMap;
	}
	
	public static Expr resolveUserFuncDef(UserFuncDef ufd) throws Exception {
		Signature sig = ufd.getSignature();
		Map<String, UserFuncDef> ufdMap = new HashMap<String, UserFuncDef>();
		Map<String, UserConstDef> ucdMap = new HashMap<String, UserConstDef>();
		ufdMap.put(sig.getName(), ufd);
		
		Resolve resolve = new Resolve(ufdMap, ucdMap);
		sig.accept(resolve);
		Expr result = (Expr) ufd.getExpression().accept(resolve);
		if (resolve.getFreeVarMap().size() != 0) {
			throw new Exception("Unable to compile function. Definition contains free variables.");
		}
		
		return result;
	}
	
	public static Expr resolveUserFuncDef(UserFuncDef ufd,
			Map<String, UserFuncDef> ufdMap, Map<String, UserConstDef> ucdMap) throws Exception {
		Signature sig = ufd.getSignature();
		if (ufdMap.containsKey(sig.getName())) {
			throw new Exception("Function "+sig.getName()+" is defined in terms of itself.");
		}
		ufdMap.put(sig.getName(), ufd);
		
		Resolve resolve = new Resolve(ufdMap, ucdMap);
		sig.accept(resolve);
		Expr result = (Expr) ufd.getExpression().accept(resolve);
		if (resolve.getFreeVarMap().size() != 0) {
			throw new Exception("Unable to resolve function. Definition contains free variables.");
		}
		
		return result;
	}
	
	public static Expr resolveUserConstDef(UserConstDef ucd) throws Exception {
		String name = ucd.getName();
		Map<String, UserFuncDef> ufdMap = new HashMap<String, UserFuncDef>();
		Map<String, UserConstDef> ucdMap = new HashMap<String, UserConstDef>();
		ucdMap.put(name, ucd);
		
		Resolve resolve = new Resolve(ufdMap, ucdMap);
		Expr result = (Expr) ucd.getExpression().accept(resolve);
		if (resolve.getFreeVarMap().size() != 0) {
			throw new Exception("Unable to resolve function. Definition contains free variables.");
		}
		
		return result;
	}
	
	public static Expr resolveUserConstDef(UserConstDef ucd,
			Map<String, UserFuncDef> ufdMap, Map<String, UserConstDef> ucdMap) throws Exception {
		String name = ucd.getName();
		if (ucdMap.containsKey(name)) {
			throw new Exception("Constant "+name+" is defined in terms of itself.");
		}
		ucdMap.put(name, ucd);
		
		Resolve resolve = new Resolve(ufdMap, ucdMap);
		Expr result = (Expr) ucd.getExpression().accept(resolve);
		if (resolve.getFreeVarMap().size() != 0) {
			throw new Exception("Unable to resolve constant. Definition contains free variables.");
		}
		
		return result;
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
	public Node visit(NumTok node) throws Exception {
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
		
		ConstDef def = Globals.getInstance().getConstDef(name);
		if (def != null) {
			def.accept(this);
			return new Const(def);	
		}
		
		freeVarMap.put(name, node);
		
		return node;
	}
	
	@Override
	public Node visit(Call node) throws Exception {
		FuncDef def = Globals.getInstance().getFuncDef(node.getName());
		if (def == null) {
			throw new Exception("Function " + node.getName() + " undefined");
		}
		else if (!node.match(def.getSignature())) {
			throw new Exception("Signature mismatch: "+ node.getName());
		}
		else {
			def.accept(this);
		}
		
		List<Expr> arguments = node.getArguments();
		for (int i = 0; i < arguments.size(); ++i) {
			arguments.set(i, (Expr) arguments.get(i).accept(this));
		}
		
		return new Func(def, arguments);
	}

	@Override
	public Node visit(FuncDef node) {
		return node;
	}
	
	@Override
	public Node visit(UserFuncDef node) throws Exception {
		node.resolve(ufdMap, ucdMap);
		return node;
	}
	
	@Override
	public Node visit(ConstDef node) {
		return node;
	}
	
	@Override
	public Node visit(UserConstDef node) throws Exception {
		node.resolve(ufdMap, ucdMap);
		return node;
	}
}
