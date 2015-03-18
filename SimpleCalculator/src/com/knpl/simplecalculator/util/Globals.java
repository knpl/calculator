package com.knpl.simplecalculator.util;

import java.util.LinkedHashMap;
import java.util.Map;

import com.knpl.simplecalculator.nodes.Func;
import com.knpl.simplecalculator.nodes.Call;
import com.knpl.simplecalculator.nodes.ConstDef;

import com.knpl.simplecalculator.nodes.Builtins.Pi;
import com.knpl.simplecalculator.nodes.Builtins.Euler;
import com.knpl.simplecalculator.nodes.Builtins.Im;

public class Globals {
	private static Globals instance = null;
	
	public static Globals getInstance() {
		if (instance == null) {
			instance = new Globals();
		}
		return instance;
	}
	
	private Map<String, FuncDef> funcDefMap;
	private Map<String, ConstDef> constDefMap;
	
	private Globals() {
		funcDefMap = new LinkedHashMap<String, FuncDef>();
		constDefMap = new LinkedHashMap<String, ConstDef>();
		
		FuncDef defs[] = BuiltinFuncDefs.builtinFunctions;
		for (int i = 0; i < defs.length; i++) {
			FuncDef def = defs[i];
			funcDefMap.put(def.getSignature().getName(), def);
		}
		
		constDefMap.put("pi", new Pi());
		constDefMap.put("e", new Euler());
		constDefMap.put("i", new Im());
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
		return def.createFunction(call);
	}
	
	public ConstDef getConstant(String id) {
		return constDefMap.get(id);
	}
	
	public FuncDef getFuncDef(String id) {
		return funcDefMap.get(id);
	}

	public boolean putFuncDef(FuncDef funcDef) {
		boolean result = false;
		String name = funcDef.getSignature().getName();
		if (funcDefMap.get(name) == null) {
			funcDefMap.put(name, funcDef);
			result = true;
		}
		return result;
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
}
