package com.knpl.simplecalculator.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.knpl.simplecalculator.nodes.Func;
import com.knpl.simplecalculator.nodes.Call;
import com.knpl.simplecalculator.nodes.Constant;
import com.knpl.simplecalculator.nodes.UserConst;

import com.knpl.simplecalculator.nodes.Builtins.Pi;
import com.knpl.simplecalculator.nodes.Builtins.Euler;
import com.knpl.simplecalculator.nodes.Builtins.Im;

public class Globals {
	private static Globals instance = null;
	
	private Map<String, FunctionDefinition> builtinFuncDefMap;
	private Map<String, UserFuncDef> userFuncDefMap;
	private Map<String, Constant> constDefMap;
	private Map<String, UserConst> userConstDefMap;
	
	private Globals() {
		builtinFuncDefMap = new HashMap<String, FunctionDefinition>();
		userFuncDefMap = new HashMap<String, UserFuncDef>();
		constDefMap = new HashMap<String, Constant>();
		userConstDefMap = new HashMap<String, UserConst>();
		
		FunctionDefinition defs[] = BuiltinFuncDefs.builtinFunctions;
		for (int i = 0; i < defs.length; i++) {
			builtinFuncDefMap.put(defs[i].getSignature().getName(), defs[i]);
		}
		
		constDefMap.put("pi", new Pi());
		constDefMap.put("e", new Euler());
		constDefMap.put("i", new Im());
	}
	
	private static void createInstance() {
		if (instance == null)
			instance = new Globals();
	}
	
	public static Globals getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}
	
	public Func createFunction(Call call) throws Exception {
		FunctionDefinition def;
		
		def= builtinFuncDefMap.get(call.getName());
		if (def != null) {
			return def.createFunction(call);	
		}
		
		def= userFuncDefMap.get(call.getName());
		if (def != null) {
			return def.createFunction(call);	
		}
		
		throw new Exception("Function \"" + call.getName() + "\" undefined");
	}
	
	public Constant getConstant(String id) {
		Constant constant = constDefMap.get(id);
		if (constant != null) {
			return constant;
		}
		return userConstDefMap.get(id);
	}
	
	public FunctionDefinition getFunctionDefinition(String id) {
		FunctionDefinition fd = builtinFuncDefMap.get(id);
		if (fd != null) {
			return fd;
		}
		return userFuncDefMap.get(id);
	}
	
	public FunctionDefinition getBuiltinFuncDef(String id) {
		return builtinFuncDefMap.get(id);
	}
	
	public UserFuncDef getUserFuncDef(String id) {
		return userFuncDefMap.get(id);
	}
	
	public UserFuncDef removeUserFuncDef(String id) {
		return userFuncDefMap.remove(id);
	}
	
	public Collection<UserFuncDef> getUserFuncDefs() {
		return userFuncDefMap.values();
	}
	
	public boolean putUserConstDef(String id, UserConst userConstDef) {
		boolean result = false;
		if (userConstDefMap.get(id) == null) {
			userConstDefMap.put(id, userConstDef);
			result = true;
		}
		return result;
	}
	
	public UserFuncDef putUserFuncDef(UserFuncDef ufd) {
		String name = ufd.getSignature().getName();
		return userFuncDefMap.put(name, ufd);
	}
	
}
