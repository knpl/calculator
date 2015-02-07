package com.knpl.simplecalculator.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.knpl.simplecalculator.nodes.Func;
import com.knpl.simplecalculator.nodes.Call;
import com.knpl.simplecalculator.nodes.Num;

public class GlobalDefinitions {
	private static GlobalDefinitions instance = null;
	
	private Map<String, FunctionDefinition> builtinFuncDefMap;
	private Map<String, UserFuncDef> userFuncDefMap;
	private Map<String, Num> constDefMap;
	
	private GlobalDefinitions() {
		builtinFuncDefMap = new HashMap<String, FunctionDefinition>();
		userFuncDefMap = new HashMap<String, UserFuncDef>();
		constDefMap = new HashMap<String, Num>();
		
		FunctionDefinition defs[] = BuiltinFuncDefs.builtinFunctions;
		for (int i = 0; i < defs.length; i++) {
			builtinFuncDefMap.put(defs[i].getSignature().getName(), defs[i]);
		}
		
		constDefMap.put("pi", new Num(Math.PI));
		constDefMap.put("e", new Num(Math.E));
	}
	
	private static void createInstance() {
		if (instance == null)
			instance = new GlobalDefinitions();
	}
	
	public static GlobalDefinitions getInstance() {
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
	
	public Num getConstant(String id) {
		return constDefMap.get(id);
	}
	
	public FunctionDefinition getFunctionDefinition(String id) {
		FunctionDefinition fd;
		fd = builtinFuncDefMap.get(id);
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
	
	public boolean putConstant(String id, Num constant) {
		boolean result = false;
		Num c = constDefMap.get(id);
		if (c == null) {
			constDefMap.put(id, constant);
			result = true;
		}
		return result;
	}
	
	public UserFuncDef putUserFuncDef(UserFuncDef ufd) {
		String name = ufd.getSignature().getName();
		return userFuncDefMap.put(name, ufd);
	}
	
}
