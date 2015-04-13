package com.knpl.simplecalculator.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.knpl.simplecalculator.nodes.Expr;
import com.knpl.simplecalculator.nodes.Func;
import com.knpl.simplecalculator.nodes.Call;
import com.knpl.simplecalculator.nodes.ConstDef;
import com.knpl.simplecalculator.nodes.MVFunc;
import com.knpl.simplecalculator.nodes.SVFunc;
import com.knpl.simplecalculator.nodes.UserConstDef;

import com.knpl.simplecalculator.nodes.Builtins.Pi;
import com.knpl.simplecalculator.nodes.Builtins.Euler;
import com.knpl.simplecalculator.nodes.Builtins.Im;
import com.knpl.simplecalculator.storage.CalculatorDb;

public class Globals {
	private static Globals instance = null;
	
	public static Globals getInstance() {
		if (instance == null) {
			instance = new Globals();
			instance.loadDefinitionsFromDatabase();
		}
		return instance;
	}
	
	private final Map<String, FuncDef> funcDefMap;
	private final Map<String, ConstDef> constDefMap;
	
	private final Map<String, UserFuncDef> userFuncDefMap;
	private final Map<String, UserConstDef> userConstDefMap;
	
	private Globals() {
		funcDefMap = new LinkedHashMap<String, FuncDef>();
		constDefMap = new LinkedHashMap<String, ConstDef>();
		
		FuncDef defs[] = BuiltinFuncDefs.builtinFunctions;
		for (int i = 0; i < defs.length; i++) {
			FuncDef def = defs[i];
			funcDefMap.put(def.getSignature().getName(), def);
		}
		
		constDefMap.put(Pi.NAME, new Pi());
		constDefMap.put(Euler.NAME, new Euler());
		constDefMap.put(Im.NAME, new Im());
		
		userFuncDefMap = new LinkedHashMap<String, UserFuncDef>();
		userConstDefMap = new LinkedHashMap<String, UserConstDef>();
	}
	
	public Map<String, FuncDef> getFuncDefMap() {
		return funcDefMap;
	}
	
	public Map<String, ConstDef> getConstDefMap() {
		return constDefMap;
	}
	
	public Func createFunction(Call call) throws Exception {
		FuncDef def = funcDefMap.get(call.getName());
		if (def == null) {
			throw new Exception("Function " + call.getName() + " undefined");	
		}
		
		if (!call.match(def.getSignature())) {
			throw new Exception("Signature mismatch: "+ call.getName());
		}
		
		List<Expr> args = call.getArguments();
		if (args.size() == 1)
			return new SVFunc(def, args.get(0));
		else
			return new MVFunc(def, args);
	}
	
	public ConstDef getConstDef(String id) {
		return constDefMap.get(id);
	}
	
	public FuncDef getFuncDef(String id) {
		return funcDefMap.get(id);
	}

	public boolean putFuncDef(UserFuncDef funcDef) {
		boolean result = false;
		String name = funcDef.getSignature().getName();
		if (funcDefMap.get(name) == null) {
			funcDefMap.put(name, funcDef);
			result = true;
		}
		return result;
	}
	
	public boolean removeUserConstDef(String id) {
		return constDefMap.remove(id) != null;
	}
	
	public boolean removeUserFuncDef(String id) {
		FuncDef fd = funcDefMap.get(id);
		if (!(fd instanceof UserFuncDef)) {
			return false;
		}
		return funcDefMap.remove(id) != null;
	}
	
	public boolean putConstDef(ConstDef constDef) {
		boolean result = false;
		String name = constDef.getName();
		if (constDefMap.get(name) == null) {
			constDefMap.put(name, constDef);
			result = true;
		}
		return result;
	}	
	
	private void loadDefinitionsFromDatabase() {
		CalculatorDb.putAllUFDs();
		CalculatorDb.putAllUCDs();
	}
}
