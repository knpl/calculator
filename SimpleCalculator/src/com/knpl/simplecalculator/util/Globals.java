package com.knpl.simplecalculator.util;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.knpl.simplecalculator.nodes.BuiltinConstDefs;
import com.knpl.simplecalculator.nodes.BuiltinFuncDefs;
import com.knpl.simplecalculator.nodes.ConstDef;
import com.knpl.simplecalculator.nodes.FuncDef;
import com.knpl.simplecalculator.nodes.UserConstDef;
import com.knpl.simplecalculator.nodes.UserFuncDef;

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
		userFuncDefMap = new LinkedHashMap<String, UserFuncDef>();
		userConstDefMap = new LinkedHashMap<String, UserConstDef>();
		
		FuncDef defs[] = BuiltinFuncDefs.builtinFuncDefs;
		for (int i = 0; i < defs.length; i++) {
			FuncDef def = defs[i];
			funcDefMap.put(def.getSignature().getName(), def);
		}
		
		ConstDef[] constDefs = BuiltinConstDefs.builtinConstDefs;
		for (int i = 0; i < constDefs.length; i++) {
			ConstDef def = constDefs[i];
			constDefMap.put(def.getName(), def);
		}
	}
	
	public Collection<UserFuncDef> getUserFuncDefs() {
		return Collections.unmodifiableCollection(userFuncDefMap.values());
	}
	
	public Collection<UserConstDef> getUserConstDefs() {
		return Collections.unmodifiableCollection(userConstDefMap.values());
	}
	
	public Collection<FuncDef> getBuiltinFuncDefs() {
		return Collections.unmodifiableCollection(funcDefMap.values());
	}
	
	public Collection<ConstDef> getBuiltinConstDefs() {
		return Collections.unmodifiableCollection(constDefMap.values());
	}
	
	public ConstDef getConstDef(String id) {
		ConstDef cd = constDefMap.get(id);
		if (cd != null)
			return cd;
		return userConstDefMap.get(id);
	}
	
	public FuncDef getFuncDef(String id) {
		FuncDef fd = funcDefMap.get(id);
		if (fd != null)
			return fd;
		return userFuncDefMap.get(id);
	}
	
	public UserConstDef getUserConstDef(String id) {
		return userConstDefMap.get(id);
	}
	
	public UserFuncDef getUserFuncDef(String id) {
		return userFuncDefMap.get(id);
	}

	public boolean putUserFuncDef(UserFuncDef funcDef) {
		String name = funcDef.getSignature().getName();
		if (funcDefMap.get(name) != null ||
			userFuncDefMap.get(name) != null) { 
			return false;
		}
		userFuncDefMap.put(name, funcDef);
		return true;
	}
	
	public boolean removeUserConstDef(String id) {
		return userConstDefMap.remove(id) != null;
	}
	
	public boolean removeUserFuncDef(String id) {
		return userFuncDefMap.remove(id) != null;
	}
	
	public boolean putUserConstDef(UserConstDef userConstDef) {
		String name = userConstDef.getName();
		if (constDefMap.get(name) != null ||
			userConstDefMap.get(name) != null) {
			return false;
		}
		userConstDefMap.put(name, userConstDef);
		return true;
	}	
	
	private void loadDefinitionsFromDatabase() {
		CalculatorDb.putAllUFDs();
		CalculatorDb.putAllUCDs();
	}
}
